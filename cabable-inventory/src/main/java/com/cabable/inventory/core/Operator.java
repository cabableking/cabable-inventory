package com.cabable.inventory.core;

import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="operators")
@NamedQueries({
	@NamedQuery(name="Operator.delete", query="delete from Operator where id=:id"),
	@NamedQuery(name="Operator.getAll", query="select d from Operator d")
	}
)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Operator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private long id ; 
	@Column 
	@NotNull
	private String name ; 
	@Column 
	@NotNull
	private String address ; 
	@Column 
	@NotNull
	private long phone_number ; 
	@Column 
	private long phone_number_2 ; 
	@Column 
	@NotNull
	private String email_id ; 
	@Column 
	private String alternate_email_id ; 
	@Column 
	private Date end_date ; 
	@Column 
	@NotNull
	private String plan_id ; 
	@Column 
	@Lob
	private byte[] logo ; 
	@Column 
	private String documents_link ; 
	@Transient 
	private Date start_date ; 
	@Column 
	private String city ; 
	@Column 
	private String state ; 
	@Column 
	private String country ; 
	@Column 
	private String website ;
	
	@Transient
	private User userDetails;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(long phone_number) {
		this.phone_number = phone_number;
	}
	public long getPhone_number_2() {
		return phone_number_2;
	}
	public void setPhone_number_2(long phone_number_2) {
		this.phone_number_2 = phone_number_2;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getAlternate_email_id() {
		return alternate_email_id;
	}
	public void setAlternate_email_id(String alternate_email_id) {
		this.alternate_email_id = alternate_email_id;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public String getDocuments_link() {
		return documents_link;
	}
	public void setDocuments_link(String documents_link) {
		this.documents_link = documents_link;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Operator other = (Operator) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public User getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	} 
	
	
}