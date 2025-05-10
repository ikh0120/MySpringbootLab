package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Query Method
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthor(String author);
    Optional<Book> findByTitle(String title);
}
