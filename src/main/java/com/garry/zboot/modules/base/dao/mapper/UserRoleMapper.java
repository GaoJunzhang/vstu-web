package com.garry.zboot.modules.base.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garry.zboot.modules.base.model.Role;
import com.garry.zboot.modules.base.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Exrickx
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Role> findByUserId(@Param("userId") String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(@Param("userId") String userId);
}
