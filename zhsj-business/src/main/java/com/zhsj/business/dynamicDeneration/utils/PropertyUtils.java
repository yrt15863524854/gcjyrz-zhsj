package com.zhsj.business.dynamicDeneration.utils;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: PropertyUtils
 * @description: TODO
 * @author: yrt
 * date: 2022/4/27 23:27
 * version 1.0
 **/
public class PropertyUtils {
    private static final class DynamicBean {
        private Object object;
        private BeanMap beanMap;
        //创建对象和属性map
        private DynamicBean(Class<?> superclass, Map<String, Class<?>> propertyMap) {
            this.object = generateBean(superclass, propertyMap);
            this.beanMap = BeanMap.create(this.object);
        }
        //为对象属性赋值
        private void setValue(String property, Object value) {
            beanMap.put(property, value);
        }

        private Object getObject() {
            return this.object;
        }
        //创建对象
        private Object generateBean(Class<?> superclass, Map<String, Class<?>> propertyMap) {
            BeanGenerator generator = new BeanGenerator();
            if (null != superclass) {
                generator.setSuperclass(superclass);
            }
            BeanGenerator.addProperties(generator, propertyMap);
            return generator.create();
        }
    }
    /**
     * 动态创建对象
     * @param valueMap 动态对象的属性和值
     * @return
     */
    public static Object generate(Map<String, Object> valueMap) {
        return generate(null, valueMap);
    }
    /**
     * 动态创建对象，可继承其它对象的属性
     * @param object 被继承属性的对象
     * @param valueMap 动态对象的属性和值
     * @return
     */
    public static Object generate(Object object, Map<String, Object> valueMap) {
        Map<String,Class<?>> fieldMap = new HashMap<String, Class<?>>();//要创建的属性
        valueMap.forEach((k, v) -> {
            if(k == null) {

            }else if(v == null) {
                fieldMap.put(k, Object.class);
            }else {
                fieldMap.put(k, v.getClass());
            }
        });
        Class<?> clazz = null;
        Class<?> superclass = null;
        if(object != null) {
            clazz = object.getClass();
            superclass = clazz;
        }
        //获取对象属性的值并保存到valueMap
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    valueMap.put(field.getName(), field.get(object));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
        //生成动态对象
        DynamicBean dynamicBean = new DynamicBean(superclass,fieldMap);
        //为动态对象属性赋值
        valueMap.forEach((k, v) -> {
            dynamicBean.setValue(k, v);
        });
        return dynamicBean.getObject();
    }
}
