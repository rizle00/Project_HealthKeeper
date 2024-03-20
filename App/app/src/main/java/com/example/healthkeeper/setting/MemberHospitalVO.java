package com.example.healthkeeper.setting;

import java.util.List;

public class MemberHospitalVO {
    public String getMEMBER_ID() {
        return MEMBER_ID;
    }

    public void setMEMBER_ID(String MEMBER_ID) {
        this.MEMBER_ID = MEMBER_ID;
    }

    public String getHOSPITAL_ID() {
        return HOSPITAL_ID;
    }

    public void setHOSPITAL_ID(String HOSPITAL_ID) {
        this.HOSPITAL_ID = HOSPITAL_ID;
    }

    public String getDOCTOR_NAME() {
        return DOCTOR_NAME;
    }

    public void setDOCTOR_NAME(String DOCTOR_NAME) {
        this.DOCTOR_NAME = DOCTOR_NAME;
    }

    private String MEMBER_ID, HOSPITAL_ID, DOCTOR_NAME;

    public List<String> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<String> doctors) {
        this.doctors = doctors;
    }

    private List<String> doctors;

}
