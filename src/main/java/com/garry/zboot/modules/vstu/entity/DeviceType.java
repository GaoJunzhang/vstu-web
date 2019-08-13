package com.garry.zboot.modules.vstu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garry.zboot.common.utils.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "device_type")
public class DeviceType implements java.io.Serializable {


    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private String name;
    private String remark;
    private Short isDelete;
    private Timestamp createTime;

    public DeviceType() {
    }

    public DeviceType(String id) {
        this.id = id;
    }

    public DeviceType(String id, String name, String remark, Short isDelete, Timestamp createTime) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.isDelete = isDelete;
        this.createTime = createTime;
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

    @Column(name = "name", length = 45)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "remark", length = 45)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "is_delete")
    public Short getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "create_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
