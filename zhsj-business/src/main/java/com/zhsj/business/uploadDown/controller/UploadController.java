package com.zhsj.business.uploadDown.controller;

import com.zhsj.business.uploadDown.dto.UploadDto;
import com.zhsj.business.uploadDown.dto.UploadQueryDto;
import com.zhsj.business.uploadDown.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @className: UploadController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 16:50
 * version 1.0
 **/
@RestController
@RequestMapping("/business/upload")
public class UploadController {
    @Resource
    private UploadService uploadService;
    @PostMapping("/getUploadRecord")
    public UploadDto getUploadRecord(@RequestBody String studentNo) {
        return uploadService.getUploadRecord(studentNo);
    }
    @PostMapping("/getUploadRecord2")
    public UploadDto getUploadRecord2(@RequestBody UploadQueryDto dto) {
        return uploadService.getUploadRecord2(dto);
    }
}
