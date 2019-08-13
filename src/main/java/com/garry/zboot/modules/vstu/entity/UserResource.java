package com.garry.zboot.modules.vstu.entity;

import com.garry.zboot.modules.base.model.User;

import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Entity
@Table(name = "user_resource")
public class UserResource  implements java.io.Serializable {

	private String id;
	private Timestamp startTime;
	private Timestamp endTime;
	private User user;
	private Resource resource;
	private Timestamp createTime;
	private Short isForever;

	public UserResource() {
	}

	public UserResource(String id, User user, Resource resource) {
		this.id = id;
		this.user = user;
		this.resource = resource;
	}

	public UserResource(String id, Timestamp startTime, Timestamp endTime, User user, Resource resource, Timestamp createTime, Short isForever) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.user = user;
		this.resource = resource;
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

	@Column(name = "start_time", length = 19)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time", length = 19)
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_id", nullable = false)
	public Resource getResource() {
		return this.resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

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
