package com.garry.zboot.modules.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garry.zboot.base.ZbootBaseEntity;
import com.garry.zboot.common.utils.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_log")
@TableName("t_log")
@ApiModel(value = "日志")
public class Log extends ZbootBaseEntity {


	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "方法操作名称")
	private String name;

	@ApiModelProperty(value = "日志类型 0登陆日志 1操作日志")
	private Integer logType;

	@ApiModelProperty(value = "请求路径")
	private String requestUrl;

	@ApiModelProperty(value = "请求类型")
	private String requestType;

	@ApiModelProperty(value = "请求参数")
	private String requestParam;

	@ApiModelProperty(value = "请求用户")
	private String username;

	@ApiModelProperty(value = "ip")
	private String ip;

	@ApiModelProperty(value = "ip信息")
	private String ipInfo;

	@ApiModelProperty(value = "花费时间")
	private Integer costTime;

	/**
	 * 转换请求参数为Json
	 * @param paramMap
	 */
	public void setMapToParams(Map<String, String[]> paramMap) {

		this.requestParam = ObjectUtil.mapToString(paramMap);
	}
}
