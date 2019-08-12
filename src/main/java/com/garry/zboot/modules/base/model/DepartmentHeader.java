package com.garry.zboot.modules.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garry.zboot.base.ZbootBaseEntity;
import com.garry.zboot.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_department_header")
@TableName("t_department_header")
@ApiModel(value = "部门负责人")
public class DepartmentHeader extends ZbootBaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "关联部门id")
	private String departmentId;

	@ApiModelProperty(value = "关联部门负责人")
	private String userId;

	@ApiModelProperty(value = "负责人类型 默认0主要 1副职")
	private Integer type = CommonConstant.HEADER_TYPE_MAIN;
}
