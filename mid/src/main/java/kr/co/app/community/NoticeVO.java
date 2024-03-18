package kr.co.app.community;

public class NoticeVO {
   private String NOTICE_ID, READ_CNT, TITLE, CONTENT, TIME, MEMBER_ID;

    public String getNOTICE_ID() {
        return NOTICE_ID;
    }

    public void setNOTICE_ID(String NOTICE_ID) {
        this.NOTICE_ID = NOTICE_ID;
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
