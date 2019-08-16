package com.javawiz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.javawiz.entity.UserDetails;

public class UserDetailsItemProcessor implements ItemProcessor<UserDetails, UserDetails>{
	private static final Logger log = LoggerFactory.getLogger(UserDetailsItemProcessor.class);
	@Override
	public UserDetails process(UserDetails item) throws Exception {
        UserDetails transformedUser = UserDetails.builder()
        		.userId(item.getUserId())
        		.username(item.getUsername().toUpperCase())
        		.firstName(item.getFirstName().toUpperCase())
        		.lastName(item.getLastName().toUpperCase())
        		.gender(item.getGender().toUpperCase())
        		.build();
        log.info("Converting ( {} ) into ({})", item, transformedUser);
		return transformedUser;
	}
}