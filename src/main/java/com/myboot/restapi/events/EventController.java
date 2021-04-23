package com.myboot.restapi.events;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myboot.restapi.common.ErrorsResource;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;

	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}

	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		return ResponseEntity.ok(eventResource);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		this.eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		Event existingEvent = optionalEvent.get();
		this.modelMapper.map(eventDto, existingEvent);
		Event savedEvent = this.eventRepository.save(existingEvent);
		EventResource eventResource = new EventResource(savedEvent);
		return ResponseEntity.ok(eventResource);
	}

	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		/*
		 * PagedResourcesAssembler<T> 의 toModel() 메서드 toModel(Page<T> page,
		 * org.springframework.hateoas.server.RepresentationModelAssembler<T,R>
		 * assembler) RepresentationModelAssembler 가 함수형 인터페이스이고 D toModel(T entity) 를
		 * 재정의 할때 람다식을 사용해야 한다.\ Converts the given entity into a D, which extends
		 * RepresentationModel. Event -> EventResource Wrapping 해야 한다.
		 */
		PagedModel<RepresentationModel<EventResource>> pagedModel = assembler.toModel(page,
				event -> new EventResource(event));
		return ResponseEntity.ok(pagedModel);
	}

	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		// EventValidator를 사용해서 입력항목의 로직을 체크
		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		Event addEvent = this.eventRepository.save(event);

		WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder.linkTo(EventController.class).slash(addEvent.getId());
		URI createUri = selfLinkBuilder.toUri();

		EventResource eventResource = new EventResource(event);
		eventResource.add(WebMvcLinkBuilder.linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withSelfRel());
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		return ResponseEntity.created(createUri).body(eventResource);
	}

	private ResponseEntity<?> badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}