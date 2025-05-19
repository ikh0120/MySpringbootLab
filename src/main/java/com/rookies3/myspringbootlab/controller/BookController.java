package com.rookies3.myspringbootlab.controller;


import com.rookies3.myspringbootlab.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController{

    private final BookService bookservice;


}