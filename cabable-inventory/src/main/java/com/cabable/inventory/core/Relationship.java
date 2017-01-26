package com.cabable.inventory.core;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DynamicUpdate(value=true)
@Table(name="onboarding")
@NamedQueries(
		{
			@NamedQuery(name="Rel.delete", query="delete from Relationship where id=:id")
		}
)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Relationship extends ContextAwareEntity{

	@Id
	@Column(updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String car_reg_id;
	
	@Column
	private String driver_license_no;
	
	@Column
	private long device_imei;
	
	@Column
	private boolean is_complete;
	
	@Column(updatable=false)
	private Date last_updated_on;
	
	@Column(updatable=false)
	private Date created_on;

	
	@ElementCollection
	@CollectionTable(name = "ratecardonboardingmap",
			joinColumns=@JoinColumn(name="onboarding_id"))
    @Column(name="rate_card_id")
	private Set<String> rate_card_ids = new HashSet<>();

	public Relationship(long operator_id){
		this.operator_id = operator_id;
	}
	
	public Relationship() {
		// TODO Auto-generated constructor stub
	}
 	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column
	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public String getCar_reg_id() {
		return car_reg_id;
	}

	public void setCar_reg_id(String car_reg_id) {
		this.car_reg_id = car_reg_id;
	}

	public String getDriver_license_no() {
		return driver_license_no;
	}

	public void setDriver_license_no(String driver_license_no) {
		this.driver_license_no = driver_license_no;
	}

	public long getDevice_imei() {
		return device_imei;
	}

	public void setDevice_imei(long device_imei) {
		this.device_imei = device_imei;
	}

	public boolean isIs_complete() {
		return is_complete;
	}

	public void setIs_complete(boolean is_complete) {
		this.is_complete = is_complete;
	}
	
	public Set<String> getRate_card_ids() {
		return rate_card_ids;
	}

	public Date getLast_updated_on() {
		return last_updated_on;
	}

	public void setLast_updated_on(Date last_updated_on) {
		this.last_updated_on = last_updated_on;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public void setRate_card_ids(Set<String> rate_card_ids) {
		this.rate_card_ids.addAll(rate_card_ids);
	}
	
	
}
