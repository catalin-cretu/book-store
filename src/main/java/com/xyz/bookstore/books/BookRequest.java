package com.xyz.bookstore.books;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BookRequest {
  String isbn;
  String name;
  String authorName;
  List<String> categories;
}
