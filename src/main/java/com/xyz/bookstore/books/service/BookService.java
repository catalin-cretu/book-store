package com.xyz.bookstore.books.service;

import com.xyz.bookstore.books.exception.BookNotFoundException;
import com.xyz.bookstore.books.repository.BookEntity;
import com.xyz.bookstore.books.repository.BookRepository;
import com.xyz.bookstore.books.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.xyz.bookstore.books.repository.BookRepositoryConverter.toBookEntity;
import static com.xyz.bookstore.books.service.BookConverter.toBook;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final AuthorService authorService;

  public List<Book> findAll() {
    return bookRepository.findAll()
        .stream()
        .map(BookConverter::toBook)
        .collect(toList());
  }

  public Book findById(final Long bookId) {
    var optionalBookEntity = bookRepository.findById(bookId);

    return optionalBookEntity
        .map(BookConverter::toBook)
        .orElseThrow(() -> new BookNotFoundException(bookId));
  }

  @Transactional
  public Book create(final Book book) {
    BookEntity bookEntity = toBookEntity(book);

    findExistingCategories(book, bookEntity);
    findOrCreateAuthor(bookEntity);

    var savedBookEntity = bookRepository.save(bookEntity);
    return toBook(savedBookEntity);
  }

  private void findExistingCategories(final Book book, final BookEntity bookEntity) {
    var titles = book.getCategories()
        .stream()
        .map(Category::getTitle)
        .collect(toList());
    var categoryEntities = categoryRepository.findAllByTitles(titles);

    bookEntity.setCategories(categoryEntities);
  }

  private void findOrCreateAuthor(final BookEntity bookEntity) {
    var authorName = bookEntity.getAuthor().getName();
    var optionalAuthorEntity = authorService.findByName(authorName);

    if (optionalAuthorEntity.isPresent()) {
      bookEntity.setAuthor(optionalAuthorEntity.get());
    } else {
      var authorEntity = authorService.create(authorName);
      bookEntity.setAuthor(authorEntity);
    }
  }
}
