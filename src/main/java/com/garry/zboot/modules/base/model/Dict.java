package com.garry.zboot.modules.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garry.zboot.base.ZbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_dict")
@TableName("t_dict")
@ApiModel(value = "字典")
public class Dict extends ZbootBaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "字典名称")
	private String title;

	@ApiModelProperty(value = "字典类型")
	private String type;

	@ApiModelProperty(value = "备注")
	private String description;

	@ApiModelProperty(value = "排序值")
	@Column(precision = 10, scale = 2)
	private BigDecimal sortOrder;
}
