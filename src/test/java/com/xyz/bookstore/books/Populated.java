package com.xyz.bookstore.books;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Populated {

  public static Book book() {
    return Book.builder()
        .build();
  }
}
