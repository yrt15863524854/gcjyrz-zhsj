package com.zhsj.business.test.service;

import com.zhsj.business.test.domain.PaperParameter;
import com.zhsj.business.test.domain.Test;

import java.util.List;

public interface TestService {
    List<Test> getAllTest();
    List<Test> dividePaper(PaperParameter paperParameter);
}
