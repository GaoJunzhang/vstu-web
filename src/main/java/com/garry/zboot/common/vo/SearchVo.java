package com.garry.zboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
* class_name: SearchVo
* package: com.garry.zboot.modules.base.vo
* describe: TODO
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 9:47
**/
@Data
public class SearchVo implements Serializable {

    private String startDate;

    private String endDate;
}
