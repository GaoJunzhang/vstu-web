package com.garry.zboot.modules.vstu.controller;

import com.garry.zboot.common.utils.PageUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.Resource;
import com.garry.zboot.modules.vstu.service.IResourceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "资源管理接口")
@RequestMapping("/zboot/resource")
@Transactional
public class ResourceController {

    @Autowired
    private IResourceService iResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Resource> get(@PathVariable String id) {

        Resource resource = iResourceService.getById(id);
        return new ResultUtil<Resource>().setData(resource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Resource>> getAll() {

        List<Resource> list = iResourceService.list();
        return new ResultUtil<List<Resource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Resource>> getByPage(@ModelAttribute PageVo pageVo,
                                             @ModelAttribute SearchVo searchVo,
                                             @ModelAttribute Resource resource) {

        IPage<Resource> data = iResourceService.selectPage(resource, pageVo, searchVo);
        return new ResultUtil<IPage<Resource>>().setData(data);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Resource> saveOrUpdate(@ModelAttribute Resource resource) {
        if (resource.getCreateTime() == null) {
            resource.setCreateTime(new Timestamp(System.currentTimeMillis()));
            resource.setIsDelete((short) 0);
        }
        if (iResourceService.saveOrUpdate(resource)) {
            return new ResultUtil<Resource>().setData(resource);
        }
        return new ResultUtil<Resource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
