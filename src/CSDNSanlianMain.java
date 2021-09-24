import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CSDNSanlianMain {

    //这里改成自己的文件路径就可以了
    private static final InputStream sanlianInputStream = CSDNSanlianMain.class.getResourceAsStream("NameAndFolderId.txt");

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = CSDNSanlianMain.class.getClassLoader().getResourceAsStream("articleUrl.properties");
        // 使用properties对象加载输入流
        properties.load(in);
        //获取key对应的value值
        //新文章的url，查看谁没有收藏这篇文章
        String articleUrl = properties.getProperty("articleUrl");
        printUnSanlianUser(articleUrl);
    }


    private static void printUnSanlianUser(String articleUrl) {
        //读取参与三连的用户
        List<PairBean<String, String>> sanlianUserList = readFileContent();
        List<PairBean<String, String>> unSanLianUser = new ArrayList<>();
        //遍历所有参与三连的用户
        for (int i = 0; i < sanlianUserList.size(); i++) {
            PairBean<String, String> sanLianUserBean = sanlianUserList.get(i);
            //读取当前用户的所有收藏夹
            UserFavoriteBean allFavorite = getAllFavorite(sanLianUserBean.getFirst());
            List<UserFavoriteBean.DataDTO.ListDTO> allFavoriteList = allFavorite.getData().getList();
            //是否收藏
            boolean isFavorite = false;
            //遍历当前用户的所有收藏夹
            for (int j = 0; j < allFavoriteList.size() && !isFavorite; j++) {
                UserFavoriteBean.DataDTO.ListDTO favoriteListDTO = allFavoriteList.get(j);
                //读取当前收藏夹所收藏的文章
                FavoriteBean favoriteBean = getFavoriteBean(sanLianUserBean.getFirst(), favoriteListDTO.getId());
                List<FavoriteBean.DataDTO.ListDTO> favoriteList = favoriteBean.getData().getList();
                //判断当前收藏夹收藏的所有文章中有没有我的文章
                for (int k = 0; k < favoriteList.size(); k++) {
                    if (articleUrl.equalsIgnoreCase(favoriteList.get(k).getUrl())) {
                        System.out.println("博主\"" + sanLianUserBean.getSecond() + "\" 收藏了我的文章，他的收藏夹地址是：" + favoriteUrl(sanLianUserBean.getFirst()));
                        isFavorite = true;
                        break;
                    }
                }
            }
            if (!isFavorite) {
                //记录没有收藏我文章的用户
                unSanLianUser.add(sanLianUserBean);
                System.err.println("博主\"" + sanLianUserBean.getSecond() + "\" 没有收藏我的文章，他的收藏夹地址是：" + favoriteUrl(sanLianUserBean.getFirst()));
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("\n\n抓取了" + sanlianUserList.size() + "个博主的收藏文件夹数据，其中有" + unSanLianUser.size() + "个博主没有对我的文章进行收藏");
        System.err.println("下面是对我文章没有收藏的博主，当前正在统计三连的文章链接：" + articleUrl);
        //打印所有没收藏我文章的用户
        for (int i = 0; i < unSanLianUser.size(); i++) {
            System.err.println("博主" + unSanLianUser.get(i).getSecond() + "没有收藏我的文章，他的收藏夹地址是：" + favoriteUrl(unSanLianUser.get(i).getFirst()));
        }
    }


    private static String favoriteUrl(String userId) {
        return "https://blog.csdn.net/" + userId + "?type=collect";
    }


    private static FavoriteBean getFavoriteBean(String userName, String folderId) {
        String data = sendGet(getFavoritePath(userName, folderId));
        Gson gson = new Gson();
        FavoriteBean mFavoriteBean = gson.fromJson(data, FavoriteBean.class);
        return mFavoriteBean;
    }

    //获取用户的所有文件夹
    private static UserFavoriteBean getAllFavorite(String userId) {
        String url = "https://blog.csdn.net//phoenix/web/v1/collect-folder/created-list?page=1&pageSize=40&blogUsername=" + userId;
        String data = sendGet(url);
        Gson gson = new Gson();
        UserFavoriteBean mUserFavoriteBean = gson.fromJson(data, UserFavoriteBean.class);
        return mUserFavoriteBean;
    }


    /**
     * @param userName 用户名
     * @param count    一次抓取多少数据
     * @return
     */
    private static String getFavoritePath(String userName, String folderId, int count) {
        return "https://blog.csdn.net/community/home-api/v1/get-favorites-item-list?blogUsername=" + userName + "&folderId=" + folderId + "&page=1&pageSize=" + count;
    }

    private static String getFavoritePath(String userName, String folderId) {
        return "https://blog.csdn.net/community/home-api/v1/get-favorites-item-list?blogUsername=" + userName + "&folderId=" + folderId + "&page=1&pageSize=" + 200;
    }


    private static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    private static String getUserName(String userId) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL("https://blog.csdn.net/" + userId);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("nickName") || line.contains("nickname")) {
                    System.out.println(line);
                    break;
//                    return line.substring(line.indexOf("_"),)
                }
            }
        } catch (Exception e) {
            System.err.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static List<PairBean<String, String>> readFileContent() {
        BufferedReader reader = null;
        List<PairBean<String, String>> mList = new ArrayList<>();
        try {
            //reader = new BufferedReader(new FileReader(file));
            reader = new BufferedReader(new InputStreamReader(sanlianInputStream, "UTF-8"));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                if (tempStr.trim().length() == 0)
                    continue;
                int gapIndex = tempStr.indexOf("(");
                mList.add(new PairBean(tempStr.substring(0, gapIndex), tempStr.substring(gapIndex)));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return mList;
    }
}
