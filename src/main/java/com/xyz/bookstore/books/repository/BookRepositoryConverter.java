package com.xyz.bookstore.books.repository;

import com.xyz.bookstore.books.service.Author;
import com.xyz.bookstore.books.service.Book;
import com.xyz.bookstore.books.service.Category;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class BookRepositoryConverter {

  public static BookEntity toBookEntity(final Book book) {
    List<Category> categories = book.getCategories() != null ? book.getCategories() : emptyList();

    return BookEntity.builder()
        .isbn(book.getIsbn())
        .name(book.getName())
        .author(toAuthor(book.getAuthor()))
        .categories(toCategoryEntities(categories))
        .build();
  }

  private static AuthorEntity toAuthor(final Author author) {
    return AuthorEntity.builder()
        .name(author.getName())
        .build();
  }

  private static List<CategoryEntity> toCategoryEntities(final List<Category> categories) {
    return categories.stream()
        .map(BookRepositoryConverter::toCategoryEntity)
        .collect(toList());
  }

  private static CategoryEntity toCategoryEntity(final Category category) {
    return CategoryEntity.builder()
        .title(category.getTitle())
        .build();
  }
}
