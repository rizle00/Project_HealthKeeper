package com.example.testapplication.firebase;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqDTO {

    private String CATEGORY_ID;
    private String name;
    private String guardian_id;
    private String member_id;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCategory_id() {
        return CATEGORY_ID;
    }

    public void setCategory_id(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuardian_id() {
        return guardian_id;
    }

    public void setGuardian_id(String guardian_id) {
        this.guardian_id = guardian_id;
    }
}
