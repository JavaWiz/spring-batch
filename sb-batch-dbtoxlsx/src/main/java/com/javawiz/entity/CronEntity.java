package com.javawiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="cron_tbl")
public class CronEntity {
	@Id
	private long cid;
	@Column(name="cron_expression")
    private String cronXpression;
}