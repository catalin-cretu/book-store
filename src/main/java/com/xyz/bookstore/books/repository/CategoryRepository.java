package com.xyz.bookstore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  @Query("select cat from CategoryEntity cat where title in :titles")
  List<CategoryEntity> findAllByTitles(@Param("titles") final List<String> titles);
}
