package com.jinzht.pro.bean;

import java.util.List;

/**
 * 站内信列表
 */
public class MessageListBean {

    /**
     * data : [{"messageId":1,"title":"金指投","messagetype":{"messageTypeId":1,"name":"系统公告"},"content":"1","url":"http://www.jinzht.com","messageDate":"2016-06-30 22:32:23","isRead":true}]
     * message :
     * status : 200
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * messageId : 1
         * title : 金指投
         * messagetype : {"messageTypeId":1,"name":"系统公告"}
         * content : 1
         * url : http://www.jinzht.com
         * messageDate : 2016-06-30 22:32:23
         * isRead : true
         */
        private int messageId;
        private String title;
        private MessagetypeBean messagetype;// messageTypeId == 1表示web形式，其他纯文字表示
        private String content;
        private String url;
        private String messageDate;
        private boolean isRead;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public MessagetypeBean getMessagetype() {
            return messagetype;
        }

        public void setMessagetype(MessagetypeBean messagetype) {
            this.messagetype = messagetype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMessageDate() {
            return messageDate;
        }

        public void setMessageDate(String messageDate) {
            this.messageDate = messageDate;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public static class MessagetypeBean {
            /**
             * messageTypeId : 1
             * name : 系统公告
             */
            private int messageTypeId;
            private String name;

            public int getMessageTypeId() {
                return messageTypeId;
            }

            public void setMessageTypeId(int messageTypeId) {
                this.messageTypeId = messageTypeId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}