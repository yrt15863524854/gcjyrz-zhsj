package com.zhsj.business.uploadDown.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.uploadDown.domain.UploadPO;
import com.zhsj.business.uploadDown.dto.UploadDto;
import com.zhsj.business.uploadDown.dto.UploadQueryDto;
import com.zhsj.business.uploadDown.mapper.UploadMapper;
import com.zhsj.business.uploadDown.service.UploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @className: UploadServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 16:03
 * version 1.0
 **/
@Service
public class UploadServiceImpl extends ServiceImpl<UploadMapper, UploadPO> implements UploadService {

    @Resource
    private UploadMapper uploadMapper;

    @Override
    public UploadDto getUploadRecord(String studentNo) {
        return uploadMapper.getUploadRecord(studentNo);
    }

    @Override
    public UploadDto getUploadRecord2(UploadQueryDto dto) {
        if (Objects.nonNull(dto.getStudentNo()) && Objects.nonNull(dto.getUploadTime())) {
            return uploadMapper.getUploadRecord2(dto);
        } else {
            return null;
        }
    }
}
