package com.rookies3.myspringbootlab.controller;


import com.rookies3.myspringbootlab.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.repository.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books") //전체 경로의 기본 prefix 주고 시작함
@RequiredArgsConstructor //Lombok이 final로 정의된 생성자를 자동으로 만들어 줌
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

    //전체 도서 조회
    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    //id로 도서 조회
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
                .map(ResponseEntity::ok) // .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    //ISBN으로 도서 조회
    /**강사님 코드*/
    //@GetMapping("/isbn/{isbn}")
    //public Book getBookByIsbn(@PathVariable String isbn) {
    //    return bookRepository.findByIsbn(isbn)
    //            .orElseThrow(() -> new BusinessException("Book Not Found",HttpStatus.NOT_FOUND));
    //}
    /**기존 코드*/
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn){
        return bookRepository.findByIsbn(isbn) //Optional<Book> 반환 //값이 존재하면 Optional.of(Book), 존재하지 않으면 Optional.empty() 반환
                //.map(book -> ResponseEntity.ok(book));
                // book -> ResponseEntity.ok(book) //body는 book, 상태코드는 200
                // 값이 없다면(Optinal.empty()==true)일경우 다음 메서드로 넘어감
                .map(ResponseEntity::ok)
                // 값이 없다면 404 반환
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /** Postman 인코딩이 달라서 한글 문자열을 영어로 바꾸니 성공*/




    //author로 도서 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author){
        return bookRepository.findByAuthor(author);
    }

    //도서 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        /**기존 코드*/
        //return new ResponseEntity<>(
        //      bookRepository.save(book), //body에 저장한 book 인스턴스가 들어가고
        //      HttpStatus.CREATED //HttpStatus.CREATED: HTTP 상태코드:201//요청에 의해 리소스가 성공적으로 생성되었음
        // );

        /**강사님 코드*/
        Book savedBook = bookRepository.save(book);
        ResponseEntity<Book> result = new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        return result;

    }

    /**기존 코드*/
    //성공
    // 보통 Service에서 @Transactional을 써서 하나라도 틀리면 전체 롤백, 모두 성공해야지만 커밋되게 함
    //도서 업데이트
    @PutMapping("/new/{id}")
    public ResponseEntity<Book> updateNewBook(@PathVariable Long id, @RequestBody Book book){
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

    /**강사님 코드*/
    @PatchMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        return bookRepository.findById(id)
                .map(updateBook -> {
                    updateBook.setPrice(book.getPrice());

                    Book saveBook = bookRepository.save(updateBook);

                    return new ResponseEntity(saveBook, headers, HttpStatus.CREATED);
                })
                //.orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
                .orElseGet(() -> {
                    Book updateBook = new Book();

                    updateBook.setTitle(book.getTitle());
                    updateBook.setAuthor(book.getAuthor());
                    updateBook.setIsbn(book.getIsbn());
                    updateBook.setPublishDate(book.getPublishDate());
                    updateBook.setPrice(book.getPrice());

                    Book saveBook = bookRepository.save(updateBook);
                    return new ResponseEntity<>(saveBook, headers, HttpStatus.CREATED);
                });

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        /**기존 코드*/
        //ResponseEntity<Void> voidResponseEntity =
        //        bookRepository.findById(id)
        //                .map(book -> {
        //                    bookRepository.delete(book);
        //                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        //                })
        //                .orElse(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
        //return voidResponseEntity;
        /**강사님 코드*/
        if(!bookRepository.existsById(id)) { //매핑되는 Book이 존재하지 않으면
            return ResponseEntity.notFound().build(); //상태코드 404 반환
        }
        //매핑되는 Book이 존재하면
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //상태코드 204 반환
    }


}
