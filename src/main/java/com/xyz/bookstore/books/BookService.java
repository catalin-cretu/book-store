package com.xyz.bookstore.books;

import com.xyz.bookstore.books.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.xyz.bookstore.books.repository.BookRepositoryConverter.toBookEntity;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;

  @Transactional
  public void create(final Book book) {
    var bookEntity = toBookEntity(book);

    bookRepository.save(bookEntity);
  }

  public List<Book> findAll() {
    return bookRepository.findAll()
        .stream()
        .map(BookConverter::toBook)
        .collect(toList());
  }
}
