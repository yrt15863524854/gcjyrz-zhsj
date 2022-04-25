package com.zhsj.business.uploadDown.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.uploadDown.domain.UploadPO;
import com.zhsj.business.uploadDown.dto.UploadDto;
import com.zhsj.business.uploadDown.dto.UploadQueryDto;
import org.apache.ibatis.annotations.Param;

/**
 * @interfaceName: UploadService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 16:02
 * version 1.0
 **/
public interface UploadService extends IService<UploadPO> {
    UploadDto getUploadRecord(@Param("studentNo") String studentNo);
    UploadDto getUploadRecord2(@Param("dto") UploadQueryDto dto);
}
