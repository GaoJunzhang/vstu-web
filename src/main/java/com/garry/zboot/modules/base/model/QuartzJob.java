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
@Table(name = "t_quartz_job")
@TableName("t_quartz_job")
@ApiModel(value = "定时任务")
public class QuartzJob extends ZbootBaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "任务类名")
	private String jobClassName;

	@ApiModelProperty(value = "cron表达式")
	private String cronExpression;

	@ApiModelProperty(value = "参数")
	private String parameter;

	@ApiModelProperty(value = "备注")
	private String description;

	@ApiModelProperty(value = "状态 0正常 -1停止")
	private Integer status = CommonConstant.STATUS_NORMAL;

}
