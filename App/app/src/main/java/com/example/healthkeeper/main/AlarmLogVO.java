package com.example.healthkeeper.main;

public class AlarmLogVO {
   private String ALARM_ID, TIME, STATE, MEMBER_ID, CATEGORY_ID;
   private AlarmTypeVO typeVO;

    public String getALARM_ID() {
        return ALARM_ID;
    }

    public void setALARM_ID(String ALARM_ID) {
        this.ALARM_ID = ALARM_ID;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
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

    public AlarmTypeVO getTypeVO() {
        return typeVO;
    }

    public void setTypeVO(AlarmTypeVO typeVO) {
        this.typeVO = typeVO;
    }
}
