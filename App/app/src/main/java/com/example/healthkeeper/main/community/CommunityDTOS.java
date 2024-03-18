package com.example.healthkeeper.main.community;

import java.io.Serializable;
import java.util.List;


public class CommunityDTOS {

  public class Community_faqDTO{//자주하는 질문 게시판
      private  String FAQ_ID, TITLE, CONTENT, MEMBER_ID;

      public Community_faqDTO(String TITLE, String CONTENT, String MEMBER_ID) {
          this.TITLE = TITLE;
          this.CONTENT = CONTENT;
          this.MEMBER_ID = MEMBER_ID;
      }

      public String getFAQ_ID() {
          return FAQ_ID;
      }

      public void setFAQ_ID(String FAQ_ID) {
          this.FAQ_ID = FAQ_ID;
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

      public String getMEMBER_ID() {
          return MEMBER_ID;
      }

      public void setMEMBER_ID(String MEMBER_ID) {
          this.MEMBER_ID = MEMBER_ID;
      }
  }






    public class Community_QuestionDTO{//질문 게시판
      private String QUE_ID, TITLE, CONTENT, TIME, MEMBER_ID, SECRET, CATEGORY_ID, READ_CNT;
        public Community_QuestionDTO(String TITLE, String CONTENT, String TIME, String MEMBER_ID, String SECRET, String CATEGORY_ID, String READ_CNT) {
            this.TITLE = TITLE;
            this.CONTENT = CONTENT;
            this.TIME = TIME;
            this.MEMBER_ID = MEMBER_ID;
            this.SECRET = SECRET;
            this.CATEGORY_ID = CATEGORY_ID;
            this.READ_CNT = READ_CNT;
        }

        public String getQUE_ID() {
            return QUE_ID;
        }

        public void setQUE_ID(String QUE_ID) {
            this.QUE_ID = QUE_ID;
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

        public String getSECRET() {
            return SECRET;
        }

        public void setSECRET(String SECRET) {
            this.SECRET = SECRET;
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

    public class AnswerDTO{
      private String  ANS_ID, CONTENT, QUE_ID, ADMIN_ID, TIME;

        public String getANS_ID() {
            return ANS_ID;
        }

        public void setANS_ID(String ANS_ID) {
            this.ANS_ID = ANS_ID;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public String getQUE_ID() {
            return QUE_ID;
        }

        public void setQUE_ID(String QUE_ID) {
            this.QUE_ID = QUE_ID;
        }

        public String getADMIN_ID() {
            return ADMIN_ID;
        }

        public void setADMIN_ID(String ADMIN_ID) {
            this.ADMIN_ID = ADMIN_ID;
        }

        public String getTIME() {
            return TIME;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }
    }





    public class Community_NoticeDTO {//공지사항 게시판

        private String    NOTICE_ID,READ_CNT, TITLE, CONTENT, TIME, MEMBER_ID;



        public Community_NoticeDTO(String READ_CNT, String TITLE, String CONTENT, String TIME, String MEMBER_ID) {
            this.READ_CNT = READ_CNT;
            this.TITLE = TITLE;
            this.CONTENT = CONTENT;
            this.TIME = TIME;
            this.MEMBER_ID = MEMBER_ID;
        }


        public String getREAD_CNT() {
            return READ_CNT;
        }

        public void setREAD_CNT(String READ_CNT) {
            this.READ_CNT = READ_CNT;
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
    }


}
