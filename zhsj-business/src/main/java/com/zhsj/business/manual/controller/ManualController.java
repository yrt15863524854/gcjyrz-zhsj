package com.zhsj.business.manual.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/business/manual")
public class ManualController {


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


}
