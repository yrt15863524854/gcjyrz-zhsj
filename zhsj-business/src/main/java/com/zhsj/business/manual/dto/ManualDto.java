package com.zhsj.business.manual.dto;

import lombok.Data;

@Data
public class ManualDto {
    /**
     * 设计书id
     **/
    private Long id;
    /**
     * 设计书编码
     */
    private String manualCode;
    /**
     * 设计书题目
     */
    private String manualName;
    /**
     * 课程编码
     */
    private String courseCode;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 班级编码
     */
    private String classCode;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生组号
     */
    private String studentGroup;
    //学生学号
    private String studentNo;
    /**
     * 教师反馈意见
     */
    private String feedback;
    /**
     * 是否审核
     */
    private Integer isAudit;
    //功能模块
    private String functionalModule;
    //学生姓名
    private String studentName;
}
