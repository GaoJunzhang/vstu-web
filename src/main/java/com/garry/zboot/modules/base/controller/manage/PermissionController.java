package com.garry.zboot.modules.base.controller.manage;

import cn.hutool.core.util.StrUtil;
import com.garry.zboot.common.constant.CommonConstant;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.utils.SecurityUtil;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.config.security.permission.MySecurityMetadataSource;
import com.garry.zboot.modules.base.model.Permission;
import com.garry.zboot.modules.base.model.RolePermission;
import com.garry.zboot.modules.base.model.User;
import com.garry.zboot.modules.base.service.PermissionService;
import com.garry.zboot.modules.base.service.RolePermissionService;
import com.garry.zboot.modules.base.service.mybatis.IPermissionService;
import com.garry.zboot.modules.base.utils.DtoUtil;
import com.garry.zboot.modules.base.vo.MenuVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@Api(description = "菜单/权限管理接口")
@RequestMapping("/zboot/permission")
@CacheConfig(cacheNames = "permission")
@Transactional
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private IPermissionService iPermissionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;


    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户页面菜单数据")
    public Result<List<MenuVo>> getAllMenuList(){

        List<MenuVo> menuList = new ArrayList<>();
        // 读取缓存
        User u = securityUtil.getCurrUser();
        String key = "permission::userMenuList:" + u.getId();
        String v = redisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(v)){
            menuList = new Gson().fromJson(v, new TypeToken<List<Permission>>(){}.getType());
            return new ResultUtil<List<MenuVo>>().setData(menuList);
        }

        // 用户所有权限 已排序去重
        List<Permission> list = iPermissionService.findByUserId(u.getId());

        // 筛选0级页面
        for(Permission p : list){
            if(CommonConstant.PERMISSION_NAV.equals(p.getType())&&CommonConstant.LEVEL_ZERO.equals(p.getLevel())){
                menuList.add(DtoUtil.permissionToMenuVo(p));
            }
        }
        // 筛选一级页面
        List<MenuVo> firstMenuList = new ArrayList<>();
        for(Permission p : list){
            if(CommonConstant.PERMISSION_PAGE.equals(p.getType())&&CommonConstant.LEVEL_ONE.equals(p.getLevel())){
                firstMenuList.add(DtoUtil.permissionToMenuVo(p));
            }
        }
        // 筛选二级页面
        List<MenuVo> secondMenuList = new ArrayList<>();
        for(Permission p : list){
            if(CommonConstant.PERMISSION_PAGE.equals(p.getType())&&CommonConstant.LEVEL_TWO.equals(p.getLevel())){
                secondMenuList.add(DtoUtil.permissionToMenuVo(p));
            }
        }
        // 筛选二级页面拥有的按钮权限
        List<MenuVo> buttonPermissions = new ArrayList<>();
        for(Permission p : list){
            if(CommonConstant.PERMISSION_OPERATION.equals(p.getType())&&CommonConstant.LEVEL_THREE.equals(p.getLevel())){
                buttonPermissions.add(DtoUtil.permissionToMenuVo(p));
            }
        }

        // 匹配二级页面拥有权限
        for(MenuVo m : secondMenuList){
            List<String> permTypes = new ArrayList<>();
            for(MenuVo me : buttonPermissions){
                if(m.getId().equals(me.getParentId())){
                    permTypes.add(me.getButtonType());
                }
            }
            m.setPermTypes(permTypes);
        }
        // 匹配一级页面拥有二级页面
        for(MenuVo m : firstMenuList){
            List<MenuVo> secondMenu = new ArrayList<>();
            for(MenuVo me : secondMenuList){
                if(m.getId().equals(me.getParentId())){
                    secondMenu.add(me);
                }
            }
            m.setChildren(secondMenu);
        }
        // 匹配0级页面拥有一级页面
        for(MenuVo m : menuList){
            List<MenuVo> firstMenu = new ArrayList<>();
            for(MenuVo me : firstMenuList){
                if(m.getId().equals(me.getParentId())){
                    firstMenu.add(me);
                }
            }
            m.setChildren(firstMenu);
        }

        // 缓存
        redisTemplate.opsForValue().set(key, new Gson().toJson(menuList));
        return new ResultUtil<List<MenuVo>>().setData(menuList);
    }

    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限菜单树")
    @Cacheable(key = "'allList'")
    public Result<List<Permission>> getAllList(){

        // 0级
        List<Permission> list0 = permissionService.findByLevelOrderBySortOrder(CommonConstant.LEVEL_ZERO);
        for(Permission p0 : list0){
            // 一级
            List<Permission> list1 = permissionService.findByParentIdOrderBySortOrder(p0.getId());
            p0.setChildren(list1);
            // 二级
            for(Permission p1 : list1){
                List<Permission> children1 = permissionService.findByParentIdOrderBySortOrder(p1.getId());
                p1.setChildren(children1);
                // 三级
                for(Permission p2 : children1){
                    List<Permission> children2 = permissionService.findByParentIdOrderBySortOrder(p2.getId());
                    p2.setChildren(children2);
                }
            }
        }
        return new ResultUtil<List<Permission>>().setData(list0);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    @CacheEvict(key = "'menuList'")
    public Result<Permission> add(@ModelAttribute Permission permission){

        // 判断拦截请求的操作权限按钮名是否已存在
        if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())){
            List<Permission> list = permissionService.findByTitle(permission.getTitle());
            if(list!=null&&list.size()>0){
                return new ResultUtil<Permission>().setErrorMsg("名称已存在");
            }
        }
        Permission u = permissionService.save(permission);
        //重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        //手动删除缓存
        redisTemplate.delete("permission::allList");
        return new ResultUtil<Permission>().setData(u);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Permission> edit(@ModelAttribute Permission permission){

        // 判断拦截请求的操作权限按钮名是否已存在
        if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())){
            // 若名称修改
            Permission p = permissionService.get(permission.getId());
            if(!p.getTitle().equals(permission.getTitle())){
                List<Permission> list = permissionService.findByTitle(permission.getTitle());
                if(list!=null&&list.size()>0){
                    return new ResultUtil<Permission>().setErrorMsg("名称已存在");
                }
            }
        }
        Permission u = permissionService.update(permission);
        //重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        //手动批量删除缓存
        Set<String> keys = redisTemplate.keys("userPermission:" + "*");
        redisTemplate.delete(keys);
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
        redisTemplate.delete(keysUserMenu);
        redisTemplate.delete("permission::allList");
        return new ResultUtil<Permission>().setData(u);
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    @CacheEvict(key = "'menuList'")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id:ids){
            List<RolePermission> list = rolePermissionService.findByPermissionId(id);
            if(list!=null&&list.size()>0){
                return new ResultUtil<Object>().setErrorMsg("删除失败，包含正被角色使用关联的菜单或权限");
            }
        }
        for(String id:ids){
            permissionService.delete(id);
        }
        //重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        //手动删除缓存
        redisTemplate.delete("permission::allList");
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索菜单")
    public Result<List<Permission>> searchPermissionList(@RequestParam String title){

        List<Permission> list = permissionService.findByTitleLikeOrderBySortOrder("%"+title+"%");
        return new ResultUtil<List<Permission>>().setData(list);
    }
}
