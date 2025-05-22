package com.rookies3.myspringbootlab.controller.dto;

import com.rookies3.myspringbootlab.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$", //정규표현식 사용
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;
        
        @Valid //null값이 들어오면 안된다
        private BookDetailDTO detailRequest;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @NoArgsConstructor //기본생성자 자동 생성
    @AllArgsConstructor //모든 필드를 매개변수로 받는 생성자 자동 생성
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        //Book => BookDTO.Response
        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null //book.getBookDetail()이 null이 아니라면
                    //BookDetailResponse.builder()...build()로 Builder 객체를 만든 뒤 다시 BookDetailResponse로 반환하겠다
                    ? BookDetailResponse.builder()
                        .id(book.getBookDetail().getId())
                        .description(book.getBookDetail().getDescription())
                        .language(book.getBookDetail().getLanguage())
                        .pageCount(book.getBookDetail().getPageCount())
                        .publisher(book.getBookDetail().getPublisher())
                        .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                        .edition(book.getBookDetail().getEdition())
                        .build()
                    //book.getBookDetail()이 null이라면 null을 반환하겠다
                    : null;
            
            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }
    
    @Data
    @NoArgsConstructor //기본 생성자 생성
    @AllArgsConstructor //모든 매개변수 받는 생성자 생성
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}