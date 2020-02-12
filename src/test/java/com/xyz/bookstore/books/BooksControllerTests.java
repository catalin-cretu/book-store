package com.xyz.bookstore.books;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class BooksControllerTests {

  @Autowired
  private BookService bookService;

  @BeforeAll
  static void setup() {
    RestAssured.reset();

    RestAssured.port = 8080;
    RestAssured.basePath = "/api";

    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  @DisplayName("GET")
  class Get {

    @Test
    @DisplayName("/books - returns all books")
    void getAll() {
      bookService.create(Populated.book());
      bookService.create(Populated.book());
      bookService.create(Populated.book());

      get("/books")
          .then().assertThat()

          .statusCode(OK.value())
          .body("id", hasSize(3));
    }
  }
}
