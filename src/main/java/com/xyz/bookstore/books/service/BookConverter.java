package com.xyz.bookstore.books.service;

import com.xyz.bookstore.books.repository.BookEntity;
import com.xyz.bookstore.books.repository.CategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class BookConverter {

  public static Book toBook(final BookEntity bookEntity) {
    var authorEntity = bookEntity.getAuthor();

    return Book.builder()
        .id(bookEntity.getId())
        .isbn(bookEntity.getIsbn())
        .name(bookEntity.getName())
        .author(new Author(authorEntity.getId(), authorEntity.getName()))
        .categories(toCategories(bookEntity.getCategories()))
        .build();
  }

  private static List<Category> toCategories(final List<CategoryEntity> categories) {
    return categories.stream()
        .map(BookConverter::toCategory)
        .collect(toList());
  }

  private static Category toCategory(final CategoryEntity categoryEntity) {
    return new Category(categoryEntity.getId(), categoryEntity.getTitle());
  }
}
