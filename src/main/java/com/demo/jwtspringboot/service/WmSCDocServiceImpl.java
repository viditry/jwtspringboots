package com.demo.jwtspringboot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.jwtspringboot.model.dao.WmsCDocNo;
import com.demo.jwtspringboot.repository.dao.WmsCDocDao;
import com.demo.jwtspringboot.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
;
@Service
public class WmSCDocServiceImpl extends ServiceImpl<WmsCDocDao, WmsCDocNo> implements WmsCDocService {
    private static final Logger logger = LoggerFactory.getLogger(WmSCDocServiceImpl.class);

    @Autowired
    private WmsCDocDao wmsCDocDao;

    @Autowired
    RedisUtils redisUtils;




    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor=Exception.class)
    public synchronized Map<String, Object> getDocNos(Map<String, Object> params) {
        logger.info("getDocNo:{}", JSONObject.toJSONString(params));
        // 根据 werks doctype查询出配置信息
        // if 当前编号==0 那么 当前编号=起始号
        // 编号=系统代码+编号前缀 +当前编号+编号后缀
        // else
        // 编号=系统代码+编号前缀 +当前编号+编号后缀
        // 当前编号=当前编号+递增数量
        /**
         * 编号从Redis中取，当Redis中docNo数量少于10，就查询出配置信息，补充Redis中docNo列表，
         * 数量默认100，docType为08和14类型的每次补充数量为500
         * 并更新WMS_C_DOC_NO表中CURRENT_NO的值
         */
        Map<String, Object> retmap=new HashMap<String, Object>();
        String docno="";
        String msg="";

        String werks = params.get("WERKS")==null?"":params.get("WERKS").toString();
        String docType = params.get("WMS_DOC_TYPE").toString();

        String uid = UUID.randomUUID().toString();
        String lockDocType = "";
        String redisKey = "";

        boolean lockflag = false;
        try {
            int no_length =0;//配置的doc_no长度
            String preNo = ""; //前缀
            String backNo = ""; //后缀
            Map<String, Object> param=new HashMap<String, Object> ();
            param.put("werks", werks);
            param.put("doc_type", docType);
            param.put("del", "0");
            List<WmsCDocNo> docNolist=wmsCDocDao.selectByMap(param);
            if(docNolist==null || docNolist.size()<=0) {
                lockDocType = "lockDocType_"+docType;
                redisKey = "wms:docType_"+docType;
            } else {
                lockDocType = "lockDocType_"+werks+docType;
                redisKey = "wms:docType_"+werks+docType;
                no_length = Integer.parseInt(String.valueOf(docNolist.get(0).getNoLength()));
                preNo = docNolist.get(0).getPreNo() == null?"":docNolist.get(0).getPreNo();
                backNo = docNolist.get(0).getBackNo() == null?"":docNolist.get(0).getBackNo();
            }

            //分布式加锁
            if (redisUtils.tryLock(lockDocType, uid)) {
                lockflag = true;
//				Map<String, Object> param=new HashMap<String, Object> ();
                String sys_str= "";//系统代码

                Map<String,Object> rs_map = redisUtils.get(redisKey,Map.class);
                int doc_no_count = 0; //redis中doc_no剩余数量
                int add_count = 0;	//补全数量
                int incrementNum = 1; //步长
                int docno_current=0;//当前编码


                if(rs_map!=null){
                    doc_no_count =  (int) rs_map.get("doc_no_count");
                    docno_current = (int) rs_map.get("docno_current");
                    incrementNum = (int) rs_map.get("incrementNum");
                    no_length = no_length==0?(int) rs_map.get("no_length"):no_length;
                    preNo = preNo.equals("")?rs_map.get("preNo").toString():preNo;
                    backNo = backNo.equals("")?rs_map.get("backNo").toString():backNo;
                    rs_map.put("no_length", no_length);
                    rs_map.put("preNo", preNo);
                    rs_map.put("backNo", backNo);

                }else{//redis 中未找到docNo信息，查询数据库表中获取当前docNo信息，并保存到redis中
                    if("08".equals(docType) || "14".equals(docType)){
                        add_count = 10000;
                    }else
                        add_count = 100;

//					param.put("werks", werks);
//					param.put("doc_type", docType);
//					param.put("del", "0");
//					List<WmsCDocNo> docNolist=this.selectByMap(param);
                    if(docNolist==null || docNolist.size()<=0) {
                        param.remove("werks");
                        docNolist=wmsCDocDao.selectByMap(param);
                    }

                    if(docNolist!=null&&docNolist.size()>0){
                        incrementNum = docNolist.get(0).getIncrementNum();
                        docno_current = Integer.parseInt(docNolist.get(0).getCurrentNo());
                        no_length = Integer.parseInt(String.valueOf(docNolist.get(0).getNoLength()));
                        rs_map = new HashMap<String,Object>();
                        preNo = docNolist.get(0).getPreNo() == null?"":docNolist.get(0).getPreNo();
                        backNo = docNolist.get(0).getBackNo() == null?"":docNolist.get(0).getBackNo();
                        rs_map.put("doc_no_count", add_count);
                        rs_map.put("docno_current", docno_current+incrementNum);
                        rs_map.put("incrementNum", incrementNum);
                        rs_map.put("no_length", no_length);
                        rs_map.put("preNo", preNo);
                        rs_map.put("backNo", backNo);

                        doc_no_count = add_count;

                        param.put("add_count", docno_current+add_count);
                        wmsCDocDao.updateDocNoById(param);
                    } else {
                        //#17 单据编号规则未配置，无法产生单据编号
                        retmap.put("MSG", "OutErrorCodeEnum.OUT10000141.msg()");
                        return retmap;
                    }


                }
                if(doc_no_count<=10){//不足时，补全redis中doc_no_count数量，更新数据库中当前docNo
                    if("08".equals(docType) || "14".equals(docType)){
                        add_count = 10000-doc_no_count;
                    }else
                        add_count = 100-doc_no_count;

                    param.put("werks", werks);
                    param.put("doc_type", docType);
                    param.put("add_count", docno_current+add_count+doc_no_count);
                    wmsCDocDao.updateDocNoById(param);

                    rs_map.put("doc_no_count", add_count+doc_no_count-1); //剩余数量补充后，还得扣掉本次取掉的一个号

                }else{
                    rs_map.put("doc_no_count", doc_no_count-1);
                }

                String docno_current_str=String.valueOf(docno_current);

                String add0="";
                if(docno_current_str.length()<no_length){//当前长度小于设置的长度
                    for(int i=0;i<no_length-docno_current_str.length();i++){
                        add0="0".concat(add0);
                    }
                } else if (docno_current_str.length() > no_length) {//当前长度大于设置的长度
                    //#18 当前编码的长度大于设置的长度!请重新设置长度
                    msg = "OutErrorCodeEnum.OUT10000142.msg()";
                    retmap.put("MSG", msg);
                    return retmap;
                }
                docno_current_str=add0.concat(docno_current_str);
                if("".equals(msg)){//没有报错信息
                    docno=sys_str+(preNo+werks+docno_current_str+backNo);
                }

                retmap.put("docno", docno);
                retmap.put("MSG", "success");

                rs_map.put("docno_current", docno_current+incrementNum);
                redisUtils.set(redisKey, rs_map,RedisUtils.NOT_EXPIRE);
                logger.info("getDocNo result : {}", JSON.toJSONString(rs_map));
                logger.info("WmsCDocNoServiceImpl ==> getDocNo ==> retmap:{}", JSON.toJSONString(retmap));
                return retmap;
            } else {
                //#19 上一个号码正在生成中，请稍后再试
                //上一个号码正在生成中，请稍后再试
                retmap.put("MSG", lockDocType + "," + "OutErrorCodeEnum.OUT10000143.msg()");
                logger.info("WmsCDocNoServiceImpl ==> getDocNo ==> retmap:{}", JSON.toJSONString(retmap));
                return retmap;
            }

        } catch(Exception e) {
            //#20
            logger.error("WmsCDocNoServiceImpl ==> getDocNo ==> Exception ==>{}", e.getMessage(), e);
            //retmap.put("MSG", e.getMessage());
            retmap.put("MSG", "LocaleLanguageFactory.getValue()");
            return retmap;
        } finally {
            if (lockflag)
                redisUtils.unlock(lockDocType, uid);
        }
    }

    @Override
    public  Map<String, Object> getDocNo(String werks, String docType) {
        Map<String, Object> param=new HashMap<String, Object> ();
        param.put("werks", werks);
        param.put("doc_type", docType);
        Map<String, Object> map = getDocNos(param);


        return map;
    }
}
