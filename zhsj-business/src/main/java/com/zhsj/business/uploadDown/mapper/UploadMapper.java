package com.zhsj.business.uploadDown.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.uploadDown.domain.UploadPO;
import com.zhsj.business.uploadDown.dto.UploadDto;
import com.zhsj.business.uploadDown.dto.UploadQueryDto;
import org.apache.ibatis.annotations.Param;

/**
 * @className: UploadMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 16:00
 * version 1.0
 **/
public interface UploadMapper extends BaseMapper<UploadPO> {
    UploadDto getUploadRecord(@Param("studentNo") String studentNo);
    UploadDto getUploadRecord2(@Param("dto")UploadQueryDto dto);
}
