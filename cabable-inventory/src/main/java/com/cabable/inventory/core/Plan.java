package com.cabable.inventory.core;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name="plan")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Plan {

	@Id
	private long id;
	
	@Column
	private String name;
	
	@Column
	private PlanType type;
	
	@Column
	private String description;
	
	@Column
	private String rate_card_keys;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PlanType getType() {
		return type;
	}

	public void setType(PlanType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRate_card_keys() {
		return rate_card_keys;
	}

	public void setRate_card_keys(String rate_card_keys) {
		this.rate_card_keys = rate_card_keys;
	}
	
	public List<String> getRate_card_keys_list() {
		return Arrays.asList(rate_card_keys.split(":"));
	}
	
	public void setRate_card_keys_list(List<String> keys){
		StringBuilder builder = new StringBuilder();
		for(String key: keys){
			builder.append(key+":");
		}
		builder.deleteCharAt(builder.length()-1);
		this.setRate_card_keys(builder.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
