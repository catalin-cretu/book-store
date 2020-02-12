package com.xyz.bookstore.books.service;

import com.xyz.bookstore.books.repository.AuthorEntity;
import com.xyz.bookstore.books.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;

  @Transactional
  public AuthorEntity create(final String authorName) {
    return authorRepository.save(new AuthorEntity(null, authorName));
  }

  public Optional<AuthorEntity> findByName(final String authorName) {
    return authorRepository.findByName(authorName);
  }
}
