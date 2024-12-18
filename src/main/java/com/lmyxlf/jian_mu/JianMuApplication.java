package com.lmyxlf.jian_mu;

import com.lmyxlf.jian_mu.global.config.annotation.EnableCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCommonConfig
@SpringBootApplication
public class JianMuApplication {

    public static void main(String[] args) {
        SpringApplication.run(JianMuApplication.class, args);
    }

}
