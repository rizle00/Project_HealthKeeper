package kr.co.app.community;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class QueVO {
   private String QUE_ID, TITLE, CONTENT, TIME, MEMBER_ID, SECRET, CATEGORY_ID, READ_CNT;
   private AnswerVO vo;
public String getQUE_ID() {
	return QUE_ID;
}

public void setQUE_ID(String qUE_ID) {
	QUE_ID = qUE_ID;
}

public String getTITLE() {
	return TITLE;
}

public void setTITLE(String tITLE) {
	TITLE = tITLE;
}

public String getCONTENT() {
	return CONTENT;
}

public void setCONTENT(String cONTENT) {
	CONTENT = cONTENT;
}

public String getTIME() {
	return TIME;
}

public void setTIME(String tIME) {
	TIME = tIME;
}

public String getMEMBER_ID() {
	return MEMBER_ID;
}

public void setMEMBER_ID(String mEMBER_ID) {
	MEMBER_ID = mEMBER_ID;
}

public String getSECRET() {
	return SECRET;
}

public void setSECRET(String sECRET) {
	SECRET = sECRET;
}

public String getCATEGORY_ID() {
	return CATEGORY_ID;
}

public void setCATEGORY_ID(String cATEGORY_ID) {
	CATEGORY_ID = cATEGORY_ID;
}

public String getREAD_CNT() {
	return READ_CNT;
}

public void setREAD_CNT(String rEAD_CNT) {
	READ_CNT = rEAD_CNT;
}

   
}
