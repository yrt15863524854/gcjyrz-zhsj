package com.zhsj.business.manual.domain;

import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

/**
 * @className: TopicDetailPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:10
 * version 1.0
 **/
@Data
public class TopicDetailPO extends BaseEntityPO {
    private Long id;
    //html
    private String html;
    //text
    private String text;
    //学生组号
    private Integer studentGroup;
}
