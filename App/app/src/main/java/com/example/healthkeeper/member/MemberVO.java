package com.example.healthkeeper.member;


import lombok.Data;


public class MemberVO {
     public String getMEMBER_ID() {
          return MEMBER_ID;
     }

     public void setMEMBER_ID(String MEMBER_ID) {
          this.MEMBER_ID = MEMBER_ID;
     }

     public String getGUARDIAN_ID() {
          return GUARDIAN_ID;
     }

     public void setGUARDIAN_ID(String GUARDIAN_ID) {
          this.GUARDIAN_ID = GUARDIAN_ID;
     }

     public String getPW() {
          return PW;
     }

     public void setPW(String PW) {
          this.PW = PW;
     }

     public String getNAME() {
          return NAME;
     }

     public void setNAME(String NAME) {
          this.NAME = NAME;
     }

     public String getPHONE() {
          return PHONE;
     }

     public void setPHONE(String PHONE) {
          this.PHONE = PHONE;
     }

     public String getBIRTH() {
          return BIRTH;
     }

     public void setBIRTH(String BIRTH) {
          this.BIRTH = BIRTH;
     }

     public String getGENDER() {
          return GENDER;
     }

     public void setGENDER(String GENDER) {
          this.GENDER = GENDER;
     }

     public String getBLOOD() {
          return BLOOD;
     }

     public void setBLOOD(String BLOOD) {
          this.BLOOD = BLOOD;
     }

     public String getSOCIAL() {
          return SOCIAL;
     }

     public void setSOCIAL(String SOCIAL) {
          this.SOCIAL = SOCIAL;
     }

     public String getEMAIL() {
          return EMAIL;
     }

     public void setEMAIL(String EMAIL) {
          this.EMAIL = EMAIL;
     }

     public String getADDRESS() {
          return ADDRESS;
     }

     public void setADDRESS(String ADDRESS) {
          this.ADDRESS = ADDRESS;
     }

     public String getADDRESS_DETAIL() {
          return ADDRESS_DETAIL;
     }

     public void setADDRESS_DETAIL(String ADDRESS_DETAIL) {
          this.ADDRESS_DETAIL = ADDRESS_DETAIL;
     }

     public String getALARM() {
          return ALARM;
     }

     public void setALARM(String ALARM) {
          this.ALARM = ALARM;
     }

     public String getROLE() {
          return ROLE;
     }

     public void setROLE(String ROLE) {
          this.ROLE = ROLE;
     }

     public String getToken() {
          return token;
     }

     public void setToken(String token) {
          this.token = token;
     }

     private String MEMBER_ID;

     // 보호자 아이디
     private String GUARDIAN_ID;

     // 회원 비밀번호
     private String PW;

     // 회원 이름
     private String NAME;

     // 회원 전화번호
     private String PHONE;

     // 회원 생년월일
     private String BIRTH;

     // 회원 성별
     private String GENDER;

     // 회원 혈액형
     private String BLOOD;

     // 회원 소셜로그인
     private String SOCIAL;

     // 회원 이메일
     private String EMAIL;

     // 회원 기본주소
     private String ADDRESS;

     // 회원 상세주소
     private String ADDRESS_DETAIL;

     // 회원 알람여부
     private String ALARM;

     // 회원 등급 ( 일반 , 관리자 )
     private String ROLE;

     private String token;
}
