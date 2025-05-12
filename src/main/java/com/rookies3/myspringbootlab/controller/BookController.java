package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.entity.Book;

import com.rookies3.myspringbootlab.exception.BookNotFoundException;
import com.rookies3.myspringbootlab.repository.*;
//import com.rookies3.myspringbootlab.repository.BookRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    /** 1. POST /api/books: 새 도서 등록 */
    @PostMapping
    public Book create(@RequestBody Book book){
        return bookRepository.save(book);
        //기존 src/test/java의
        //  com.rookies3.myspringbootlab 패키지의
        //      .repository.BookRepositoryTest.java
        //
        // 파일에서 생성한 books 테이블의 레코드를 isbn 컬럼이 unique=true로 선언되어 중복값을 방지하기 위해
        // delete from books; 쿼리로 books 테이블의 값 전체를 삭제한 뒤 진행했습니다.
        // 혹시 몰라 repository.BookRepositoryTest.testShowAllBooks() 메서드를 선언 후 결과값을 주석으로 첨부했습니다.
        //
        // 이후
        // Postman으로 HTTP Method를 POST로 선택 후 텍스트필드에 http://localhost:8080/api/books를 삽입했습니다
        // 그리고 바로 밑 Headers 메뉴의
        // Key 컬럼에는 Content-Type을
        // Value 컬럼에는 applicaiton/json을 넣고
        // 주어진 JSON 데이터를 Body 메뉴의 라디오버튼 raw 선택 후
        // Text라 주어진 드롭박스를 JSON으로 변환 후
        // JSON 데이터를 하나하나 넣고 SEND 버튼을 클릭했습니다.
        // 결과값은 MySpringBootLabApplication 콘솔에서
        //      Hibernate: insert into books (author,isbn,price,publish_date,title) values (?,?,?,?,?)
        //      Hibernate: insert into books (author,isbn,price,publish_date,title) values (?,?,?,?,?)
        // 두 줄이 출력되었으며
        // lab_db라는 데이터베이스의 테이블 books에도
        //    MariaDB [lab_db]> select * from books;
        //    +-------+--------------+----+-----------+---------------+-------------------------+
        //    | price | publish_date | id | author    | isbn          | title                   |
        //    +-------+--------------+----+-----------+---------------+-------------------------+
        //    | 30000 | 2025-05-07   |  4 | 홍길동    | 9788956746425 | 스프링 부트 입문        |
        //    | 35000 | 2025-04-30   |  5 | 박둘리    | 9788956746432 | JPA 프로그래밍          |
        //    +-------+--------------+----+-----------+---------------+-------------------------+
        //    2 rows in set (0.001 sec)
        // 값이 삽입된 것을 확인할 수 있었습니다.
    }

    /** 2. GET /api/books: 모든 도서 조회 */
    @GetMapping
    public List<Book> findAllBooks(){
        return bookRepository.findAll();
        // MySpringbootLabApplication 서버를 재시작하지 않아 계속해서 405에러가 생겼습니다
        // Postman 반환값:
//            [
//                {
//                    "id": 4,
//                    "title": "스프링 부트 입문",
//                    "author": "홍길동",
//                    "isbn": "9788956746425",
//                    "publishDate": "2025-05-07",
//                    "price": 30000
//                },
//                {
//                    "id": 5,
//                    "title": "JPA 프로그래밍",
//                    "author": "박둘리",
//                    "isbn": "9788956746432",
//                    "publishDate": "2025-04-30",
//                    "price": 35000
//                }
//            ]
    }

    /** 3. GET /api/books/{id}: ID로 특정 도서 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<Book> findByBookForId(@PathVariable Long id){
        Optional<Book> optBook = bookRepository.findById(id);
//        return optBook.map(boook -> ResponseEntity.ok(boook))
//                .orElse(ResponseEntity.notFound().build());
        return optBook.map(ResponseEntity::ok)
                .orElse(new ResponseEntity("Book Not Found", HttpStatus.NOT_FOUND));

        //Postman을 사용해서 Http Get 요청으로 http://127.0.0.1:8080/api/books/5를 보낸 결과
        /* 이러한 결과가 출력되고
            {
                "id": 5,
                "title": "JPA 프로그래밍",
                "author": "박둘리",
                "isbn": "9788956746432",
                "publishDate": "2025-04-30",
                "price": 35000
            }
         */
        //Postman Http Get 요청으로 http://127.0.0.1:8080/api/books/123을 보낸 결과
        /* Book Not Found 출력 */
    }

    /** 3. GET /api/books/{id}: ID로 특정 도서 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<Book> findByBookForIdNew(@PathVariable Long id){
        return bookRepository.findById(id).map(ResponseEntity::ok).orElse(new ResponseEntity("Book Not Found", HttpStatus.NOT_FOUND));
    }

    /** 4. GET /api/books/isbn/{isbn}: ISBN으로 도서 조회 */
    @GetMapping("/isbn/{isbn}")
    public Book findByBookForIsbn(@PathVariable String isbn){
        Optional<Book> optIsbnBook = bookRepository.findByIsbn(isbn);

        return optIsbnBook
                .orElseThrow(() -> new BookNotFoundException("책이 없습니다!", HttpStatus.NOT_FOUND));
        /** isbn을 lab_db의 books 테이블의 isbn 컬럼값을 넣었을 때
        *  http://localhost:8080/api/books/isbn/9788956746425
        */
        /* 출력값(JSON):
            {
                "id": 4,
                "title": "스프링 부트 입문",
                "author": "홍길동",
                "isbn": "9788956746425",
                "publishDate": "2025-05-07",
                "price": 30000
            }
        */

        /** isbn을 lab_db의 books 테이블의 isbn 컬럼값을 넣었을 때
        *  http://localhost:8080/api/books/isbn/1234567890
        */
        /* 출력값(JSON):
            {
                "timestamp": "2025-05-12T14:56:39.407+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "trace": "com.rookies3.myspringbootlab.exception.BookNotFoundException: 책이 없습니다!\r\n\tat com.rookies3.myspringbootlab.controller.BookController.lambda$findByBookForIsbn$0(BookController.java:118)\r\n\tat java.base/java.util.Optional.orElseThrow(Optional.java:403)\r\n\tat com.rookies3.myspringbootlab.controller.BookController.findByBookForIsbn(BookController.java:118)\r\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:258)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:191)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:986)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:891)\r\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:114)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:483)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:116)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:398)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:903)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1740)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1189)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:658)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n\tat java.base/java.lang.Thread.run(Thread.java:833)\r\n",
                "message": "책이 없습니다!",
                "path": "/api/books/isbn/1234567890"
            }
        */
    }

    /** 5. PUT /api/books/{id}: 도서 정보 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<String> ModifyBook(@PathVariable Long id){
        Optional<Book> optModBook = bookRepository.findById(id);
        Book modBook = optModBook.orElseThrow(() ->
                new BookNotFoundException("Book.id: " + id + "   Book Not Found!", HttpStatus.NOT_FOUND));

        Integer beforePrice = modBook.getPrice();

        modBook.setPrice(modBook.getPrice() * 10);

        Integer afterPrice = modBook.getPrice();

        bookRepository.save(modBook);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Book Id: " + id + "\nBook Before Price: " + beforePrice + "\nBook After Price: " + afterPrice);

        /** books 테이블의 id 컬럼의 값이 4인 레코드 존재 */
        //  MariaDB [lab_db]> select * from books where id=4;
        //  +-------+--------------+----+-----------+---------------+-------------------------+
        //  | price | publish_date | id | author    | isbn          | title                   |
        //  +-------+--------------+----+-----------+---------------+-------------------------+
        //  | 30000 | 2025-05-07   |  4 | 홍길동    | 9788956746425 | 스프링 부트 입문        |
        //  +-------+--------------+----+-----------+---------------+-------------------------+
        //  1 row in set (0.001 sec)
        /**Postman을 사용해서 http://localhost:8080/api/books/4를 HTTP PUT 요청을 했을 때*/
        // Book Id: 4
        // Book Before Price: 30000
        // Book After Price: 300000
        /**HTTP PUT 요청으로 바뀌었나 lab_db의 books 테이블 확인*/
        // MariaDB [lab_db]> select * from books where id=4;
        // +--------+--------------+----+-----------+---------------+-------------------------+
        // | price  | publish_date | id | author    | isbn          | title                   |
        // +--------+--------------+----+-----------+---------------+-------------------------+
        // | 300000 | 2025-05-07   |  4 | 홍길동    | 9788956746425 | 스프링 부트 입문        |
        // +--------+--------------+----+-----------+---------------+-------------------------+
        // 1 row in set (0.001 sec)
        /**HTTP PUT 요청으로 price 컬럼의 값이 30000에서 300000으로 바뀐 것을 확인*/
        /** books 테이블의 id 컬럼이 120인 레코드 없음 */
        // MariaDB [lab_db]> select * from books where id=120;
        // Empty set (0.001 sec)
        /**Postman을 사용해서 http://localhost:8080/api/books/120을 HTTP PUT 요청했을 때 */
        //{
        //    "timestamp": "2025-05-12T15:45:21.347+00:00",
        //        "status": 500,
        //        "error": "Internal Server Error",
        //        "trace": "com.rookies3.myspringbootlab.exception.BookNotFoundException: Book.id: 120   Book Not Found!\r\n\tat com.rookies3.myspringbootlab.controller.BookController.lambda$ModifyBook$1(BookController.java:153)\r\n\tat java.base/java.util.Optional.orElseThrow(Optional.java:403)\r\n\tat com.rookies3.myspringbootlab.controller.BookController.ModifyBook(BookController.java:152)\r\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:258)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:191)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:986)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:891)\r\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doPut(FrameworkServlet.java:925)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:114)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:483)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:116)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:398)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:903)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1740)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1189)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:658)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n\tat java.base/java.lang.Thread.run(Thread.java:833)\r\n",
        //        "message": "Book.id: 120   Book Not Found!",
        //        "path": "/api/books/120"
        //}
    }

    /** 6. DELETE /api/books/{id}: 도서 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        Optional<Book> optDelBook = bookRepository.findById(id);

        Book deleteBook = optDelBook
                .orElseThrow(() -> new BookNotFoundException("books 테이블의 id 컬럼이 " + id + "인 레코드는 존재하지 않습니다. ", HttpStatus.NOT_FOUND));

        String deleteBookTitle = deleteBook.getTitle();

        bookRepository.delete(deleteBook);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("books 테이블의 id 컬럼이 " + id+"인 " + deleteBookTitle + " 책을 삭제했습니다. ");
        /** 전체 테이블 레코드 갯수: 2개 */
        //    MariaDB [lab_db]> select count(*) as bookRecodeCnt from books;
        //    +---------------+
        //    | bookRecodeCnt |
        //    +---------------+
        //    |             2 |
        //    +---------------+
        //    1 row in set (0.001 sec)


        /** 전체 테이블 레코드 초기값: id 컬럼의 값은 4, 5 인것을 알 수 있음 */
        //    MariaDB [lab_db]> select id, title, author, isbn, price, publish_date as publishDate from books;
        //    +----+-------------------------+-----------+---------------+--------+-------------+
        //    | id | title                   | author    | isbn          | price  | publishDate |
        //    +----+-------------------------+-----------+---------------+--------+-------------+
        //    |  4 | 스프링 부트 입문        | 홍길동    | 9788956746425 | 300000 | 2025-05-07  |
        //    |  5 | JPA 프로그래밍          | 박둘리    | 9788956746432 |  35000 | 2025-04-30  |
        //    +----+-------------------------+-----------+---------------+--------+-------------+
        //    2 rows in set (0.000 sec)

        /** id 컬럼을 4나 5가 아닌 다른 Long형 숫자로 삭제 시도해보기 */
        /** Postman에서 HTTP DELETE 요청을 위한 URL: http://localhost:8080/api/books/10 */
        // 결과값


    }
}
