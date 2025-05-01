package com.rookies3.myspringbootlab.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//myprop.username=springboot
//myprop.port=${random.int(1,100)}


@Getter @Setter
@Component
@ConfigurationProperties("myprop")
public class MyPropProperties {
    private String username;
    private int port;

}
