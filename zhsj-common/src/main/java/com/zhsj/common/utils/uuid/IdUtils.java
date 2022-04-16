package com.zhsj.common.utils.uuid;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成器工具类
 *
 * @author zhsj
 */
public class IdUtils
{
    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }

    private static AtomicInteger counter = new AtomicInteger(0);
    /**
     * @description longID
     * @date 2022/4/2 11:03
     * @param
     * @return java.lang.Long
     * @author yrt
     **/
    public static Long longID() {
        if (counter.get() > 999999) {
            counter.set(1);
        }
        long time = System.currentTimeMillis();
        return time * 100 + counter.incrementAndGet();
    }

}
