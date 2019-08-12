package com.garry.zboot.modules.blog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.garry.zboot.common.utils.SnowFlakeUtil;
import com.sun.jmx.snmp.Timestamp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "test", type = "article", shards = 1, replicas = 0, refreshInterval = "-1")
public class Article implements Serializable {
    private static final long serialVersionUID = 1625003584234738356L;
    @Id
    @ApiModelProperty(value = "唯一标识")
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    @ApiModelProperty(value = "唯一标识")
    private String title;
    /*@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date,format = DateFormat.date_hour_minute_second_fraction)*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date, index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();
    @ApiModelProperty(value = "文章楔子")
    private String wedge;
    @ApiModelProperty(value = "图片集")
    private String imgs;
    @ApiModelProperty(value = "图片描述")
    private String imgDesc;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "点赞数量")
    private Integer like;//点赞量
    @ApiModelProperty(value = "阅读数量")
    private Integer page;//阅读量
    @ApiModelProperty(value = "转发量")
    private Integer forward;//转发量
    @ApiModelProperty(value = "创建人id")
    private String uid;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    @ApiModelProperty(value = "所属栏目1234")
    private Integer category;//栏目
    @ApiModelProperty(value = "作者名")
    private String author;///
    @ApiModelProperty(value = "农历显示")
    private String lunarCalendar;

}
