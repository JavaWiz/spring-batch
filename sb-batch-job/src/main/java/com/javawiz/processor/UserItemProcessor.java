package com.javawiz.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.javawiz.dto.User;
import com.javawiz.dto.UserEntity;

public class UserItemProcessor implements ItemProcessor<User, UserEntity>{
	private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);
	@Override
	public UserEntity process(User item) throws Exception {
        final String name = item.getName().toUpperCase();
        UserEntity transformedUser = UserEntity.builder()
        		.uid(item.getId())
        		.name(name)
        		.build();
        log.info("Converting ( {} ) into ({})", item, transformedUser);
		return transformedUser;
	}
}