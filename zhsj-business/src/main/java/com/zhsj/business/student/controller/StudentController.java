package com.zhsj.business.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.config.ZhsjConfig;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.domain.entity.SysUser;
import com.zhsj.common.core.page.TableDataInfo;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.ImportUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.StringUtils;
import com.zhsj.system.domain.SysUserRole;
import com.zhsj.system.domain.UserRolePO;
import com.zhsj.system.mapper.SysUserMapper;
import com.zhsj.system.service.ISysRoleService;
import com.zhsj.system.service.ISysUserService;
import jdk.nashorn.internal.objects.Global;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("business/student")
public class StudentController extends BaseController {
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private StudentService studentService;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ClassInfoMapper classInfoMapper;
    @Resource
    private ISysRoleService iSysRoleService;
    @GetMapping("/get")
    public TableDataInfo get(StudentQueryDto studentQueryDto){
        startPage();
        List<StudentDto> list = studentService.listStudent(studentQueryDto);
        return getDataTable(list);
    }
    @PostMapping("/add")
    public AjaxResult add(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") Integer id){
        return AjaxResult.success(studentService.getById(id));
    }

    @PostMapping("/importTemplate")
    @ResponseBody
    public void importTemplate(HttpServletResponse response){
        studentService.importTemplate(response);
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = ZhsjConfig.getProfile();//图片存储路径
        String userName = SecurityUtils.getLoginUser().getUsername();//登录用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        String month = sdf.format(cal.get(Calendar.MONTH));
        String day = sdf.format(cal.get(Calendar.DAY_OF_MONTH));
        String realPath = imagePath + month + "/";
        File files = new File(realPath);
        if (!files.exists()){
            files.mkdirs();
        }
        response.setContentType("text/plain; charset = UTF-8");
        String originalFilename;
        String importFilePath = null;
        if (file.isEmpty()){
            logger.info("请选择文件后上传");
            return error("请选择文件后上传");
        } else {
            originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            if ("xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension)) {
                originalFilename = userName + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFilename);
                try {
                    file.transferTo(new File(realPath, originalFilename));
                    //保存文件路径
                    importFilePath = imagePath + month + "/" + originalFilename;
                    logger.info("load success" + request.getContextPath() + File.separator + "upload" + File.separator + day + File.separator + originalFilename);
                    logger.info("leaving upload!");
                } catch (Exception e) {
                    logger.info("文件[" + originalFilename + "]上传失败，堆栈轨迹如下");
                    e.printStackTrace();
                    logger.info("文件上传失败，请重试！！");
                    return error("文件上传失败，请重试！！");
                }
            }else {
                logger.info("load success 只支持xls,xlsx格式");
            }
        }
        List<StudentPO> list = UpdateExcelBindingContainers(importFilePath);
        String message = importVendor(list);
        return AjaxResult.success(message);
    }
    /**
     * 学生信息
     */
    private List<StudentPO> UpdateExcelBindingContainers(String importFilePath) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(importFilePath);
        InputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row currentRow;
        List<StudentPO> list = new ArrayList<>();
        if (sheet.getLastRowNum() == 0) {
            throw new RuntimeException("Excel没有数据");
        }
        for (int i = 1; i<= sheet.getLastRowNum(); i++){
            StudentPO detail = new StudentPO();
            SysUser user = new SysUser();
            UserRolePO userRolePO = new UserRolePO();
            currentRow = sheet.getRow(i);
            if (currentRow == null) {
                continue;
            }
            //学生编码
            String studentCode = ImportUtils.getJavaValue(currentRow.getCell(0)).toString();
            if (studentCode.isEmpty()) {
                throw new BaseException("第" + (i + 1) + "学生编码不能为空");
                //throw new RuntimeException("第" + (i + 1) + "学生编码不能为空");
            } else {
                StudentPO code = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_code", studentCode));
                if (Objects.nonNull(code)) {
                    throw new BaseException("第" + (i + 1) + "行的学生编码信息已存在");
                    //throw new RuntimeException("第" + (i + 1) + "行的学生编码信息已存在");
                }
                detail.setStudentCode(studentCode);
            }
            //学生学号
            String studentNo = ImportUtils.getJavaValue(currentRow.getCell(1)).toString();
            if (studentNo.isEmpty()) {
                throw new BaseException("第" + (i + 1) + "学生学号不能为空");
                //throw new RuntimeException("第" + (i + 1) + "学生学号不能为空");
            } else {
                StudentPO no = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_no", studentNo));
                if (Objects.nonNull(no)) {
                    throw new BaseException("第" + (i + 1) + "行学生学号信息已存在");
                   //throw new RuntimeException("第" + (i + 1) + "行学生学号信息已存在");
                }
                long sno = Long.parseLong(studentNo);
                user.setUserId(sno);
                user.setUserName(studentNo);
                userRolePO.setUserId(sno);
                detail.setStudentNo(studentNo);
            }
            //学生姓名
            String studentName = ImportUtils.getJavaValue(currentRow.getCell(2)).toString();
            if (studentName.isEmpty()) {
                throw new BaseException("第" + (i + 1) + "学生姓名不能为空");
                //throw new RuntimeException("第" + (i + 1) + "学生姓名不能为空");
            } else {
                user.setNickName(studentName);
                detail.setStudentName(studentName);
            }
            //学生班级
            String studentClass = ImportUtils.getJavaValue(currentRow.getCell(3)).toString();
            if (studentClass.isEmpty()) {
                throw new BaseException("第" + (i + 1) + "学生班级不能为空");
                //throw new RuntimeException("第" + (i + 1) + "学生班级不能为空");
            } else {
                ClassInfoPO code = classInfoMapper.selectOne(new QueryWrapper<ClassInfoPO>().eq("class_code", studentClass));
                if (Objects.isNull(code)) {
                    throw new BaseException("第" + (i + 1) + "行的班级信息未找到");
                    //throw new RuntimeException("第" + (i + 1) + "行的班级信息未找到");
                }
                detail.setStudentClass(studentClass);
            }
            //学生组号
            if (Objects.isNull(currentRow.getCell(4))){
                detail.setStudentGroup(null);
            } else {
                String group = ImportUtils.getJavaValue(currentRow.getCell(4)).toString();
                detail.setStudentGroup(Integer.parseInt(group));
            }
            //是否为组长
            if (Objects.isNull(currentRow.getCell(5))){
                detail.setLeader(null);
            } else {
                String lead = ImportUtils.getJavaValue(currentRow.getCell(5)).toString();
                detail.setLeader(Integer.parseInt(lead));
            }
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            user.setCreateBy(getUsername());
            user.setPassword(SecurityUtils.encryptPassword("123456"));
            userRolePO.setRoleId((long)100);
            iSysUserService.insertUser(user);
            iSysRoleService.insertUserRole(userRolePO);
            list.add(detail);
        }
        return list;
    }

    /**
     * 导入学生信息
     */
    private String importVendor(List<StudentPO> detailList) throws Exception{
        if (StringUtils.isNull(detailList) || detailList.size() == 0) {
            throw new RuntimeException("导入学生数据不能为空");
        }
        StringBuilder successMsg = new StringBuilder();
        for (StudentPO student: detailList) {
            student.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            student.setCreateTime(DateUtils.getNowDate());
        }
        if (!studentService.saveBatch(detailList)) {
            throw new BaseException("学生信息批量导入异常");
        }
        successMsg.insert(0, "数据已全部导入共" + detailList.size() + "条");
        return successMsg.toString();
    }
}
