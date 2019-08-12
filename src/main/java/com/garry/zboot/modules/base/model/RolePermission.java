package com.garry.zboot.modules.base.model;

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
@Table(name = "t_role_permission")
@TableName("t_role_permission")
@ApiModel(value = "角色权限")
public class RolePermission extends ZbootBaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色id")
	private String roleId;

	@ApiModelProperty(value = "权限id")
	private String permissionId;

}
