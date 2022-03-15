package com.zhsj.business.kaoqin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class KaoQinDto {
    /**
     * 学生学号
     */
    private String studentNo;
    /**
     * 考勤学生
     */
    private String kaoqinStudent;
    /**
     *考勤基本信息
     */
    private String kaoqinInfo;
    /**
     * 考勤状态
     */
    private String kaoqinStatus;
    /**
     * 班级编号
     */
    private String classCode;
    /**
     * 班级编号
     */
    private String className;
    /**
     * 考勤时间
     */
    private Date kaoqinTime;
    /**
     * 出勤率
     */
    private String attendance;
}
