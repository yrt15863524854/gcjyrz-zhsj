package com.zhsj.business.classManagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.classManagement.domain.ClassPO;
import com.zhsj.business.classManagement.mapper.ClassMapper;
import com.zhsj.business.classManagement.service.ClassService;
import org.springframework.stereotype.Service;

/**
 * @className: ClassServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 13:35
 * version 1.0
 **/
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, ClassPO> implements ClassService {
}
