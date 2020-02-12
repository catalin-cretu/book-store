package com.xyz.bookstore.books;

import com.xyz.bookstore.books.repository.AuthorEntity;
import com.xyz.bookstore.books.repository.AuthorRepository;
import com.xyz.bookstore.books.repository.BookRepository;
import com.xyz.bookstore.books.repository.CategoryEntity;
import com.xyz.bookstore.books.repository.CategoryRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class BooksControllerTests {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private CategoryRepository categoryRepository;

  private AuthorEntity defaultAuthorEntity;
  private CategoryEntity defaultCategoryEntity;

  @BeforeAll
  static void setup() {
    RestAssured.reset();

    RestAssured.port = 8080;
    RestAssured.basePath = "/api";

    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeEach
  @Transactional
  void setupDefaults() {
    if (defaultAuthorEntity == null) {
      defaultAuthorEntity = authorRepository.saveAndFlush(Populated.authorEntity());
      defaultCategoryEntity = categoryRepository.saveAndFlush(Populated.categoryEntity());
    }
  }

  @Nested
  @DisplayName("GET")
  class Get {

    @Test
    @DisplayName("/books - returns all books")
    void getAll() {
      get("/books")
          .then().assertThat()
          .body("$", empty());

      createPopulatedBook();
      createPopulatedBook();
      createPopulatedBook();

      get("/books")
          .then().assertThat()

          .statusCode(OK.value())
          .body("$", hasSize(3));
    }

    @Test
    @DisplayName("/books/{id} - missing ID - returns not found")
    void getOneMissing() {
      get("/books/-443")
          .then().assertThat()

          .statusCode(NOT_FOUND.value())
          .body("message", is("Cannot find book with ID: -443"));
    }

    @Test
    @DisplayName("/books/{id} - by ID - returns book with ID")
    void getOne() {
      var authorEntity = Populated.authorEntity("GRR Martin");
      authorEntity = authorRepository.saveAndFlush(authorEntity);
      var categoryEntities = Populated.categoryEntities("fantasy", "thriller");
      categoryEntities = categoryRepository.saveAll(categoryEntities);

      var book = bookRepository.save(Populated.bookEntity()
          .name("Ice and Fire")
          .isbn("12AA")
          .author(authorEntity)
          .categories(categoryEntities)
          .build());
      var bookId = book.getId();

      get("/books/{id}", bookId)
          .then().assertThat()

          .statusCode(OK.value())
          .body("id", is(bookId.intValue()))
          .body("isbn", is("12AA"))
          .body("name", is("Ice and Fire"))
          .body("author.id", is(authorEntity.getId().intValue()))
          .body("author.name", is("GRR Martin"))
          .body("categories.title", hasItems("thriller", "fantasy"));
    }
  }

  private void createPopulatedBook() {
    var book = Populated.bookEntity()
        .author(defaultAuthorEntity)
        .categories(List.of(defaultCategoryEntity))
        .build();

    bookRepository.save(book);
  }
}
