package com.xyz.bookstore.books;

import com.xyz.bookstore.books.repository.AuthorEntity;
import com.xyz.bookstore.books.repository.BookEntity;
import com.xyz.bookstore.books.repository.CategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@UtilityClass
public class Populated {

  public static BookEntity.BookEntityBuilder bookEntity() {
    return BookEntity.builder()
        .isbn(randomAlphanumeric(10))
        .name(randomAlphanumeric(30))
        .author(authorEntity("author"))
        .categories(categoryEntities("fantasy", "crime"));
  }

  public static AuthorEntity authorEntity(final String authorName) {
    return new AuthorEntity(null, authorName);
  }

  public static List<CategoryEntity> categoryEntities(final String... categories) {
    return Arrays.stream(categories)
        .map(title -> new CategoryEntity(null, title))
        .collect(toList());
  }

  public static AuthorEntity authorEntity() {
    return new AuthorEntity(null, randomAlphanumeric(20));
  }

  public static CategoryEntity categoryEntity() {
    return new CategoryEntity(null, randomAlphanumeric(12));
  }
}
