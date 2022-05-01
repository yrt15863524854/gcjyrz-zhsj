package com.zhsj.business.dynamicDenerationTest.annotation;

import java.lang.annotation.*;

/**
 * @annotationTypeName: RelationFiled
 * @description: TODO
 * @author: yrt
 * date: 2022/4/27 23:20
 * version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationFiled {
    /**
     * 视图模型对象中数据的key
     * @return string
     */
    String attributeName();
    /**
     * 数据对象的属性（流程实例id）
     * @return string
     */
    String propertyName() default "procInsId";
    /**
     * 数据对象要添加的字段名称
     * @return string[]
     */
    String[] fields() default {};
}
