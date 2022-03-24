package com.zhsj.business.manual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ManualMapper extends BaseMapper<ManualPO> {
    List<ManualDto> listManualInfo(@Param("dto")ManualQueryDto manualQueryDto);
}
