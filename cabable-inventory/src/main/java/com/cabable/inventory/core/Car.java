package com.cabable.inventory.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="cars")
@NamedQueries({
	@NamedQuery(name="Car.delete", query="delete from Car where car_reg_id=:car_reg_id and operator_id=:operator_id"),
	@NamedQuery(name="Car.getAll", query="select d from Car d where operator_id=:operator_id")
	}
)
public class Car {
	
	@Id
	private String car_reg_id;
	
	@Column
	private String color;
	
	@Column
	private String make;
	
	@Column
	private String model;
	
	@Column
	private Integer year;
	
	@Column
	private boolean has_ac;
	
	@Column
	private int capacity;
	
	@Column
	private String category;
	
	@Column
	private int states_permit_map;
	
	@Column
	private boolean is_assigned;
	
	@Column
	@Min(1)
	private long operator_id;
	
	@Column
	private long rate_card_id;
	
	
	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public void setStates_permit_map(int states_permit_map) {
		this.states_permit_map = states_permit_map;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((car_reg_id == null) ? 0 : car_reg_id.hashCode());
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
		Car other = (Car) obj;
		if (car_reg_id == null) {
			if (other.car_reg_id != null)
				return false;
		} else if (!car_reg_id.equals(other.car_reg_id))
			return false;
		return true;
	}
	
	public String getCar_reg_id() {
		return car_reg_id;
	}
	public void setCar_reg_id(String car_reg_id) {
		this.car_reg_id = car_reg_id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public boolean isHas_ac() {
		return has_ac;
	}
	public void setHas_ac(boolean has_ac) {
		this.has_ac = has_ac;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getStates_permit_map() {
		return states_permit_map;
	}
	public boolean isIs_assigned() {
		return is_assigned;
	}
	public void setIs_assigned(boolean is_assigned) {
		this.is_assigned = is_assigned;
	}

	public long getrate_card_id() {
		return rate_card_id;
	}

	public void setrate_card_id(long rate_card_id) {
		this.rate_card_id = rate_card_id;
	}
}
