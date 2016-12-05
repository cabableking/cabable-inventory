package com.cabable.inventory.core;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="devices")
@NamedQueries({
	@NamedQuery(name="Device.delete", query="delete from Device where imei=:imei and operator_id=:operator_id"),
	@NamedQuery(name="Device.getAll", query="select d from Device d where operator_id=:operator_id")
	}
)
public class Device {

	@Column 
	private String device_make; 
	@Column 
	private String device_model; 
	@Column 
	private String os_version; 
	@Id
	@Column(nullable = false, columnDefinition = "INT(16) UNSIGNED")
	private long imei; 
	@Column(columnDefinition = "UNSIGNED INT(20)")
	private int device_phone_number; 
	@Column 
	private String network_provider; 
	@Column 
	private Date created_on; 
	@Column 
	private Date last_updated_on; 
	@Column
	private boolean is_assigned;
	
	@Min(1)
	@Column(columnDefinition = "UNSIGNED INT(11)")
	private long operator_id;

	public String getDevice_make() {
		return device_make;
	}
	public void setDevice_make(String device_make) {
		this.device_make = device_make;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public long getImei() {
		return imei;
	}
	public void setImei(long imei) {
		this.imei = imei;
	}
	public int getDevice_phone_number() {
		return device_phone_number;
	}
	public void setDevice_phone_number(int device_phone_number) {
		this.device_phone_number = device_phone_number;
	}
	public String getNetwork_provider() {
		return network_provider;
	}
	public void setNetwork_provider(String network_provider) {
		this.network_provider = network_provider;
	}
	public Date getCreated_on() {
		return created_on;
	}
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	public Date getLast_updated_on() {
		return last_updated_on;
	}
	public void setLast_updated_on(Date last_updated_on) {
		this.last_updated_on = last_updated_on;
	}
	public long getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (imei ^ (imei >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (imei != other.imei)
			return false;
		return true;
	}
	public boolean isIs_assigned() {
		return is_assigned;
	}
	public void setIs_assigned(boolean is_assigned) {
		this.is_assigned = is_assigned;
	} 


}
