package com.myboot.restapi.events;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboot.restapi.accounts.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime beginEnrollmentDateTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime closeEnrollmentDateTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime beginEventDateTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endEventDateTime;

	private String location;
	private int basePrice;
	private int maxPrice;
	private int limitOfEnrollment;
	private boolean offline;
	private boolean free;
	@ManyToOne
	private Account manager;
	
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus = EventStatus.DRAFT;

	public void update() {
		// Update free
		if (this.basePrice == 0 && this.maxPrice == 0) {
			this.free = true;
		} else {
			this.free = false;
		}
		// Update offline
		if (this.location == null || this.location.isBlank()) {
			this.offline = false;
		} else {
			this.offline = true;
		}
	}
}
