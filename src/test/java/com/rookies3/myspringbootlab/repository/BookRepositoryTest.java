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
//    @Rollback(value = false)
    @Disabled
    void testCreateBook() throws Exception {
        /**기존 코드*/
        /**
         * Given-When-Then 패턴으로 구현하기
         */
        //Given
//        Book book1 = new Book("스프링 부트 입문", "홍길동", "9788956746425", LocalDate.parse("2025-05-07"), 30000);
//        //When
//        Book addBook1 = bookRepository.save(book1);
//        //Then
//        assertThat(addBook1.getTitle()).isEqualTo("스프링 부트 입문");
//
//        //Given
//        Book book2 = new Book("JPA 프로그래밍","박둘리","9788956746432", LocalDate.parse("2025-04-30"), 35000);
//        //When
//        Book addBook2 = bookRepository.save(book2);
//        //Then
//        assertThat(addBook2.getAuthor()).isEqualTo("박둘리");

        /**강사님 코드*/
        //Given
        Book book = new Book();
        book.setTitle("sk shieldus rookies 2");
        book.setAuthor("sk author 2");
        book.setIsbn("12122433342");
        book.setPublishDate(LocalDate.of(2023, 1, 14));
        book.setPrice(27000);

        //When
        Book saveBook = bookRepository.save(book);

        //Then
        assertThat(saveBook.getId()).isNotNull();
        assertThat(saveBook.getTitle()).isEqualTo("sk shieldus rookies 2");
        assertThat(saveBook.getAuthor()).isEqualTo("sk author 2");

    }

    @Test
    void testFindByIsbn() {
        /**기존 코드*/
        //Optional<Book> findByIsbn(String isbn);
//
//        // 유효한 isbn 입력 시
//        Optional<Book> optBook1 = bookRepository.findByIsbn("9788956746425");
//        Book hongsBook = optBook1.orElseGet(() -> new Book());
//        assertThat(hongsBook.getAuthor()).isEqualTo("홍길동");
//
//        // 유효하지 않은 isbn 입력 시
//        Book nothingBook = bookRepository.findByIsbn("253259810574")
//                .orElseGet(() -> new Book());
//        assertThat(nothingBook.getPrice()).isNull();

        /**강사님 코드*/
        //Given
        Book book = new Book();
        book.setTitle("꼭 읽어야 하는 책");
        book.setAuthor("이민혁");
        book.setIsbn("20000121");
        book.setPublishDate(LocalDate.of(2023, 1, 14));
        book.setPrice(27000);

        //Hibernate: insert into books (author,isbn,price,publish_date,title) values (?,?,?,?,?)
        bookRepository.save(book);
        //com.rookies3.myspringbootlab.entity.Book@745e1fb7
        System.out.println(book);

        //When
        //Hibernate: select b1_0.book_id,b1_0.author,b1_0.isbn,b1_0.price,b1_0.publish_date,b1_0.title from books b1_0 where b1_0.isbn=?
        Optional<Book> foundBook = bookRepository.findByIsbn("20000121");

        //Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("꼭 읽어야 하는 책");
    }

    @Test
    void testFindByAuthor() {
        /**기존 코드*/
        //List<Book> findByAuthor(String author);
//        String author = "박둘리";
//
//        List<Book> books = bookRepository.findByAuthor(author);
//        if (!books.isEmpty()) {
//            // System.out.println("Found Books");
//            System.out.print(author + " 작가님께서 집필하신 도서는 ");
//            System.out.println(books.size() + "권 입니다. ");
//            for (Book book : books) {
//                System.out.println("도서 제목: " + book.getTitle());
//            }
//        } else {
//            System.out.print(author);
//            System.out.println(" 작가님께서 집필하신 도서는 없습니다. ");
//        }

        /**강사님 코드*/
        // Given
        Book book1 = new Book();
        book1.setTitle("스프링 부트 입문");
        book1.setAuthor("홍길동");
        book1.setIsbn("12341234");
        book1.setPublishDate(LocalDate.of(2012,3,16));
        book1.setPrice(32000);

        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박둘리");
        book2.setIsbn("43214321");
        book2.setPublishDate(LocalDate.of(2003, 2, 11));
        book2.setPrice(30000);

        Book book3 = new Book();
        book3.setTitle("스프링 클라우드");
        book3.setAuthor("홍길동");
        book3.setIsbn("12345678");
        book3.setPublishDate(LocalDate.of(2024,7,8));
        book3.setPrice(38000);

//        bookRepository.save(book1);
//        bookRepository.save(book2);
//        bookRepository.save(book3);
        //이렇게 한 번에 저장 가능함
        bookRepository.saveAll(List.of(book1, book2, book3));

        //When
        List<Book> books = bookRepository.findByAuthor("홍길동");

        //Then
        assertThat(books).hasSize(2);
        assertThat(books).extracting("title").contains("스프링 부트 입문", "스프링 클라우드");
    }

    @Test
//    @Rollback(value = false)
    void testUpdateBook() {
        /**기존 코드*/
        //수정 전
//        String title = "스프링 부트 입문";
//
//        Optional<Book> optBooks = Optional.ofNullable(
//            bookRepository.findByTitle(title)
//            .orElseThrow(() -> new RuntimeException(String.format("\"%s\"라는 제목을 가진 책은 존재하지 않습니다. ",title)))
//        );
//
//        List<Book> listBooks= new ArrayList<>();
//
//        if(optBooks.isPresent()){
//            while(!optBooks.isEmpty()){
//                Book newBook = optBooks.get();
//                listBooks.add(newBook);
//            }
//            for(Book expensiveBook : listBooks){
//                expensiveBook.setPrice(expensiveBook.getPrice()*3);
//            }
//
//            bookRepository.saveAll(listBooks);
//        }

        //수정 후
//        String isbn = "9788956746425";
//        String title="스프링 부트 입문";
//        Book book = bookRepository.findByIsbn(isbn)
//                .orElseThrow(() -> new RuntimeException("Book Not Found"));
//        book.setPrice(book.getPrice() * 3);
//        assertThat(book.getPrice()).isEqualTo(book.getPrice());

        /**강사님 코드*/
        //Rollback시 update 쿼리가 안뜸
        //  Hibernate: update books set price=? where book_id=?가 안뜸
        //Given
        Book book = new Book();
        book.setTitle("공부중");
        book.setAuthor("열공다방 46번 자리");
        book.setIsbn("11111111");
        book.setPublishDate(LocalDate.of(2025,05,15));
        book.setPrice(17500);
        Book saveBook = bookRepository.save(book);

        //When
        saveBook.setPrice(20000);
        Book updateBook = bookRepository.save(saveBook);

        //Then
        assertThat(updateBook.getPrice()).isEqualTo(20000);
        assertThat(saveBook.getPrice()).isEqualTo(20000);

    }


    @Test
//    @Rollback(value = false)
    @Disabled
    void testDeleteBook() {
        /**기존 코드*/
        //Optional<Book> findByIsbn(String isbn);
        //List<Book> findByAuthor(String author);
//        Book book1 = bookRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("Not Found Book"));
//        Book book2 = bookRepository.findById(2L)
//                .orElseThrow(() -> new RuntimeException("책 없어요"));
//
//        bookRepository.delete(book1);
//        bookRepository.delete(book2);

        /**강사님 코드*/
        bookRepository.deleteAll();

        //Given
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("202505150211");
        book.setPublishDate(LocalDate.of(2025,05,15));
        book.setPrice(20000);
        Book saveBook = bookRepository.save(book);

        //When
        bookRepository.deleteById(saveBook.getId());

        //Then
        assertThat(bookRepository.findById(saveBook.getId())).isEmpty();
    }


    /*
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

//        출력 :
//          해당 testShowAllBooks() Console Output Result:
//          _____________________________________________________________
//          |title			|author	|isbn			|price	|publishDate|
//          -------------------------------------------------------------
//          |스프링 부트 입문	|홍길동	|9788956746425	|270000	|2025-05-07	|
//          -------------------------------------------------------------
//          |JPA 프로그래밍	|박둘리	|9788956746432	|35000	|2025-04-30	|
//          -------------------------------------------------------------


    }
    */



}