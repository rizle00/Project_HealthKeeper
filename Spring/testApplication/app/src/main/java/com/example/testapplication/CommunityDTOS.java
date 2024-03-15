package com.example.testapplication;


public class CommunityDTOS {

  public class Community_BoardDTO{
        private int  member_age, member_number;
        private String writer, title,msg, time, comments,content;
      public String getContent() {
          return content;
      }

      public void setContent(String content) {
          this.content = content;
      }

      public String getTime() {
          return time;
      }

      public void setTime(String time) {
          this.time = time;
      }

      public String getComments() {
          return comments;
      }

      public void setComments(String comments) {
          this.comments = comments;
      }

      public Community_BoardDTO(String title, String writer, String time) {
            this.title = title;
            this.writer = writer;
            this.time = time;
        }

        public int getMember_number() {
            return member_number;
        }

        public void setMember_number(int member_number) {
            this.member_number = member_number;
        }

        public int getMember_age() {
            return member_age;
        }

        public void setMember_age(int member_age) {
            this.member_age = member_age;
        }

        public String getWriter() {
            return writer;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


      public void getId() {
      }


  }






    public class Community_QuestionDTO{
       private String Notice_id;
        private String TITLE;
        private String CONTENT;
        private String TIME;
        private String MEMBER_ID;
        private String CATEGORY_ID;
        private String READ_CNT;

        public String getNotice_id() {
            return Notice_id;
        }

        public void setNotice_id(String QUE_IDQUE_ID) {
            this.Notice_id = QUE_IDQUE_ID;
        }

        public String getTITLE() {
            return TITLE;
        }

        public void setTITLE(String TITLE) {
            this.TITLE = TITLE;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public String getTIME() {
            return TIME;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }

        public String getMEMBER_ID() {
            return MEMBER_ID;
        }

        public void setMEMBER_ID(String MEMBER_ID) {
            this.MEMBER_ID = MEMBER_ID;
        }



        public String getCATEGORY_ID() {
            return CATEGORY_ID;
        }

        public void setCATEGORY_ID(String CATEGORY_ID) {
            this.CATEGORY_ID = CATEGORY_ID;
        }

        public String getREAD_CNT() {
            return READ_CNT;
        }

        public void setREAD_CNT(String READ_CNT) {
            this.READ_CNT = READ_CNT;
        }



    }







    public class Community_NoticeDTO {

        private String category, noticeTitle, date, time, content;


        public Community_NoticeDTO(String category, String noticeTitle, String date, String time, String content) {
            this.category = category;
            this.noticeTitle = noticeTitle;
            this.date = date;
            this.time = time;
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


    }
}
