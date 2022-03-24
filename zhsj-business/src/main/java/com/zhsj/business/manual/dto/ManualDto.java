package com.zhsj.business.manual.dto;

import lombok.Data;

@Data
public class ManualDto {
    /**
     * 设计书题目
     */
    private String manualName;
    /**
     * 课程编码
     */
    private String courseName;
    /**
     * 班级编码
     */
    private String className;
    /**
     * 学生组号
     */
    private String studentGroup;
    /**
     * 教师反馈意见
     */
    private String feedback;
    /**
     * 是否审核
     */
    private Integer isAudit;
}
