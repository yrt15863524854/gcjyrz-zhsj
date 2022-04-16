package com.zhsj;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@MapperScan("com.zhsj.system.mapper")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ZhsjApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhsjApplication.class, args);
        System.out.println("系统启动成功");
    }
}
