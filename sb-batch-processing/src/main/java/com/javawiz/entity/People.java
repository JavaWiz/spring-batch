package com.javawiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="people")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class People {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="people_id")
	private int pid;
	@Column(name="first_name")
	private String lastName;
	@Column(name="last_name")
    private String firstName;
}
