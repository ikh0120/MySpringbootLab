package com.rookies3.myspringbootlab.controller.dto;

import com.rookies3.myspringbootlab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateRequest { //도서 생성 시 사용되는 DTO
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        private String isbn;

        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;


        //BookCreateRequest => Entity
        public Book toEntity() { //형변환
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateRequest { //도서 정보 업데이트 시 사용되는 DTO
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        // 확장 가능성을 위해 추가 필드들을 옵셔널하게 포함할 수 있음
        private String title;
        private String author;
        private LocalDate publishDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponse { //클라이언트에게 반환되는 도서 정보 DTO
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;


        //Book => BookResponse
        public static BookResponse from(Book book) {
            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPrice(),
                    book.getPublishDate()
            );
        }
    }
}