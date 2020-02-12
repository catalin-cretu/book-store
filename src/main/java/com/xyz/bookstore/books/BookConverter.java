package com.xyz.bookstore.books;

import com.xyz.bookstore.books.repository.BookEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookConverter {

  public static Book toBook(final BookEntity bookEntity) {
    return Book.builder()
        .id(bookEntity.getId())
        .build();
  }
}
