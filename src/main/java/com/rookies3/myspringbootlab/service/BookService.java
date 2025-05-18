package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    /**내 코드*/
    //public List<BookDTO.BookResponse> getAllBooks() {
    //    List<Book> bookList = bookRepository.findAll();
    //    List<BookDTO.BookResponse> bookDTOResponseList = new ArrayList<>();
    //
    //     for(Book book : bookList) {
    //        bookDTOResponseList.add(BookDTO.BookResponse.from(book));
    //    }
    //    return bookDTOResponseList;
    //}
    /**강사님 코드*/
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                //.map(book -> BookDTO.BookResponse.from(book))
                .map(BookDTO.BookResponse::from)
                .toList(); //Stream<BookDTO.BookResponse> => List<BookDTO.BookResponse>
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book result = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found with id:" + id, HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(result);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book result = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found with ISBN: " 
                        + isbn, HttpStatus.NOT_FOUND));
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
        //ISBN 중복검사
        bookRepository.findByIsbn(request.getIsbn()) //Optional<Book>
                .ifPresent(book -> { //같은 ISBN을 가진 book이 존재한다면
                    //오류 던지고 끝남
                    throw new BusinessException("같은 isbn을 가진 책이 존재합니다. ", HttpStatus.CONFLICT);
                });
        //BookCreateRequest => Entity
        Book book = request.toEntity();
        //등록 처리
        Book savedBook = bookRepository.save(book);
        //Book => BookResponse
        return BookDTO.BookResponse.from(savedBook);
//        return BookDTO.BookResponse.from(bookRepository.save(request.toEntity()));
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        BookDTO.BookResponse book = getBookById(id);

        if(book.getPrice() != null){ book.setPrice(request.getPrice()); }

        return book;
    }

    @Transactional
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)){
            throw new BusinessException("Book Not Found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }

}
