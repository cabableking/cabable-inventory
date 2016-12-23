package com.cabable.inventory.core;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="ratecardoperatormap")
@JsonIgnoreProperties(ignoreUnknown=true)
@NamedQueries({
	@NamedQuery(name="RateCard.delete", query="delete from RateCard r where id=:rate_card_id and operator_id=:operator_id")
})
public class RateCard {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long rate_card_id; 
	
	@Column 
	@NotNull
	private String rcname; 
	
	@Column
	@NotNull
	private long operator_id; 
	
	@Column
	@NotNull
	private long plan_id;
	
	@ElementCollection
    @MapKeyColumn(name="rckey")
    @Column(name="rcvalue")
    @CollectionTable(name="ratecard", joinColumns=@JoinColumn(name="rate_card_id" , referencedColumnName="rate_card_id"))
	private Map<String, String> cardMap;

	public long getRate_card_id() {
		return rate_card_id;
	}

	public void setRate_card_id(long rate_card_id) {
		this.rate_card_id = rate_card_id;
	}

	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public Map<String, String> getCardMap() {
		return cardMap;
	}

	public void setCardMap(Map<String, String> cardMap) {
		this.cardMap = cardMap;
	}

	public String getName() {
		return rcname;
	}

	public void setName(String rcname) {
		this.rcname = rcname;
	}

	public long getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(long plan_id) {
		this.plan_id = plan_id;
	} 
	
}
