package com.zhsj.business.test.controller;

import com.zhsj.business.test.domain.PaperParameter;
import com.zhsj.business.test.domain.Test;
import com.zhsj.business.test.service.TestService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/pager")
    public List<Test> pager(@RequestBody PaperParameter paperParameter) {
        int pageNum = paperParameter.getPageNum();
        pageNum = (pageNum - 1)*paperParameter.getPageSize();
        paperParameter.setPageNum(pageNum);
        return testService.dividePaper(paperParameter);
    }
}
