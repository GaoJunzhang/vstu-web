package com.garry.zboot.modules.vstu.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceBean {
    private String id;
    private String deviceName;
    private String deviceMac;
    private Timestamp createTime;
    private Short enable;
    private String deviceTypeName;
    private String deviceTypeId;
}
