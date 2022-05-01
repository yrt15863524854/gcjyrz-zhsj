package com.zhsj.business.excel.write;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @className: DemoData
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 10:48
 * version 1.0
 **/
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {


    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;

   public DemoData(){

   }}
