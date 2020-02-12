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
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
      bookRepository.deleteAll();

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

  @Nested
  @DisplayName("POST")
  class Post {

    @Test
    @DisplayName("/books - missing user/pass - returns unauthorized")
    void createNoAuth() {
      given()
          .body("{}")
          .post("/books")

          .then().assertThat()
          .statusCode(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("/books - creates book")
    void createOne() {
      String location = given()
          .auth().preemptive().basic("admin", "admin")
          .contentType(JSON)
          .body(Map.of(
              "isbn", "9780134686042",
              "name", "Effective Java",
              "authorName", "Joshua Bloch",
              "categories", List.of("non-fiction", "thriller")))
          .post("/books")

          .then().assertThat()
          .statusCode(CREATED.value())
          .header(LOCATION, matchesPattern("/api/books/[0-9]+"))
          .extract().header(LOCATION);

      get("/books/{bookId}", 1)

          .then().assertThat()
          .body("isbn", is("9780134686042"))
          .body("name", is("Effective Java"))
          .body("author.name", is("Joshua Bloch"))
          .body("categories.title", hasItems("non-fiction", "thriller"));
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
