package com.xyz.bookstore.books.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BookNotFoundException extends ResponseStatusException {

  public BookNotFoundException(final Long bookId) {
    super(NOT_FOUND, "Cannot find book with ID: " + bookId);
  }
}
