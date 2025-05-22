package com.rookies3.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="book_details")
@NoArgsConstructor //기본 생성자 자동 생성: public BookDetail() { }
@AllArgsConstructor //모든 필드를 매개변수로 받는 생성자 자동 생성: public BookDetail(...) {...}
@Builder
@Getter @Setter
public class BookDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_detail_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "edition")
    private String edition;

    //FK(Foreign Key)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;
}
