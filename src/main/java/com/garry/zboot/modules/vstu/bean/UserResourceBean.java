package com.garry.zboot.modules.vstu.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResourceBean {

    private String id;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;
    private String userId;
    private String resourceId;
    private Timestamp createTime;
    private Short isForever;

    private String name;
    private String url;
    private String proImg;
    private String proVideo;
    private String title;
    private String content;
    private Short isDelete;
}
