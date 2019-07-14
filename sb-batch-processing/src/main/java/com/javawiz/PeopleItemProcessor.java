package com.javawiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.javawiz.entity.People;

/**
 * 
 * @author javawiz
 * @since 13 July 2019
 *
 */

public class PeopleItemProcessor implements ItemProcessor<People, People> {

    private static final Logger log = LoggerFactory.getLogger(PeopleItemProcessor.class);
	@Override
	public People process(People people) throws Exception {
		final String firstName = people.getFirstName().toUpperCase();
        final String lastName = people.getLastName().toUpperCase();
        People transformedPeople = People.builder()
        		.firstName(firstName).lastName(lastName)
        		.build();
        log.info("Converting ( {} ) into ({})", people, transformedPeople);
		return transformedPeople;
	}
}
