package com.xyz.bookstore.books.service;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class Book {
  Long id;

  String isbn;
  String name;
  Author author;
  List<Category> categories;

  Instant createdTimestamp;
  Instant updatedTimestamp;
}
