package com.zlc.family;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 启动程序
 *
 * @author family
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FamilyApplication {
    @Autowired
    private Flyway flyway;

    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(FamilyApplication.class, args);
    }

    @PostConstruct
    public void migrateDatabase() {
        // 设置全局默认时区为东八区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        // 手动触发 Flyway 迁移操作
        flyway.migrate();
    }
}
