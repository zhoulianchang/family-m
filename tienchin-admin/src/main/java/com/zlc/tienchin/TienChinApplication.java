package com.zlc.tienchin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author tienchin
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TienChinApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(TienChinApplication.class, args);
    }
}
