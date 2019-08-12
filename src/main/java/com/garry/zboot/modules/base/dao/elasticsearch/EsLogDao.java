package com.garry.zboot.modules.base.dao.elasticsearch;

import com.garry.zboot.modules.base.model.elasticsearch.EsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsLogDao extends ElasticsearchRepository<EsLog,String> {
    Page<EsLog> findByLogType(Integer type, Pageable pageable);
}
