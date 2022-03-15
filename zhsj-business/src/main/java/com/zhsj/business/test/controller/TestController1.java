package com.zhsj.business.test.controller;

import com.zhsj.business.test.domain.Test;
import com.zhsj.business.test.service.TestService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/business/test")
public class TestController1 extends BaseController {
    @Resource
    private TestService testService;

    @GetMapping("/get")
    public TableDataInfo get(){
        startPage();
        List<Test> list = testService.getAllTest();
        return getDataTable(list);
    }
}
