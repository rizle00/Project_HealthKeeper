package com.example.healthkeeper.setting;

import lombok.Getter;
import lombok.Setter;


public class DiseaseVO {
   private String MEMBER_ID, DISEASE_NAME, DESCRIPTION;

   public String getDISEASE_NAME() {
      return DISEASE_NAME;
   }

   public void setDISEASE_NAME(String DISEASE_NAME) {
      this.DISEASE_NAME = DISEASE_NAME;
   }

   public String getMEMBER_ID() {
      return MEMBER_ID;
   }

   public void setMEMBER_ID(String MEMBER_ID) {
      this.MEMBER_ID = MEMBER_ID;
   }

   public String getDESCRIPTION() {
      return DESCRIPTION;
   }

   public void setDESCRIPTION(String DESCRIPTION) {
      this.DESCRIPTION = DESCRIPTION;
   }
}
