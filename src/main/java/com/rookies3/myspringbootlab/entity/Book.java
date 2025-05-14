package com.rookies3.myspringbootlab.entity;

import jakarta.persistence.*;

//import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
//import lombok.Builder;
//import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

//@Builder
@Entity
@Table(name = "books")
@Getter @Setter
@DynamicUpdate
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private LocalDate publishDate;



    public Book() {}

    public Book(String title, String author, String isbn, LocalDate publishDate, Integer price){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.price = price;
    }
}
