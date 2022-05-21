package com.zhsj.business.point.dto;

import lombok.Data;

import java.util.List;

/**
 * @className: ExcelQueryDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 16:39
 * version 1.0
 **/
@Data
public class ExcelQueryDto {
    private String classCode;
    private String studentNo;
    private String teacherCode;
    private List<String> classCodeList;
    private String xueqi;
    private String name;
    private String grad;
    private String pers;
}
