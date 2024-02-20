package com.example.healthkeeper.member;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuardianMemberVO {
    private String guardian_id, guardian_pw, guardian_email, guardian_phone, social, alram, patient_id;

}
