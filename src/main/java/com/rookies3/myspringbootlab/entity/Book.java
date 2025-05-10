package com.rookies3.myspringbootlab.entity;

import jakarta.persistence.*;

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
    private Long id;

    private String title;
    private String author;

    @Column(unique = true)
    private String isbn;

    private LocalDate publishDate;
    private Integer price;

    public Book() {}

    public Book(String title, String author, String isbn, LocalDate publishDate, Integer price){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.price = price;
    }
}
