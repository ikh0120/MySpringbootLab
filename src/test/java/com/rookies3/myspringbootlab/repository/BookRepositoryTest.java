package com.rookies3.myspringbootlab.repository;


import com.rookies3.myspringbootlab.entity.Book;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@ActiveProfiles("prod")
//@DataJpaTest //해당 인터페이스에 @Transactional 어노테이션이 존재
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    void testShowAllBooks(){
        List<Book> listBooks = bookRepository.findAll();
        assertThat(listBooks).isNotNull();
        System.out.println("_____________________________________________________________");
        System.out.print("|title\t\t\t");
        System.out.print("|author\t");
        System.out.print("|isbn\t\t\t");
        System.out.print("|price\t");
        System.out.println("|publishDate|");
        System.out.println("-------------------------------------------------------------");
        for(Book book : listBooks){
            System.out.print("|"+book.getTitle()+"\t|");
            System.out.print(book.getAuthor()+"\t|");
            System.out.print(book.getIsbn()+"\t|");
            System.out.print(book.getPrice()+"\t|");
            System.out.println(book.getPublishDate()+"\t|");
            System.out.println("-------------------------------------------------------------");
        }


    }

    @Test
    @Rollback(value = false)
    @Disabled
    void testDeleteBook() {
        //Optional<Book> findByIsbn(String isbn);
        //List<Book> findByAuthor(String author);
        Book book1 = bookRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Not Found Book"));
        Book book2 = bookRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("책 없어요"));

        bookRepository.delete(book1);
        bookRepository.delete(book2);

    }

    @Test
    @Rollback(value = false)
    void testUpdateBook() {
        /**
        String title = "스프링 부트 입문";

        Optional<Book> optBooks = Optional.ofNullable(
                bookRepository.findByTitle(title)
                    .orElseThrow(() -> new RuntimeException(String.format("\"%s\"라는 제목을 가진 책은 존재하지 않습니다. ",title)))
        );

        List<Book> listBooks= new ArrayList<>();

        if(optBooks.isPresent()){
            while(!optBooks.isEmpty()){
                Book newBook = optBooks.get();
                listBooks.add(newBook);
            }
            for(Book expensiveBook : listBooks){
                expensiveBook.setPrice(expensiveBook.getPrice()*3);
            }

            bookRepository.saveAll(listBooks);
        }
        */
        String isbn = "9788956746425";
        String title="스프링 부트 입문";
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        book.setPrice(book.getPrice() * 3);
        assertThat(book.getPrice()).isEqualTo(book.getPrice());

    }

    @Test
    void testFindByAuthor() {
        //List<Book> findByAuthor(String author);
        String author = "박둘리";

        List<Book> books = bookRepository.findByAuthor(author);
        if (!books.isEmpty()) {
            // System.out.println("Found Books");
            System.out.print(author + " 작가님께서 집필하신 도서는 ");
            System.out.println(books.size() + "권 입니다. ");
            for (Book book : books) {
                System.out.println("도서 제목: " + book.getTitle());
            }
        } else {
            System.out.print(author);
            System.out.println(" 작가님께서 집필하신 도서는 없습니다. ");
        }

    }

    @Test
    void testFindByIsbn() {
        //Optional<Book> findByIsbn(String isbn);

        // 유효한 isbn 입력 시
        Optional<Book> optBook1 = bookRepository.findByIsbn("9788956746425");
        Book hongsBook = optBook1.orElseGet(() -> new Book());
        assertThat(hongsBook.getAuthor()).isEqualTo("홍길동");

        // 유효하지 않은 isbn 입력 시
        Book nothingBook = bookRepository.findByIsbn("253259810574")
                .orElseGet(() -> new Book());
        assertThat(nothingBook.getPrice()).isNull();
    }

    @Test
    @Rollback(value = false)
    @Disabled
    void testCreateBook() throws Exception {
        /**
         * Given-When-Then 패턴으로 구현하기
         */
        //Given
        Book book1 = new Book("스프링 부트 입문", "홍길동", "9788956746425", LocalDate.parse("2025-05-07"), 30000);
        //When
        Book addBook1 = bookRepository.save(book1);
        //Then
        assertThat(addBook1.getTitle()).isEqualTo("스프링 부트 입문");

        //Given
        Book book2 = new Book("JPA 프로그래밍","박둘리","9788956746432", LocalDate.parse("2025-04-30"), 35000);
        //When
        Book addBook2 = bookRepository.save(book2);
        //Then
        assertThat(addBook2.getAuthor()).isEqualTo("박둘리");


    }
}