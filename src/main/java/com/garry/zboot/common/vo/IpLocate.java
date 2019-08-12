package com.garry.zboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * class_name: IpLocate
 * package: com.garry.zboot.common.vo
 * describe: IpLocate
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2019/7/9
 * creat_time: 10:05
 **/
@Data
public class IpLocate implements Serializable {

    private String retCode;

    private City result;
}

