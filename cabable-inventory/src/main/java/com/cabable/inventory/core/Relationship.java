package com.cabable.inventory.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="onboarding")
@NamedQueries(
		{
			@NamedQuery(name="Rel.delete", query="delete from Relationship where id=:id")
		}
)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Relationship {

	@Id
	private int id;
	
	@Column
	@Min(1)
	private long operator_id;
	
	@Column
	private String car_reg_id;
	
	@Column
	private String driver_license_no;
	
	@Column
	private long device_imei;
	
	@Column
	private boolean is_complete;

	public Relationship(long operator_id){
		this.operator_id = operator_id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	
	
	
}
