package com.example.healthkeeper.firebase;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {

    private String CATEGORY_ID;
    private String name;
    private String GUARDIAN_ID;
    private String MEMBER_ID;

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGUARDIAN_ID() {
        return GUARDIAN_ID;
    }

    public void setGUARDIAN_ID(String GUARDIAN_ID) {
        this.GUARDIAN_ID = GUARDIAN_ID;
    }

    public String getMEMBER_ID() {
        return MEMBER_ID;
    }

    public void setMEMBER_ID(String MEMBER_ID) {
        this.MEMBER_ID = MEMBER_ID;
    }
}
