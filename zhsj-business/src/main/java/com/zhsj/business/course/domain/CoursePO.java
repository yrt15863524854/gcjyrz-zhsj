package com.zhsj.business.course.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_course")
public class CoursePO extends BaseEntityPO {
    private Long id;
    private String courseCode;
    private String courseName;
    private String courseNature;
}
