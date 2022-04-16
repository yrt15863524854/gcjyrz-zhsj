package com.zhsj.business.manual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.manual.domain.TopicDetailPO;
import com.zhsj.business.manual.dto.TopicDetailDto;
import com.zhsj.business.manual.dto.TopicDetailQueryDto;

/**
 * @interfaceName: TopicDetailService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:52
 * version 1.0
 **/
public interface TopicDetailService extends IService<TopicDetailPO> {
    /**
     * @description getTopicDetail
     * @date 2022/4/8 11:01
     * @param dto
     * @return com.zhsj.business.manual.dto.TopicDetailDto
     * @author yrt
     **/
    TopicDetailDto getTopicDetail(TopicDetailQueryDto dto);
    /**
     * @description insertTopicDetail
     * @date 2022/4/8 11:01
     * @param dto
     * @return void
     * @author yrt
     **/
    void insertTopicDetail(TopicDetailDto dto);
    /**
     * @description updateTopicDetail
     * @date 2022/4/8 11:01
     * @param dto
     * @return void
     * @author yrt
     **/
    void updateTopicDetail(TopicDetailDto dto);
}
