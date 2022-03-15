package com.zhsj.business.kaoqin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class KaoQinQueryDto {
    private String studentNo;
    private String kaoqinStudent;
    private Date startTime;
    private Date endTime;
    private String classCode;
    private Integer status;
}
