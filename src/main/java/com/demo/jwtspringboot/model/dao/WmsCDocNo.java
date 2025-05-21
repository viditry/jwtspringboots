package com.demo.jwtspringboot.model.dao;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@TableName("wms_c_doc_no")
public class WmsCDocNo {

    @TableId(type = IdType.INPUT)
    private Long id;

    private String werks;
    private String docType;
    private String preNo;
    private String backNo;
    private Long startNo;
    private Long endNo;
    private Integer incrementNum;
    private Long noLength;
    private String currentNo;

    private String del;
    private String creator;
    private String createDate;
    private String editor;
    private String editDate;
    private String memo;

    private String ruleSource;
    private String ruleNo;
    private String ruleVersion;

    // Getters & Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(String ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    public String getRuleSource() {
        return ruleSource;
    }

    public void setRuleSource(String ruleSource) {
        this.ruleSource = ruleSource;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(String currentNo) {
        this.currentNo = currentNo;
    }

    public Long getNoLength() {
        return noLength;
    }

    public void setNoLength(Long noLength) {
        this.noLength = noLength;
    }

    public Integer getIncrementNum() {
        return incrementNum;
    }

    public void setIncrementNum(Integer incrementNum) {
        this.incrementNum = incrementNum;
    }

    public Long getEndNo() {
        return endNo;
    }

    public void setEndNo(Long endNo) {
        this.endNo = endNo;
    }

    public Long getStartNo() {
        return startNo;
    }

    public void setStartNo(Long startNo) {
        this.startNo = startNo;
    }

    public String getBackNo() {
        return backNo;
    }

    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }

    public String getPreNo() {
        return preNo;
    }

    public void setPreNo(String preNo) {
        this.preNo = preNo;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }
}