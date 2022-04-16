package com.zhsj.business.manual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.manual.domain.TopicDetailPO;
import com.zhsj.business.manual.dto.TopicDetailDto;
import com.zhsj.business.manual.dto.TopicDetailQueryDto;
import com.zhsj.business.manual.mapper.TopicDetailMapper;
import com.zhsj.business.manual.service.TopicDetailService;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @className: TopicDetailServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:53
 * version 1.0
 **/
@Service
public class TopicDetailServiceImpl extends ServiceImpl<TopicDetailMapper, TopicDetailPO> implements TopicDetailService {
    @Resource
    private TopicDetailMapper topicDetailMapper;

    @Override
    public TopicDetailDto getTopicDetail(TopicDetailQueryDto dto) {
        return topicDetailMapper.getTopicDetail(dto);
    }

    @Override
    public void insertTopicDetail(TopicDetailDto dto) {
        TopicDetailPO po = new TopicDetailPO();
        BeanUtils.copyProperties(dto, po);
        po.setId(IdUtils.longID());
        po.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        po.setCreateTime(DateUtils.getNowDate());
        topicDetailMapper.insertTopicDetail(po);
    }

    @Override
    public void updateTopicDetail(TopicDetailDto dto) {
        TopicDetailPO po = new TopicDetailPO();
        BeanUtils.copyProperties(dto, po);
        po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        po.setUpdateTime(DateUtils.getNowDate());
        topicDetailMapper.updateTopicDetail(po);
    }
}
