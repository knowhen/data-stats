package org.when;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.when.data.*.mapper")
public class DataStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataStatsApplication.class, args);
    }

}
