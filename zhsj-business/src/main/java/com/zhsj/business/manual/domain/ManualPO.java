package com.zhsj.business.manual.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

@TableName("business_manual")
@Data
public class ManualPO extends BaseEntityPO {
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
     * 班级编码
     */
    private String classCode;
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
    /**
     * 学生学号
     */
    private String studentNo;
    /**
     * 功能模块
     */
    private String functionalModule;
}
