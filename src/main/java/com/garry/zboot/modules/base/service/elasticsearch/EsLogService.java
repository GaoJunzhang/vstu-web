package com.garry.zboot.modules.base.service.elasticsearch;

import com.garry.zboot.modules.base.model.elasticsearch.EsLog;
import com.garry.zboot.common.vo.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * EsLogService
 * @Author gaojunzhang
 * @Date 2019/7/8 23:23
 */
public interface EsLogService {

    /**
     * 添加日志
     * @param esLog
     * @return
     */

    EsLog saveLog(EsLog esLog);

    /**
     * 通过id删除日志
     * @param id
     */
    void deleteLog(String id);

    /**
     * 删除全部日志
     */
    void deleteAll();

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<EsLog> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
}
