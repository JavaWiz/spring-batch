package com.javawiz.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.javawiz.entity.UserDetails;
import com.javawiz.repository.UserDetailsRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
    private UserJobListener listener;

	/*
	 * @Autowired private DataSource dataSource;
	 */
	
	@Autowired
	private UserDetailsRepository repository;
	
	@Qualifier("userDetailsExcelWriter")
	@Autowired
	private UserDetailsExcelWriter userDetailsExcelWriter;
	
	/*
	 * @Bean public StepScope stepScope() { final StepScope stepScope = new
	 * StepScope(); stepScope.setAutoProxy(true); return stepScope; }
	 */
	
	private Resource outputResource = new FileSystemResource("output/outputData.csv");
	
	@Bean
	public RepositoryItemReader<UserDetails> reader() {
		final Map<String, Sort.Direction> sorts = new HashMap<>();
	    sorts.put("userId", Direction.ASC);
		RepositoryItemReader<UserDetails> reader = new RepositoryItemReaderBuilder<UserDetails>().repository(repository)
				.sorts(sorts)
				.saveState(false)
				.methodName("findAll").build();
		return reader;
	}
	
	/*
	@Bean(destroyMethod="")
	public JdbcCursorItemReader<UserDetails> reader() {
		JdbcCursorItemReader<UserDetails> reader = new JdbcCursorItemReader<UserDetails>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT user_id, username, first_name, last_name, gender FROM user_details");
		reader.setRowMapper(new UserRowMapper());
		return reader;
	}*/

	@Bean
    public ItemProcessor<UserDetails, UserDetails> processor() {
        return new UserDetailsItemProcessor();
    }
	
	@Bean
	public FlatFileItemWriter<UserDetails> csvWriter() {
		FlatFileItemWriter<UserDetails> writer = new FlatFileItemWriter<UserDetails>();
		writer.setResource(outputResource);
		writer.setLineAggregator(new DelimitedLineAggregator<UserDetails>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<UserDetails>() {
					{
						setNames(new String[] { "userId", "username", "firstName", "lastName", "gender" });
					}
				});
			}
		});

		return writer;
	}
	
	@Bean
	public Step step(ItemReader<UserDetails> reader, ItemProcessor<UserDetails, UserDetails> processor, ItemWriter<UserDetails> writer) {
		return stepBuilderFactory.get("step")
				.<UserDetails, UserDetails>chunk(10)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.listener(listener())
				.build();
	}

	@Bean
	public Job exportUserDetailsJob() {
		return jobBuilderFactory
		          .get("chunksJob")
		          .listener(listener)
		          .start(step(reader(), processor(), userDetailsExcelWriter))
		          .build();
		/*
		 * return jobBuilderFactory.get("exportJob") .incrementer(new
		 * RunIdIncrementer()) .listener(listener) .flow(step()).end().build();
		 */
	}
	
	@Bean
	public ItemCountListener listener() {
	    return new ItemCountListener();
	}
}