package com.zhsj.business.courseTask.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @className: CourseTaskPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/21 13:58
 * version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("business_course_task")
public class CourseTaskPO extends BaseEntityPO {
    private Long id;
    private String courseTaskCode;
    private String courseCode;
    private String courseTaskName;
    private String courseBriefly;
    private String courseTaskHtml;
}
