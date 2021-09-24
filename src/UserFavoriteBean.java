import java.util.List;

public class UserFavoriteBean {

    /**
     * code : 200
     * message : success
     * data : {"list":[{"id":10753332,"name":"默认收藏夹","username":"as604049322","detailUrl":null,"favoriteNum":187,"followNum":0,"loginUserNameIsFollow":false,"updateTime":1631601014000,"updatedAt":"2021-09-14T06:30:14.000+0000","dateFormat":"1 小时前"}],"total":1}
     */

    private int code;
    private String message;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * list : [{"id":10753332,"name":"默认收藏夹","username":"as604049322","detailUrl":null,"favoriteNum":187,"followNum":0,"loginUserNameIsFollow":false,"updateTime":1631601014000,"updatedAt":"2021-09-14T06:30:14.000+0000","dateFormat":"1 小时前"}]
         * total : 1
         */

        private int total;
        private List<ListDTO> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public static class ListDTO {
            /**
             * id : 10753332
             * name : 默认收藏夹
             * username : as604049322
             * detailUrl : null
             * favoriteNum : 187
             * followNum : 0
             * loginUserNameIsFollow : false
             * updateTime : 1631601014000
             * updatedAt : 2021-09-14T06:30:14.000+0000
             * dateFormat : 1 小时前
             */

            private String id;
            private String name;
            private String username;
            private String detailUrl;
            private int favoriteNum;
            private int followNum;
            private boolean loginUserNameIsFollow;
            private long updateTime;
            private String updatedAt;
            private String dateFormat;


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public int getFavoriteNum() {
                return favoriteNum;
            }

            public void setFavoriteNum(int favoriteNum) {
                this.favoriteNum = favoriteNum;
            }

            public int getFollowNum() {
                return followNum;
            }

            public void setFollowNum(int followNum) {
                this.followNum = followNum;
            }

            public boolean isLoginUserNameIsFollow() {
                return loginUserNameIsFollow;
            }

            public void setLoginUserNameIsFollow(boolean loginUserNameIsFollow) {
                this.loginUserNameIsFollow = loginUserNameIsFollow;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getDateFormat() {
                return dateFormat;
            }

            public void setDateFormat(String dateFormat) {
                this.dateFormat = dateFormat;
            }
        }
    }
}
