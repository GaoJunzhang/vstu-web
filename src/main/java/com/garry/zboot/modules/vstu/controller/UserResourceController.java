package com.garry.zboot.modules.vstu.controller;

import com.garry.zboot.common.utils.PageUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.modules.vstu.entity.UserResource;
import com.garry.zboot.modules.vstu.service.IUserResourceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "设备资源管理接口")
@RequestMapping("/zboot/userResource")
@Transactional
public class UserResourceController {

    @Autowired
    private IUserResourceService iUserResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserResource> get(@PathVariable String id){

        UserResource userResource = iUserResourceService.getById(id);
        return new ResultUtil<UserResource>().setData(userResource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserResource>> getAll(){

        List<UserResource> list = iUserResourceService.list();
        return new ResultUtil<List<UserResource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<UserResource>> getByPage(@ModelAttribute PageVo page){

        IPage<UserResource> data = iUserResourceService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<UserResource>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserResource> saveOrUpdate(@ModelAttribute UserResource userResource){

        if(iUserResourceService.saveOrUpdate(userResource)){
            return new ResultUtil<UserResource>().setData(userResource);
        }
        return new ResultUtil<UserResource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iUserResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
