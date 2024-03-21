package com.example.healthkeeper.bluetooth;

import lombok.Data;

public class ConditionVO {
 public String getCONDITION_ID() {
  return CONDITION_ID;
 }

 public void setCONDITION_ID(String CONDITION_ID) {
  this.CONDITION_ID = CONDITION_ID;
 }

 public String getCONDITION_PULSE() {
  return CONDITION_PULSE;
 }

 public void setCONDITION_PULSE(String CONDITION_PULSE) {
  this.CONDITION_PULSE = CONDITION_PULSE;
 }

 public String getCONDITION_TEMPERATURE() {
  return CONDITION_TEMPERATURE;
 }

 public void setCONDITION_TEMPERATURE(String CONDITION_TEMPERATURE) {
  this.CONDITION_TEMPERATURE = CONDITION_TEMPERATURE;
 }

 public String getCONDITION_TIME() {
  return CONDITION_TIME;
 }

 public void setCONDITION_TIME(String CONDITION_TIME) {
  this.CONDITION_TIME = CONDITION_TIME;
 }

 public String getMEMBER_ID() {
  return MEMBER_ID;
 }

 public void setMEMBER_ID(String MEMBER_ID) {
  this.MEMBER_ID = MEMBER_ID;
 }

 public String getCONDITION_ACCIDENT() {
  return CONDITION_ACCIDENT;
 }

 public void setCONDITION_ACCIDENT(String CONDITION_ACCIDENT) {
  this.CONDITION_ACCIDENT = CONDITION_ACCIDENT;
 }

 private  String CONDITION_ID, CONDITION_PULSE, CONDITION_TEMPERATURE, CONDITION_TIME, MEMBER_ID, CONDITION_ACCIDENT;
}
