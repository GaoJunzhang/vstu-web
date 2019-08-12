package com.garry.zboot.config.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * class_name: UserAuditor
 * package: com.garry.zboot.config.jpa
 * describe: 审计记录创建或修改用户
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2019/7/9
 * creat_time: 10:34
 **/
@Configuration
@Slf4j
public class UserAuditor implements AuditorAware<String> {
    /**
     * Spring Data提供支持审计功能：即由谁在什么时候创建或修改实体。
     * Spring Data提供了在实体类的属性上增加@CreatedBy，@LastModifiedBy，@CreatedDate，@LastModifiedDate注解，并配置相应的配置项，即可实现审计功能，
     * 有系统自动记录 createdBy CreatedDate lastModifiedBy lastModifiedDate 四个属性的值。
     */
    @Override
    public Optional<String> getCurrentAuditor() {

        UserDetails user;
        try {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(user.getUsername());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
