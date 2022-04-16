package com.zhsj.business.manual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.manual.domain.TopicDetailPO;
import com.zhsj.business.manual.dto.TopicDetailDto;
import com.zhsj.business.manual.dto.TopicDetailQueryDto;
import org.apache.ibatis.annotations.Param;

/**
 * @interfaceName: TopicDetailMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:14
 * version 1.0
 **/
public interface TopicDetailMapper extends BaseMapper<TopicDetailPO> {
    TopicDetailDto getTopicDetail(@Param("dto")TopicDetailQueryDto dto);
    void insertTopicDetail(@Param("PO") TopicDetailPO po);
    void updateTopicDetail(@Param("PO") TopicDetailPO po);
}
