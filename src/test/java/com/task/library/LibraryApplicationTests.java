package com.task.library;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.task.library.model.Book;
import com.task.library.repository.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = {"/schema-mysql.sql", "/data-mysql.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.NONE) 
@ContextConfiguration
public class LibraryApplicationTests {
	
	Book book;
	Book book2;
	Book book3;

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	DataSource dataSource;

	@Autowired
	private BookRepository bookRepository;
	
	@Before
	@Rollback(false)
	@Transactional
	public void setup() {
		System.out.println("setup");
		book = new Book("title1", "ISBN1", 1);
		book.setBookId(1);
		book = bookRepository.save(book);
		System.out.println(book);
//		entityManager.persist(book);
//	    entityManager.flush();
//	    System.out.println("setup");
//		 book2 = new Book("title2", "ISBN2", 1);
//		//bookRepository.save(book2);
//		entityManager.persist(book2);
//	    entityManager.flush();
//		 book3 = new Book("title3", "ISBN3", 2);
//		bookRepository.save(book3);
	}
	
	@Test
	public void saveBook() {
		book = new Book("title2", "ISBN2", 1);
		book.setBookId(2);
		book = bookRepository.save(book);
		assertNotNull(book);
		System.out.println(book);
	}
	
	@Test
	public void whenFindByTitle_thenReturnBook() {
		List<Book> books = bookRepository.findAll();
	   Book found = bookRepository.findByTitle(book.getTitle());

	    assertSame("title is the same", found.getTitle(), book.getTitle());
	}

//	@Test
//	public void booksByAuthorCheck() {
//		List<Book> books = bookRepository.findAllByAuthor(1L);
//
//		assertEquals("size incorrect", 2, books.size());
//	}
	

}


 