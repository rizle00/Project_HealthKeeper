package com.example.healthkeeper.setting;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter

public class HospitalVO {
    private String HOSPITAL_ID;
    private String NAME;
    private String LOCATION;

    public String getHOSPITAL_ID() {
        return HOSPITAL_ID;
    }

    public void setHOSPITAL_ID(String HOSPITAL_ID) {
        this.HOSPITAL_ID = HOSPITAL_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getCONTACT() {
        return CONTACT;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    private String CONTACT;


}
