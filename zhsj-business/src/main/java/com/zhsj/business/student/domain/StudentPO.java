package com.zhsj.business.student.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * student实体类
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("business_student")
public class StudentPO extends BaseEntityPO {

    private static final long serialVersionUID = 1L;
    /**
     * 学生id
     */
    private Integer id;
    /**
     * 学生编码
     */
    private String studentCode;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 学号
     */
    private String studentNo;
    /**
     * 学生班级
     */
    private String studentClass;
    /**
     * 学生组号
     */
    private Integer studentGroup;
    /**
     * 是否为组长
     */
    private Integer leader;

}
