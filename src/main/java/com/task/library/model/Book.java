package com.task.library.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name= "book_id")
    private long bookId;

    private String title;

    private String ISBN;

    public long author;

    public Book() {

    }

    public Book(String title, String isbn, long author) {
        this.title = title;
        this.ISBN = isbn;
        this.author = author;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long book_id) {
        this.bookId = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book {book_id: " + bookId +
                ", title: "+ title +
                ", ISBN: "+ ISBN +
                ", author: "+ author + "}";
    }


}
