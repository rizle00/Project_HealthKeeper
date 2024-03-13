package com.example.healthkeeper.main.community;

import java.io.Serializable;


public class CommunityDTOS {

  public class Community_BoardDTO{
        private int  member_age, member_number;
        private String writer, title,msg, time, comments;


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
      private String question,answer;

        public Community_QuestionDTO(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
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
