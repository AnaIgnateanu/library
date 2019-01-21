package com.task.library.controller;

import com.task.library.model.Author;
import com.task.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class AuthorController {
        @Autowired
        private AuthorService authorService;

    @RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @RequestMapping(value = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Author getAuthorById(@PathVariable(value = "id") long id) {
        return authorService.getAuthorById(id);
    }
}
