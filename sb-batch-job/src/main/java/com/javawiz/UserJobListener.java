package com.javawiz;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javawiz.dto.UserEntity;
import com.javawiz.repository.UserRepository;

@Component
public class UserJobListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(UserJobListener.class);

	@Autowired
    private UserRepository repository;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.debug("!!! JOB FINISHED! Time to verify the results");
			List<UserEntity> users = repository.findAll();
			users.forEach(user -> log.info("Found <" + user + "> in the database."));
			repository.deleteAll();
		}
	}
}