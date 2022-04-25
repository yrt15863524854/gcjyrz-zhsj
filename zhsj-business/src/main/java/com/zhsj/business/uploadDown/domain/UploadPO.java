package com.zhsj.business.uploadDown.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @className: UploadPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 15:57
 * version 1.0
 **/
@Data
@TableName("business_upload")
public class UploadPO {
    private Long id;
    private String uploadName;
    private String studentNo;
    private String studentGroup;
    private Date uploadTime;
}
