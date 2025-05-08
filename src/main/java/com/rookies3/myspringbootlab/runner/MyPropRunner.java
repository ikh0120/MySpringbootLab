package com.rookies3.myspringbootlab.runner;

import com.rookies3.myspringbootlab.config.MyEnvironment;
import com.rookies3.myspringbootlab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.swing.*;
//myprop.username=springboot
//myprop.port=${random.int(1,100)}

@Component
public class MyPropRunner implements ApplicationRunner {

    //1-4)
    @Value("${myprop.username}")
    private String name;
    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties prop;

    @Autowired
    private Environment env;

    @Autowired
    private MyEnvironment myEnv;

    private Logger log = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception{
//        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//        System.out.println("myprop.username: "+ name );
//        System.out.println("myprop.port: "+port);
//
//        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//        System.out.println("getUsername(): " + prop.getUsername());
//        System.out.println("getPort(): " + prop.getPort());
//
//        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//        //myprop.username=springboot
//        //myprop.port=${random.int(1,100)}
//        System.out.println("Environment로 환경변수 가져오기: "+env.getProperty("myprop.username"));
//        System.out.println("Environment로 환경변수 가져오기: "+env.getProperty("myprop.port"));
//
//
//        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//        //myprop.username=springboot
//        //myprop.port=${random.int(1,100)}
//        System.out.println("Active MyEnviroment Bean: " + myEnv);

        System.out.println("====================================================================================================================================================================================");

        // logger.debug, logger.info로 바꾸기
        log.info("logger.debug, logger.info로 변경하기");
        log.info("myprop.username: {}", name );
        log.info("myprop.port: {}",port);

        log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        log.info("getUsername(): {}", prop.getUsername());
        log.info("getPort(): {}", prop.getPort());

        log.debug("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        //myprop.username=springboot
        //myprop.port=${random.int(1,100)}
        log.debug("Environment로 환경변수 가져오기: {}", env.getProperty("myprop.username"));
        log.debug("Environment로 환경변수 가져오기: {}", env.getProperty("myprop.port"));


        log.debug("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        //myprop.username=springboot
        //myprop.port=${random.int(1,100)}
//        log.debug("env 출력: {}",env );
        log.debug("Active MyEnviroment Bean: {}", myEnv);

    }
}
