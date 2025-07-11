package com.rookies3.myspringbootlab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {	
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus; //Spring의 Http 상태 코드

    public BusinessException(String message) {
        // HttpStatus.EXPECTATION_FAILED: 417
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }    
}
