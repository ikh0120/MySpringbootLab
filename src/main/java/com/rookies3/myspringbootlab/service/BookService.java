package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.entity.BookDetail;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.exception.ErrorCode;
import com.rookies3.myspringbootlab.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
//                .map(book -> BookDTO.Response.fromEntity(book))
                .map(BookDTO.Response::fromEntity)
//                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ID", id));
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(BookDTO.Response::fromEntity)
//                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ISBN", isbn));
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

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        //BookDTO.Response response = BookDTO.Response.builder()
//                .title(request.getTitle())
//                .author(request.getAuthor())
//                .isbn(request.getIsbn())
//                .price(request.getPrice())
//                .publishDate(request.getPublishDate())
//                .build();
        //
        //BookDTO.BookDetailResponse bookDetails = BookDTO.BookDetailResponse.builder()
//                .description(request.getDetailRequest().getDescription())
//                .language(request.getDetailRequest().getLanguage())
//                .pageCount(request.getDetailRequest().getPageCount())
//                .publisher(request.getDetailRequest().getPublisher())
//                .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
//                .edition(request.getDetailRequest().getEdition())
//                .build();
        //
        //response.setDetail(bookDetails);
        // ISBN이 존재하는지 확인하기
        if(bookRepository.existsByIsbn(request.getIsbn())){
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }
        
        //우선 Book 엔티티 만들기
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        //request의 DetailRequest가 null이 아닐 때만 동작
        if(request.getDetailRequest() != null) { 
            //BookDetail을 만들어서
            BookDetail detail = BookDetail.builder()
                            .description(request.getDetailRequest().getDescription())
                            .language(request.getDetailRequest().getLanguage())
                            .pageCount(request.getDetailRequest().getPageCount())
                            .publisher(request.getDetailRequest().getPublisher())
                            .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                            .edition(request.getDetailRequest().getEdition())
                            //연관관계 저장을 위해 추가하기
                            .book(book)
                            .build();
            
            //연관관계 저장을 위해 추가하기
            book.setBookDetail(detail);
        }
        
        Book saveBook = bookRepository.save(book);

        return BookDTO.Response.fromEntity(saveBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request){
//        bookRepository.findByIsbn(request.getIsbn())
//                .ifPresent(book -> {
//                    throw new BusinessException("ISBN 겹침", HttpStatus.CONFLICT);
//                });
//
//        Book update = bookRepository.findById(id)
//                .map(book -> {
//                    book.setTitle(request.getTitle());
//                    book.setAuthor(request.getAuthor());
//                    book.setIsbn(request.getIsbn());
//                    book.setPrice(request.getPrice());
//                    book.setPublishDate(request.getPublishDate());
//
//                    BookDetail detail = BookDetail.builder()
//                            .description(request.getDetailRequest().getDescription())
//                            .language(request.getDetailRequest().getLanguage())
//                            .pageCount(request.getDetailRequest().getPageCount())
//                            .publisher(request.getDetailRequest().getPublisher())
//                            .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
//                            .edition(request.getDetailRequest().getEdition())
//                            .build();
//
//                    book.setBookDetail(detail);
//
//                    return book;
//                }).orElseThrow(() -> {
//                            throw new BusinessException("존재하는 id가 없습니다", HttpStatus.NOT_FOUND);
//                        }
//                );
//
//        return BookDTO.Response.fromEntity(update);
        //업데이트 할 Book 객체 찾기
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ID", id));

        //isbn 중복 검사
        if(!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        //업데이트 하기
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        //bookDetail 업데이트 하기
        if(request.getDetailRequest() != null) {
            BookDetail bookDetail = book.getBookDetail();

            //새로운 bookDetail 업데이트 하기
            if(bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }

            //bookDetail 필드 업데이트하기
            bookDetail.setDescription(request.getDetailRequest().getDescription());
            bookDetail.setLanguage(request.getDetailRequest().getLanguage());
            bookDetail.setPageCount(request.getDetailRequest().getPageCount());
            bookDetail.setPublisher(request.getDetailRequest().getPublisher());
            bookDetail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            bookDetail.setEdition(request.getDetailRequest().getEdition());
        }

        //업데이트 한 값 저장하기
        Book update = bookRepository.save(book);
        return BookDTO.Response.fromEntity(update);

    }

    @Transactional
    public void deleteBook(Long id) {
//        bookRepository.findById(id).orElseThrow(() ->
//                new BusinessException("Book Not Found Please Change By Id!", HttpStatus.NOT_FOUND));

        if(!bookRepository.existsById(id)){
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ID", id);
        }
        bookRepository.deleteById(id);
    }


}