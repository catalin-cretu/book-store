package com.xyz.bookstore.books.repository;

import com.xyz.bookstore.books.Book;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookRepositoryConverter {

  public static BookEntity toBookEntity(final Book book) {
    return BookEntity.builder().build();
  }
}
