package com.zhsj.business.uploadDown;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.business.uploadDown.domain.UploadPO;
import com.zhsj.business.uploadDown.service.UploadService;
import com.zhsj.common.config.ZhsjConfig;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: UploadDownloadController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/19 11:24
 * version 1.0
 **/
@RestController
public class UploadDownloadController {
    @Resource
    private StudentService studentService;
    @Resource
    private ClassInfoService classInfoService;
    @Resource
    private UploadService uploadService;
    @RequestMapping("/business/upload")
    @ResponseBody
    public Map<String, Object> importData(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String path = ZhsjConfig.getProfile();
        String username = SecurityUtils.getLoginUser().getUsername();
        QueryWrapper<StudentPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", username);
        StudentPO student = studentService.getOne(wrapper);
        String studentGroup = student.getStudentGroup() + "";
        String studentName = student.getStudentName();
        QueryWrapper<ClassInfoPO> classInfoPOQueryWrapper = new QueryWrapper<>();
        classInfoPOQueryWrapper.eq("class_code", student.getStudentClass());
        ClassInfoPO classInfo = classInfoService.getOne(classInfoPOQueryWrapper);
        String className = classInfo.getClassName();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String year = format.format(new Date());
        String courseDesign = year + "????????????";
        String realPath = path + "/" + courseDesign + "/";
        File files = new File(realPath);
        if (!files.exists()) {
            files.mkdirs();
        }
        response.setContentType("test/plain; chartset = UTF-8");
        String originalFilename = "";
        if (file.isEmpty()) {
            map.put("info", "????????????????????????");
        } else {
            originalFilename = file.getOriginalFilename();
            UploadPO uploadPO = new UploadPO();
            uploadPO.setId(IdUtils.longID());
            uploadPO.setStudentGroup(studentGroup);
            uploadPO.setStudentNo(username);
            uploadPO.setUploadName(originalFilename);
            uploadPO.setUploadTime(DateUtils.getNowDate());
            uploadService.save(uploadPO);
            map.put("name", originalFilename);
            map.put("studentNo", username);
            map.put("studentGroup", studentGroup);
            String extension = FilenameUtils.getExtension(originalFilename);
            if ("zip".equalsIgnoreCase(extension)) {
                originalFilename = className + "_" + username + "_" + studentName + "_" + studentGroup + "???"+ "_" + "????????????" + ".zip";
                try {
                    file.transferTo(new File(realPath, originalFilename));
                    map.put("info", "??????????????????");
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("info", "??????????????????");
                }
            } else {
                map.put("info", "?????????.zip??????");
            }
        }
        return map;
    }
    @PostMapping("/business/download/{studentNo}")
    @ResponseBody
    public Map<String, Object> download(HttpServletResponse response, @PathVariable("studentNo") String studentNo) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<StudentPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", studentNo);
        StudentPO student = studentService.getOne(wrapper);
        String studentName = student.getStudentName();
        String studentGroup = student.getStudentGroup() + "";
        QueryWrapper<ClassInfoPO> classInfoPOQueryWrapper = new QueryWrapper<>();
        classInfoPOQueryWrapper.eq("class_code", student.getStudentClass());
        ClassInfoPO classInfo = classInfoService.getOne(classInfoPOQueryWrapper);
        String className = classInfo.getClassName();
        try {
            InputStream inputStream = new FileInputStream(genPath() + className + "_" + studentNo + "_" +studentName + "_" + studentGroup + "???"+ "_" + "????????????" + ".zip");
            //?????????????????????
            response.setContentType("application/force-download");
            ServletOutputStream out = response.getOutputStream();
            //??????URLEncoder??????????????????????????????????????????
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(className + studentGroup + "???"+ "_" + "????????????" + ".zip", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            map.put("message", "????????????");
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            map.put("message", e.getMessage() + "???" + studentGroup + "????????????");
        }
        return map;
    }
    private String getOriginalFilename(String studentNo) {
        return "";
    }

    private static String genPath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String year = format.format(new Date());
        String path = ZhsjConfig.getProfile();
        String courseDesign = year + "????????????";
        return path + "/" + courseDesign + "/";
    }
}
