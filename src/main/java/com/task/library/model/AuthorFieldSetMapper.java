package com.task.library.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class AuthorFieldSetMapper implements FieldSetMapper<Author> {

    @Override
    public Author mapFieldSet(FieldSet fieldSet) {
        return new Author(fieldSet.readLong("authorId"),
                fieldSet.readString("name"));
    }
}
