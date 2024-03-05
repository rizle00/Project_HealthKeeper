package com.example.healthkeeper.setting;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeVO {
    private int notice_num,readcnt;
    private String title, content,admin_id;
    private Date time;

}
