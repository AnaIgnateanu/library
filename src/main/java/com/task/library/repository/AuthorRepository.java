package com.task.library.repository;

import com.task.library.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findAuthorByName(String name);
    List<Author> findAll();
    Author findAuthorByAuthorId(long id);
}
