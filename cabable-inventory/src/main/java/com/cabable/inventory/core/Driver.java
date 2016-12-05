package com.cabable.inventory.core;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;


@Entity
@Table(name="drivers")
@NamedQueries({
	@NamedQuery(name="Driver.delete", query="delete from Driver where driver_license_no=:driver_license_no and operator_id=:operator_id"),
	@NamedQuery(name="Driver.getAll", query="select d from Driver d where operator_id=:operator_id")
	}
)
public class Driver {

	@Column 
	private String    first_name ; 
	
	@Column 
	private String     last_name ; 

	@Column(nullable=false, columnDefinition="blob")
	private byte[] 		photo;

	@Id 
	private String     driver_license_no ; 
	
	@Column(nullable=false, columnDefinition="blob")
	private byte[]     license_photo ; 
	
	@Column 
	private int     age ; 
	
	@Column
	@Enumerated(EnumType.STRING)
	private Gender     gender ; 
	
	@Column 
	private int     rating ; 
	
	@Column 
	private Date     created_on ; 
	
	@Column 
	private Date     updated_on ; 
	
	@Column 
	private boolean     is_assigned ; 
	
	@Column 
	private int     contact_num ; 
	
	@Column(nullable=false, columnDefinition = "UNSIGNED INT(11)")
	@Min(1)
	private long     operator_id ;

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getDriver_license_no() {
		return driver_license_no;
	}

	public void setDriver_license_no(String driver_license_no) {
		this.driver_license_no = driver_license_no;
	}

	public byte[] getLicense_photo() {
		return license_photo;
	}

	public void setLicense_photo(byte[] license_photo) {
		this.license_photo = license_photo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public Date getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}

	public boolean isIs_assigned() {
		return is_assigned;
	}

	public void setIs_assigned(boolean is_assigned) {
		this.is_assigned = is_assigned;
	}

	public int getContact_num() {
		return contact_num;
	}

	public void setContact_num(int contact_num) {
		this.contact_num = contact_num;
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
		result = prime * result + ((driver_license_no == null) ? 0 : driver_license_no.hashCode());
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
		Driver other = (Driver) obj;
		if (driver_license_no == null) {
			if (other.driver_license_no != null)
				return false;
		} else if (!driver_license_no.equals(other.driver_license_no))
			return false;
		return true;
	} 

	


}
