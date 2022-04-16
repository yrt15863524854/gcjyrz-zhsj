package com.zhsj.business.manual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import com.zhsj.common.core.domain.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManualService extends IService<ManualPO> {
    /**
     * @description ListManualInfo
     * @date 2022/3/26 19:39
     * @param manualQueryDto
     * @return java.util.List<com.zhsj.business.manual.dto.ManualDto>
     * @author yrt
     **/
    List<ManualDto> ListManualInfo(ManualQueryDto manualQueryDto);
    /**
     * @description insertManual
     * @date 2022/4/2 16:38
     * @param manualDto
     * @return com.zhsj.common.core.domain.AjaxResult
     * @author yrt
     **/
    AjaxResult insertManual(ManualDto manualDto);
    /**
     * @description updateManual
     * @date 2022/4/2 16:38
     * @param manualDto
     * @return com.zhsj.common.core.domain.AjaxResult
     * @author yrt
     **/
    AjaxResult updateManual(ManualDto manualDto);
    /**
     * @description getRoleId
     * @date 2022/4/2 17:15
     * @param userId
     * @return java.lang.Long
     * @author yrt
     **/
    Long getRoleId(Long userId);
    /**
     * @description getStudentGroup
     * @date 2022/4/2 23:00
     * @param sno
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author yrt
     **/
    Map<String, Object> getStudentGroup(String sno);
    /**
     * @description getManualInfoBySg
     * @date 2022/4/2 23:05
     * @param dto
     * @return com.zhsj.business.manual.dto.ManualDto
     * @author yrt
     **/
    ManualDto getManualInfoBySg(ManualQueryDto dto);
}
