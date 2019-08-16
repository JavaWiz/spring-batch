package com.javawiz.configuration;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.javawiz.entity.CronEntity;
import com.javawiz.repository.CronRepository;

@Configuration
@EnableScheduling
public class JobScheduler {
	private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;
	
	@Autowired
	private CronRepository cronRepo;

	@Bean
	public String getCronValue(){
		CronEntity c = cronRepo.findById(1l).get();
		String expression = c.getCronXpression();
		log.debug("cron expression -> {}", expression);
	    return expression;
	}
	
	//batch job will run every one minute after application is started.
	//@Scheduled(cron = "0 */1 * * * ?")
	@Scheduled(cron="#{@getCronValue}")
	public void run() throws Exception {

		log.debug("Job Started at : {} ", new Date());

		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		JobExecution execution = jobLauncher.run(job, param);

		log.debug("Job finished with status : {}", execution.getStatus());
	}
}