package com.garry.zboot.modules.vstu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garry.zboot.common.utils.SnowFlakeUtil;
import com.garry.zboot.modules.base.model.User;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

@Entity
@Table(name = "user_resource")
public class UserResource implements java.io.Serializable {

    private static final long serialVersionUID = 6560227933446043745L;
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private Timestamp startTime;
    private Timestamp endTime;
    private String userId;
    private String resourceId;
    private Timestamp createTime;
    private Short isForever;

    public UserResource() {
    }

    public UserResource(String id, String userId, String resourceId) {
        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
    }

    public UserResource(String id, Timestamp startTime, Timestamp endTime, String userId, String resourceId, Timestamp createTime, Short isForever) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.resourceId = resourceId;
        this.createTime = createTime;
        this.isForever = isForever;
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

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time", length = 19)
    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time", length = 19)
    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "t_user_id", length = 19)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "resource_id", length = 19)
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", length = 19)
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "is_forever")
    public Short getIsForever() {
        return this.isForever;
    }

    public void setIsForever(Short isForever) {
        this.isForever = isForever;
    }

}
