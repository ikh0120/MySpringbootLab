package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public List<BookDTO.BookResponse> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDTO.BookResponse> bookDTOResponseList = new ArrayList<>();

        for(Book book : bookList) {
            bookDTOResponseList.add(BookDTO.BookResponse.from(book));
        }
        return bookDTOResponseList;
    }

//    public List<Book> getAllBooks() {
//        return bookRepository.findAll();
//    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book result = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(result);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book result = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(result);
    }

    public List<BookDTO.BookResponse> getBookByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                        .stream()
                        .map(BookDTO.BookResponse::from)
                        .toList();
    }


    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request){
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(book -> {
                    throw new BusinessException("같은 isbn을 가진 책이 존재합니다. ", HttpStatus.CONFLICT);
                });

        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        return BookDTO.BookResponse.from(savedBook);
//        return BookDTO.BookResponse.from(bookRepository.save(request.toEntity()));
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        BookDTO.BookResponse book = getBookById(id);

        book.setPrice(request.getPrice());

        return book;
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

}
