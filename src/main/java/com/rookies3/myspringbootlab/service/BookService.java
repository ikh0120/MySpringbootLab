package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.entity.BookDetail;
import com.rookies3.myspringbootlab.exception.BusinessException;
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

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        BookDetail detail = BookDetail.builder()
                .description(request.getDetailRequest().getDescription())
                .language(request.getDetailRequest().getLanguage())
                .pageCount(request.getDetailRequest().getPageCount())
                .publisher(request.getDetailRequest().getPublisher())
                .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                .edition(request.getDetailRequest().getEdition())
                .build();

        book.setBookDetail(detail);

        Book saveBook = bookRepository.save(book);

        return BookDTO.Response.fromEntity(saveBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request){
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(book -> {
                    throw new BusinessException("ISBN 겹침", HttpStatus.CONFLICT);
                });

        Book update = bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(request.getTitle());
                    book.setAuthor(request.getAuthor());
                    book.setIsbn(request.getIsbn());
                    book.setPrice(request.getPrice());
                    book.setPublishDate(request.getPublishDate());

                    BookDetail detail = BookDetail.builder()
                            .description(request.getDetailRequest().getDescription())
                            .language(request.getDetailRequest().getLanguage())
                            .pageCount(request.getDetailRequest().getPageCount())
                            .publisher(request.getDetailRequest().getPublisher())
                            .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                            .edition(request.getDetailRequest().getEdition())
                            .build();

                    book.setBookDetail(detail);

                    return book;
                }).orElseThrow(() -> {
                            throw new BusinessException("존재하는 id가 없습니다", HttpStatus.NOT_FOUND);
                        }
                );

        return BookDTO.Response.fromEntity(update);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(() ->
                new BusinessException("Book Not Found Please Change By Id!", HttpStatus.NOT_FOUND));
        bookRepository.deleteById(id);
    }


}