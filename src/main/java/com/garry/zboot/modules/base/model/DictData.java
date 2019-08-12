package com.garry.zboot.modules.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garry.zboot.base.ZbootBaseEntity;
import com.garry.zboot.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_dict_data")
@TableName("t_dict_data")
@ApiModel(value = "字典数据")
public class DictData extends ZbootBaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据名称")
	private String title;

	@ApiModelProperty(value = "数据值")
	private String value;

	@ApiModelProperty(value = "排序值")
	@Column(precision = 10, scale = 2)
	private BigDecimal sortOrder;

	@ApiModelProperty(value = "是否启用 0启用 -1禁用")
	private Integer status = CommonConstant.STATUS_NORMAL;

	@ApiModelProperty(value = "备注")
	private String description;

	@ApiModelProperty(value = "所属字典")
	private String dictId;
}
