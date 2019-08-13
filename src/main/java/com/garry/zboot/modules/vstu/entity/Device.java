package com.garry.zboot.modules.vstu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garry.zboot.common.utils.SnowFlakeUtil;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class Device implements java.io.Serializable {

    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private String deviceName;
    private String deviceMac;
    private Timestamp createTime;
    private Short enable;
    private String deviceTypeId;

    public Device() {
    }

    public Device(String id, String deviceTypeId) {
        this.id = id;
        this.deviceTypeId = deviceTypeId;
    }

    public Device(String id, String deviceName, String deviceMac, Timestamp createTime, Short enable, String deviceTypeId) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceMac = deviceMac;
        this.createTime = createTime;
        this.enable = enable;
        this.deviceTypeId = deviceTypeId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false, length = 255)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "device_name", length = 50)
    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Column(name = "device_mac", length = 32)
    public String getDeviceMac() {
        return this.deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    @Column(name = "create_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "enable")
    public Short getEnable() {
        return this.enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @Column(name = "device_type_id")
    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
}
