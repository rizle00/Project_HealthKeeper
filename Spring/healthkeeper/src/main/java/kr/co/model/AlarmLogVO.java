package kr.co.model;

import lombok.Data;

@Data
public class AlarmLogVO {
   private String ALARM_ID, TIME, STATE, MEMBER_ID, CATEGORY_ID;
   private AlarmTypeVO typeVO;


}
