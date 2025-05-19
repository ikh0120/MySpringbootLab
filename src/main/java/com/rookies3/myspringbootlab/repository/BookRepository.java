package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b FROM Book b JOIN FETCH b.BookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(Long id);

    @Query("SELECT b FROM Book b JOIN FETCH b.BookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(String isbn);

    boolean existsByIsbn(String isbn);
}
