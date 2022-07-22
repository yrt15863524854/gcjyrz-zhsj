package com.zhsj.business.test.service.impl;

import com.zhsj.business.test.domain.PaperParameter;
import com.zhsj.business.test.domain.Test;
import com.zhsj.business.test.mapper.TestMapper;
import com.zhsj.business.test.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class TestServiceImpl implements TestService {
    @Resource
    private TestMapper testMapper;

    @Override
    public List<Test> getAllTest() {
        return testMapper.getAllTest();
    }

    @Override
    public List<Test> dividePaper(PaperParameter paperParameter) {
        return testMapper.dividePaper(paperParameter);
    }
}
