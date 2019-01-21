package com.task.library.controller;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookDto {
    public Long bookId;
    public String title;
    public String ISBN;
    public String authorId;
    public String author;
}
