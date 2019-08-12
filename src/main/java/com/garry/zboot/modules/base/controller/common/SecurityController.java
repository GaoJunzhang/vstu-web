package com.garry.zboot.modules.base.controller.common;

import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* class_name: SecurityController
* package: com.garry.zboot.modules.base.controller.common
* describe: Security相关接口
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 10:07
**/
@Slf4j
@RestController
@Api(description = "Security相关接口")
@RequestMapping("/zboot/common")
@Transactional
public class SecurityController {
    @RequestMapping(value = "/needLogin",method = RequestMethod.GET)
    @ApiOperation(value = "没有登录")
    public Result<Object> needLogin(){

        return new ResultUtil<Object>().setErrorMsg(401, "您还未登录");
    }
}
