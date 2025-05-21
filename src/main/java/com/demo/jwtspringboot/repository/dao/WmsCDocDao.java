package com.demo.jwtspringboot.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.jwtspringboot.model.dao.WmsCDocNo;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface WmsCDocDao extends BaseMapper<WmsCDocNo> {
    void updateDocNoById(Map<String, Object> param);
}