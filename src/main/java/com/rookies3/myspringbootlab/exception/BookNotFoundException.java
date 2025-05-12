package com.rookies3.myspringbootlab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookNotFoundException extends RuntimeException{
    private HttpStatus httpStatus;

    public BookNotFoundException(){
        this("값을 넣어주세요!", HttpStatus.BAD_REQUEST);
    }

    public BookNotFoundException(String msg){
        this("404 Not Found: "+msg, HttpStatus.NOT_FOUND);
    }

    public BookNotFoundException(String msg, HttpStatus httpStatus){
        super(msg); // String RuntimeExcepion(msg);
        this.httpStatus = httpStatus;
    }



}
