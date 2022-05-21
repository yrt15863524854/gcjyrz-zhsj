package com.zhsj.business.point.dto;

import lombok.Data;

/**
 * @className: PointComments
 * @description: TODO
 * @author: yrt
 * date: 2022/5/1 20:14
 * version 1.0
 **/
@Data
public class PointComments {
//    studentName: '王同学',
//    className: '软件一班',
//    studentNo: '201811105055',
//    projectName: '学生管理系统',
//    moduleName: '上传题目、提交作业',
//    workLoad: '适中',
//    completeness: '一般',
//    attitude: '较好',
//    attendance:  '基本',
//    courseName: 'javaWeb',
//    knowledge: '一般',
//    ability: '一般',
//    analysis: '独立工作和分析解决问题的能力较好',
//    onTime: '能够按时',
//    format: '规范',
//    grade: '及格',
//    studentScore: '68',
//    isEdit: false,
//    addTextarea: '',
    private String studentName;
    private String className;
    private String studentNo;
    private String projectName;
    private String moduleName;
    private String workLoad;
    private String completeness;
    private String attitude;
    private String attendance;
    private String courseName;
    private String knowledge;
    private String ability;
    private String analysis;
    private String onTime;
    private String format;
    private String grade;
    private String studentScore;
    private Boolean isEdit = false;
    private String addTextarea = "";
    private Boolean isQueQin = false;
}
