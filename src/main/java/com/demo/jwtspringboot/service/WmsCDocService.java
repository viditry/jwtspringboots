package com.demo.jwtspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.jwtspringboot.model.dao.WmsCDocNo;

import java.util.Map;

public interface WmsCDocService extends IService<WmsCDocNo> {


    Map<String, Object> getDocNo(String werks,String docType);}