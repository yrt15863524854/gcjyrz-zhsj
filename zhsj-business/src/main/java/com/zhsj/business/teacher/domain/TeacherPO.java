package com.zhsj.business.teacher.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @className: TeacherPO
 * @description: TODO
 * @author: yrt
 * date: 2022/3/27 16:30
 * version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@TableName("business_teacher")
@Data
public class TeacherPO extends BaseEntityPO {
    private Long id;
    private String teacherCode;
    private String teacherName;
    private String teacherPost;
}
