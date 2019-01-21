package com.task.library.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.task.library.model.Book;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();
    List<Book> findAllByAuthor(Long author);
    Book findByTitle(String title);
    @Transactional
    void deleteBookByBookId(long id);
    @Override <S extends Book> S save(S s);
}
