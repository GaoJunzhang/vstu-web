package com.garry.zboot.modules.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.garry.zboot.base.ZbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user_role")
@TableName("t_user_role")
@ApiModel(value = "用户角色")
public class UserRole extends ZbootBaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户唯一id")
	private String userId;

	@ApiModelProperty(value = "角色唯一id")
	private String roleId;

	@Transient
	@TableField(exist=false)
	@ApiModelProperty(value = "角色名")
	private String roleName;

}
