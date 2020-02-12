package com.xyz.bookstore.books;

import com.xyz.bookstore.books.service.Book;
import com.xyz.bookstore.books.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class BooksController {

  private final BookService bookService;

  @GetMapping("/books")
  public List<Book> findAll() {
    log.debug("Find all books");

    return bookService.findAll();
  }

  @GetMapping("/books/{bookId}")
  public Book findById(@PathVariable final Long bookId) {
    log.info("Find book with ID [{}]", bookId);

    return bookService.findById(bookId);
  }
}
