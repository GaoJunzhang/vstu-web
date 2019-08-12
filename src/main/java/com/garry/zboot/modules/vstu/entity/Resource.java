package com.garry.zboot.modules.vstu.entity;

import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Entity
@Table(name = "resource")
public class Resource  implements java.io.Serializable {

	private String id;
	private String name;
	private String url;
	private String proImg;
	private String proVideo;
	private String title;
	private String content;
	private Short isDelete;
	private Timestamp createTime;

	public Resource() {
	}

	public Resource(String id) {
		this.id = id;
	}

	public Resource(String id, String name, String url, String proImg, String proVideo, String title, String content, Short isDelete, Timestamp createTime) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.proImg = proImg;
		this.proVideo = proVideo;
		this.title = title;
		this.content = content;
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

	@Column(name = "url", length = 255)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "pro_img", length = 255)
	public String getProImg() {
		return this.proImg;
	}

	public void setProImg(String proImg) {
		this.proImg = proImg;
	}

	@Column(name = "pro_video", length = 255)
	public String getProVideo() {
		return this.proVideo;
	}

	public void setProVideo(String proVideo) {
		this.proVideo = proVideo;
	}

	@Column(name = "title", length = 255)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content", length = 255)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "is_delete")
	public Short getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "create_time", length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
