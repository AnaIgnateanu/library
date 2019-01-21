package com.task.library.controller;

import com.task.library.service.AuthorService;
import com.task.library.service.BookService;
import com.task.library.util.GeneratePdf;
import com.task.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @RequestMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @RequestMapping(value = "/books/{id}", produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Book getBookById(@PathVariable(value = "id") long id) {
        Book b = bookService.getBookByBookId(id);
        return b;
    }

    @RequestMapping(value = "/books/download/all" , method = RequestMethod.GET, produces =  MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdfAllBooks() {
        List<Book> books =  getAllBooks();

        ByteArrayInputStream bis = GeneratePdf.getPdf(books, "");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=all_books.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/books/download/author/{author}" , method = RequestMethod.GET, produces =  MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdfBooksByAuthor(@PathVariable(value = "author") String author) {
        List<Book> books =  getBooksByAuthor(author);
        ByteArrayInputStream bis = GeneratePdf.getPdf(books, author);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=books_by_author.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    @RequestMapping(value = "/books/author/{author}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Book> getBooksByAuthor(@PathVariable(value = "author") String author) {
        long author_id = authorService.getAuthorId(author);
        return bookService.getBookByAuthorId(author_id);
    }

    @RequestMapping(value="/books/save", method = RequestMethod.POST)
    public boolean handleFileUpload(@RequestParam("file") MultipartFile file) {
        return bookService.saveBooks(file);
    }

    @PutMapping(value = "/books/{id}")
    public void updateBook(@PathVariable Long id,
                           @RequestBody BookDto dto) {
        Book book = bookService.getBookByBookId(id);
        mapToDto(book, dto);
    }

    @DeleteMapping("/books/{id}")
    public void deleteCourseById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @RequestMapping(value= "/books", method = RequestMethod.POST)
    public void addBook(@RequestBody BookDto dto) {
        bookService.saveSingleBook(mapToEntity(dto));
    }

    private Book mapToEntity(BookDto dto) {
        Book newEntity = new Book();
        mapToDto(newEntity, dto);
        return newEntity;
    }

    private void mapToDto(Book book, BookDto dto) {
        book.setTitle(dto.title);
        book.setISBN(dto.ISBN);
        String authorName = dto.author;
        long authorId = authorService.getAuthorId(authorName);
        book.setAuthor(authorId);
    }

}
