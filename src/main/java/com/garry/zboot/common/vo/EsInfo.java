package com.garry.zboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
* class_name: EsInfo
* package: com.garry.zboot.common.vo
* describe: Elasticsearch信息接口对象
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 9:34
**/
@Data
public class EsInfo implements Serializable{

    private String cluster_name;

    private String status;

    private String number_of_nodes;

    private Integer count;
}
