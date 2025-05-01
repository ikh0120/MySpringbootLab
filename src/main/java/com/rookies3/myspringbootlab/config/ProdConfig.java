package com.rookies3.myspringbootlab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class ProdConfig {
    @Bean
    public MyEnvironment myEnv(){
        return MyEnvironment
                .builder()      //열고 MyEnviromentBuilder
                .mode("운영환경") //넣고 setMode("운영환경")
                .build();       //닫기 MyEnviroment 타입으로 변경
    }
}
