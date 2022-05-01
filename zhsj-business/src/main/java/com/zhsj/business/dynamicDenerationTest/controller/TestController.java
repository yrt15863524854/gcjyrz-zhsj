package com.zhsj.business.dynamicDenerationTest.controller;//package com.zhsj.business.dynamicDeneration.controller;
//
//import com.zhsj.business.dynamicDeneration.annotation.RelationFiled;
//import com.zhsj.business.dynamicDeneration.utils.PropertyUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.Map;
//
///**
// * @className: TestController
// * @description: TODO
// * @author: yrt
// * date: 2022/4/27 23:24
// * version 1.0
// **/
//public class TestController {
//    @RelationFiled(attributeName="process",fields= {"startAudit","startAuditName"})
//    @RequestMapping(value = "exportPdf")
//    public String exportPdf(Process process, Model model) {
//        model.addAttribute("process", process);
//        return "modules/gz/process/exportPdf";
//    }
//    @Around("actPointcut()")
//    public Object doAround(ProceedingJoinPoint pjp){
//
//        Object obj = null;
//        try {
//            //执行切入点方法
//            obj = pjp.proceed();
//            //获取执行方法
//            MethodSignature ms = (MethodSignature)pjp.getSignature();
//            Method method = pjp.getTarget().getClass().getDeclaredMethod(ms.getName(), ms.getParameterTypes());
//            //获取执行方法RelationFiled注解
//            RelationFiled rf = method.getAnnotation(RelationFiled.class);
//            //获取方法参数
//            Object[] args = pjp.getArgs();
//            Object object = null;
//            ModelMap model = null;//根据自己项目实际情况而定，比如使用的是ModelAndView这里就是ModelAndView
//            for (Object arg : args) {
//                if (arg instanceof Model){
//                    model = (ModelMap) arg;
//                    object = model.get(rf.attributeName());//通过rf.attributeName()自定义注解参数值获取要生成属性的对象
//                    break;
//                }
//            }
//            //根据反射获取流程id
//            Field field = object.getClass().getDeclaredField(rf.propertyName());
//            field.setAccessible(true);
//            Object procInsId = field.get(object);
//            //根据流程id获取流程数据
//            Map<String, Object> map = actTaskService.getMap(procInsId == null ? null : procInsId.toString());
//            //添加指定生成的属性到map，model中如有指定属性的数据可同时进行处理
//            String[] fields = rf.fields();
//            for(String f : fields) {
//                //不覆盖存在的属性
//                if(!map.containsKey(f)) {
//                    map.put(f, "");
//                }
//            }
//            //生成属性并返回
//            model.addAttribute(mi.attributeName(), PropertyUtils.generate(object, map));
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }
//
//}
