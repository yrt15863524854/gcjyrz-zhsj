package com.zhsj.business.uploadDown.dto;

import lombok.Data;

import java.util.Date;

/**
 * @className: UploadDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 16:17
 * version 1.0
 **/
@Data
public class UploadDto {
    private String uploadName;
    private String studentNo;
    private String studentGroup;
    private Date uploadTime;
}
