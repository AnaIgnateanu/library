package com.task.library.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BookFieldSetMapper implements FieldSetMapper<Book> {

	@Override
	public Book mapFieldSet(FieldSet fieldSet) {
		return new Book(fieldSet.readString("title"),
		fieldSet.readString("ISBN"),
		fieldSet.readLong("author"));
	}
}
