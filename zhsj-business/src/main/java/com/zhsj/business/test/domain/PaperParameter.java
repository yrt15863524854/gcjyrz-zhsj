package com.zhsj.business.test.domain;

import lombok.Data;

/**
 * @className: PaperParameter
 * @description: TODO
 * @author: yrt
 * date: 2022/7/1 20:30
 * version 1.0
 **/
@Data
public class PaperParameter {
    //每页条数
    int pageNum;
    //页数
    int pageSize;
}
