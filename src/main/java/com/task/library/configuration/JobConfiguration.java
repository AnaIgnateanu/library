package com.task.library.configuration;

import java.util.HashMap;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.task.library.data.Database;
import com.task.library.data.DatabaseThreadContext;
import com.task.library.model.Book;
import com.task.library.model.BookFieldSetMapper;
import com.task.library.repository.BookRepository;

@Configuration
@Aspect
@EnableBatchProcessing
public class JobConfiguration {

    private static final HashMap<String, String> OVERRIDDEN_BY_EXPRESSION = null;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
   


    @Bean
    @StepScope
    public FlatFileItemReader<Book> bookCsvReader(@Value("#{jobParameters}") HashMap<String, String> jobParameters) {
        FlatFileItemReader<Book> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new FileSystemResource(jobParameters.get("path")));

        DefaultLineMapper<Book> BookLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"title", "ISBN", "author"});

        BookLineMapper.setLineTokenizer(tokenizer);
        BookLineMapper.setFieldSetMapper(new BookFieldSetMapper());
        BookLineMapper.afterPropertiesSet();

        itemReader.setLineMapper(BookLineMapper);
        return itemReader;
    }

    @Bean
    public ItemWriter<Book> bookDbItemWriter() {
        DatabaseThreadContext.setCurrentDatabase(Database.SECONDARY);
        return list -> {
            for (Book book : list) {
                bookRepo.save(book);
            }
        };
    }

    @Bean
    public ItemWriteListener<Book> writeInDbListener() {
        return new ItemWriteListener<Book>() {
        	@Override
        	public void afterWrite(List<? extends Book> arg0) {
        		System.out.println("Succesfully written to database!");
        		
        	}
        	@Override
        	public void beforeWrite(List<? extends Book> arg0) {
        		System.out.println("Begining writing to database");
        		
        	}
        	@Override
        	public void onWriteError(Exception arg0, List<? extends Book> arg1) {
        		System.out.println("Writing to database failed");
        		
        	}
		};
    }


    private Step stepReadCsvBook() {
        return stepBuilderFactory.get("stepReadCsvBook")
                .<Book, Book>chunk(10)
                .reader(bookCsvReader(OVERRIDDEN_BY_EXPRESSION))
                .writer(bookDbItemWriter())
                .listener(writeInDbListener())
                .build();
    }


    @Bean
    public Job jobReadCsvBook() {
        return jobBuilderFactory.get("jobReadCsvBook")
                .start(stepReadCsvBook())
                .build();
        //.start(stepInitial())
        // .next(decision())
//                .on(FlowExecutionStatus.COMPLETED.getName()).to(stepReadCsvBook())
//                .build().build();
    }

//    public ReadCsvDecider decision() {
//        return new ReadCsvDecider();
//    }
//
//    private Step stepInitial() {
//        return stepBuilderFactory.get("step1")
//                .tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED).build();
//    }

}


