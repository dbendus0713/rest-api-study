package com.myboot.restapi.runner;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.myboot.restapi.events.Event;
import com.myboot.restapi.events.EventRepository;

@Component
public class EventInsertRunner implements ApplicationRunner {
	@Autowired
	EventRepository eventRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		IntStream.range(0, 30).forEach(this::generateEvent);
	}

	private Event generateEvent(int index) {
		Event event = buildEvent(index);
		return this.eventRepository.save(event);
	}

	private Event buildEvent(int index) { 
		return Event.builder()
					.name(++index + "이름")
					.description(index + "설명")
					.beginEnrollmentDateTime(LocalDateTime.of(2021, 2, 3, 10, 8))
					.closeEnrollmentDateTime(LocalDateTime.of(2021, 2, 4, 10, 8))
					.beginEventDateTime(LocalDateTime.of(2021, 2, 10, 10, 8))
					.endEventDateTime(LocalDateTime.of(2021, 2, 11, 10, 8))
					.location(index + "위치")
					.basePrice(100)
					.maxPrice(200)
					.limitOfEnrollment(100)
					.build();
	}
}
