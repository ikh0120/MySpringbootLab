package com.rookies3.myspringbootlab.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.repository.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    //Book Json 데이터 삽입
    @PostMapping("/library")
    public void addAllBooks(@RequestBody List<Book> books){ //@RequestBody: 내가 직접 넣는 변수 혹은 객체
        for(Book book : books){
            bookRepository.save(book);
            System.out.println("id: "+book.getId()+" book save!");
        }
    }


    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        /*
        Optional<Book> optBook = bookRepository.findById(id);

        ResponseEntity<Book> respEntBook = optBook
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK)) //optBook에 데이터가 존재할 때
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // optBook에 데이터가 없을 때


        return respEntBook;
         */
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn){
        return bookRepository
                .findByIsbn(isbn)
                .map(ResponseEntity::ok)//.map(book -> ResponseEntity.ok(book));
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Postman 인코딩이 달라서 한글 문자열을 영어로 바꾸니 성공
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author){
        return  bookRepository.findByAuthor(author);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        ResponseEntity<Book> reb = new ResponseEntity<>(bookRepository.save(book), HttpStatus.IM_USED);
        System.out.println(reb.toString());
        return reb;
    }

    //성공
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book){
//        book.setId(id);
        return bookRepository.findById(id)
                //optBook이라는 OptionalContainer에 Book 객체가 있다면
                .map(updateBook -> { // 해당 객체에 입력받은 book 인스턴스의 id를 제외한 모든 값을 덮어써라
                        updateBook.setTitle(book.getTitle());
                        updateBook.setAuthor(book.getAuthor());
                        updateBook.setIsbn(book.getIsbn());
                        updateBook.setPublishDate(book.getPublishDate());
                        updateBook.setPrice(book.getPrice());

                        Book finishUpdateBook = bookRepository.save(updateBook);

                        return new ResponseEntity<>(finishUpdateBook, HttpStatus.OK);
                })
                .orElseGet(() -> { // id값이 일치하는 Book 객체가 없을 때
                            // 새로운 Book 객체를 만들어 메서드 파라미터로 입력받은 Book 객체의 인스턴스를 넣고
                            Book newBook = new Book(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishDate(), book.getPrice());
                            // 저장하고 그 값을 반환 객체에 넣어라
                            Book addNewBook = bookRepository.save(newBook);
                            // ResponseEntity<Book>(addNewBook, HttpStatus.CREATED)를 반환한다
                            return new ResponseEntity<>(addNewBook, HttpStatus.CREATED);
                });
    }

    //성공
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){

        ResponseEntity<Void> voidResponseEntity =
                bookRepository.findById(id)
                        .map(book -> {
                            bookRepository.delete(book);
                            return new ResponseEntity<Void >(HttpStatus.NO_CONTENT);
                        })
                        .orElse(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

        return voidResponseEntity;

    }


}
