package com.task.library.service;


import com.task.library.model.Author;
import com.task.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public long getAuthorId(String name) {
        return authorRepository.findAuthorByName(name).getAuthorId();
    }

    public List<Author> getAllAuthors() {return authorRepository.findAll();}

    public Author getAuthorById(long id) {
        return authorRepository.findAuthorByAuthorId(id);
    }
}
