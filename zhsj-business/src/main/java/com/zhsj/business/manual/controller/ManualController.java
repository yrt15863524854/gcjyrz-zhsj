package com.zhsj.business.manual.controller;

import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import com.zhsj.business.manual.service.ManualService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/business/manual")
public class ManualController extends BaseController {

    @Resource
    private ManualService manualService;

    @PostMapping("/importTemplate")
    @ResponseBody
    public void importTemplate(HttpServletResponse response) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/model/课程设计说明书模板.docx");
            //强制下载打不开
            response.setContentType("application/force-download");
            ServletOutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课程设计说明书模板.docx", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @PostMapping("/listManualInfo")
    public TableDataInfo listManualInfo(@RequestBody ManualQueryDto manualQueryDto) {
        startPage();
        List<ManualDto> list = manualService.ListManualInfo(manualQueryDto);
        return getDataTable(list);
    }

}
