package com.garry.zboot.modules.vstu.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceBean extends MainBean{
    private String id;
    private String deviceName;
    private String deviceMac;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    private Short enable;
    private String deviceTypeName;
    private String deviceTypeId;
}
