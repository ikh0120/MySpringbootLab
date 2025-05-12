package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rookies3.myspringbootlab.repository.BookRepository;

@RestController //@ResponseBody + Controller
@RequiredArgsConstructor //Lombok이 final 필드나 @NonNull 필드 기반으로 생성자 자동 생성
@RequestMapping("/api/books") //이 컨트롤러의 모든 메서드는 "/api/books" 경로를 기본으로 가짐
public class BookController {

    private final BookRepository bookRepository;

    @PostMapping
    public Book create(@RequestBody Book book){
        return bookRepository.save(book);
    }


}
