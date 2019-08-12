package com.garry.zboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * class_name: City
 * package: com.garry.zboot.common.vo
 * describe: 城市对象
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2019/7/9
 * creat_time: 10:06
 **/
@Data
public class City implements Serializable {

    String country;

    String province;

    String city;
}
