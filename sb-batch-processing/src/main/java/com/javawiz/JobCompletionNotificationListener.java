package com.javawiz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.javawiz.entity.People;
import com.javawiz.repository.PeopleRepository;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	@Autowired
    private PeopleRepository peopleRepository;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.debug("!!! JOB FINISHED! Time to verify the results");
			List<People> people = peopleRepository.findAll();
			people.forEach(person -> log.info("Found <" + person + "> in the database."));
			peopleRepository.deleteAll();
			simpMessagingTemplate.convertAndSend("/topic/message", people);
		}
	}
}
