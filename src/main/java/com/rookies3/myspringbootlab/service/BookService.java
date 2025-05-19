package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public BookDTO.Response getBookById(Long id){
        return bookRepository.findById(id)
                .map(BookDTO.Response::fromEntity)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(BookDTO.Response::fromEntity)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(book -> BookDTO.Response.fromEntity(book))
                .toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

//    @Transactional
//    public BookDTO.Response createBook(BookDTO.Request request) {
//        //BookDTO.Response response = BookDTO.Response.builder()
//        //        .title(request.getTitle())
//        //        .author(request.getAuthor())
//        //        .isbn(request.getIsbn())
//        //        .price(request.getPrice())
//        //        .publishDate(request.getPublishDate())
//        //        .detailRequest(
//        //                BookDTO.BookDetailDTO.builder()
//        //                        .description(request.getDetailRequest().getDescription())
//        //                        .language(request.getDetailRequest().getLanguage())
//        //                        .pageCount(request.getDetailRequest().getPageCount())
//        //                        .publisher(request.getDetailRequest().getPublisher())
//        //                        .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
//        //                        .edition(request.getDetailRequest().getEdition())
//        //                        .build()
//        //        )
//        //        .build();
//        BookDTO.Response.
//
//    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request){
        bookRepository.findByIsbn(request.getIsbn())
                .map(book -> new BusinessException("find by isbn in table in \"books\" ",HttpStatus.BAD_REQUEST));
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(request.getTitle());
                    book.setAuthor(request.getAuthor());
                    book.setIsbn(request.getIsbn());
                    book.setPrice(request.getPrice());
                    book.setPublishDate(request.getPublishDate());
                })
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }


}