package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.exception.advice.DefaultExceptionAdvice;
import com.rookies3.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController{

    private final BookService bookService;

    // 성공
    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks() {
        List<BookDTO.BookResponse> allBooks = bookService.getAllBooks();
        return new ResponseEntity<>(
                allBooks,
                HttpStatus.OK
        );
    }

//    @GetMapping
//    public List<BookDTO.BookResponse> getAllBooks() {
//        return bookService.getAllBooks()
//                .stream()
//                .map(BookDTO.BookResponse::from)
//                //.map(book -> BookDTO.BookResponse.from(book))
//                .toList();
//    }

    //성공
    @GetMapping("/id/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id){
        return new ResponseEntity<>(
                bookService.getBookById(id),
                HttpStatus.OK
        );
    }



    //성공
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn) {
        return new ResponseEntity<>(
                bookService.getBookByIsbn(isbn),
                HttpStatus.FOUND
        );
    }



    //성공
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO.BookResponse>> getBookByAuthor(@PathVariable String author) {
        return new ResponseEntity<>(
                bookService.getBookByAuthor(author),
                HttpStatus.OK
        );
    }


    //성공
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(
            @Valid @RequestBody BookDTO.BookCreateRequest request) {
        return new ResponseEntity<>(
                bookService.createBook(request),
                HttpStatus.CREATED
        );
    }


    //성공
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(
            @PathVariable Long id, @Valid @RequestBody BookDTO.BookUpdateRequest request) {
        return new ResponseEntity<>(
                bookService.updateBook(id, request),
                HttpStatus.OK
        );
    }

    //성공
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }




}