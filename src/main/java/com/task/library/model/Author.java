package com.task.library.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "author_id")
    private long authorId;

    private String name;
    
    public Author() {
    	
    }

    public Author(long author_id, String name) {
        this.authorId = author_id;
        this.name = name;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long author) {
        this.authorId = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author {id: " + authorId +
                ", name: "+ name;
    }
}
