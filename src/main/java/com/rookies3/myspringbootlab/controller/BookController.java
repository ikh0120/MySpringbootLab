package com.rookies3.myspringbootlab.controller;


import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController{

    private final BookService bookService;

    //GET - 모든 책 조회 //성공
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks(){
        List<BookDTO.Response> books = bookService.getAllBooks();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    //GET - /{id} - id로 책 조회 //성공
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id){
        BookDTO.Response bookById = bookService.getBookById(id);

        return new ResponseEntity<>(bookById, HttpStatus.OK);
    }

    //GET - /isbn/{isbn} - isbn으로 책 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.Response bookByIsbn = bookService.getBookByIsbn(isbn);

        //return new ResponseEntity<>(bookByIsbn, HttpStatus.OK);
        return ResponseEntity.ok(bookByIsbn);
    }

    //GET - /search/author?author={author} - 저자로 책 검색 //성공
    @GetMapping("/search/author") // 뒤에 @RequestParam의 변수명과 값이 ?author={author}로 이어붙인 뒤 들어감
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author){
        List<BookDTO.Response> bookListByAuthor = bookService.getBooksByAuthor(author);

        return new ResponseEntity<>(bookListByAuthor, HttpStatus.OK);
    }
    //GET - /search/title?title={title} - 제목으로 책 검색 //성공
    @GetMapping("/search/title") //url에 입력하는 값 중 공백이 있다면 %20 혹은 +로 이어주기
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title){
        List<BookDTO.Response> bookListByTitle = bookService.getBooksByTitle(title);
        return ResponseEntity.ok(bookListByTitle);
    }

    //POST - 책 생성 //성공
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request){
        BookDTO.Response book = bookService.createBook(request);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    //PUT - /{id} - 책 수정 //성공
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id,
                                                       @Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response updateBook = bookService.updateBook(id, request);
        return new ResponseEntity<>(updateBook, HttpStatus.OK);
    }

    //DELETE - /{id} - 책 삭제 //성공
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        System.out.println("Delete Book with id: " + id);
        //return new ResponseEntity<>(null, HttpStatus.OK);
        //return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }




}