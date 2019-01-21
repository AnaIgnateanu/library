package com.task.library.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.task.library.data.Database;
import com.task.library.data.DatabaseThreadContext;
import com.task.library.model.Book;
import com.task.library.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Job jobReadCsvBook;

    public List<Book> getBookByAuthorId(long author) {
        return bookRepository.findAllByAuthor(author);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByBookId(long id) { return bookRepository.findOne(id);}

    public Book saveSingleBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(long id) {
        bookRepository.deleteBookByBookId(id);}


    public boolean saveBooks(MultipartFile multipartFile) {
        DatabaseThreadContext.setCurrentDatabase(Database.SECONDARY);

        try {
            String path = request.getServletContext().getRealPath("/uploads/");
            File file = new File(path);
            if (!new File(file.getPath()).exists()) {
                new File(file.getPath()).mkdir();
            }
            path = path + multipartFile.getOriginalFilename();
            file = new File(path);
            multipartFile.transferTo(file);

            try {
                JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
                jobParametersBuilder.addString("path", path);
                jobParametersBuilder.addLong("time", System.currentTimeMillis());
                jobLauncher.run(jobReadCsvBook, jobParametersBuilder.toJobParameters());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

   // @Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            jobLauncher.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobLauncher;
    }

}


