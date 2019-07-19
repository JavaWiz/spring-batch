package com.javawiz;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.javawiz.dto.User;
import com.javawiz.dto.UserEntity;
import com.javawiz.processor.UserItemProcessor;
import com.javawiz.repository.UserRepository;
import com.javawiz.repository.UserRowMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
    private UserJobListener listener;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	UserRepository repository;

	@Bean(destroyMethod="")
	public JdbcCursorItemReader<User> reader() {
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT id,name FROM user");
		reader.setRowMapper(new UserRowMapper());
		return reader;
	}

	@Bean
    public ItemProcessor<User, UserEntity> processor() {
        return new UserItemProcessor();
    }
	
	/*
	@Bean
	public FlatFileItemWriter<User> writer() {
		FlatFileItemWriter<User> writer = new FlatFileItemWriter<User>();
		writer.setResource(new ClassPathResource("users.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<User>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<User>() {
					{
						setNames(new String[] { "id", "name" });
					}
				});
			}
		});

		return writer;
	}*/
	
	@Bean
	public RepositoryItemWriter<UserEntity> userWriter() {
		return new RepositoryItemWriterBuilder<UserEntity>().methodName("save").repository(repository).build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, UserEntity>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(userWriter()).build();
	}

	@Bean
	public Job exportUserJob() {
		return jobBuilderFactory.get("exportUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1()).end().build();
	}

}