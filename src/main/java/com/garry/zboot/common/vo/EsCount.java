package com.garry.zboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
* class_name: EsCount
* package: com.garry.zboot.common.vo
* describe: elasticsearch搜索数量
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 9:37
**/
@Data
public class EsCount implements Serializable {

    private Integer count;
}
