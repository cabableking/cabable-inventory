package com.cabable.inventory.core;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
	@NamedQuery(name="RateCard.delete", query="delete from RateCard where rate_card_id=:rate_card_id")
})
@Entity(name="ratecardoperatormap")
@JsonIgnoreProperties(ignoreUnknown=true)
public class RateCard {

	@Id
	@NotNull
	private long rate_card_id; 
	
	@Column 
	@NotNull
	private String name; 
	
	@Column
	@NotNull
	private long operator_id; 
	
	@ElementCollection
	@OneToMany(cascade=CascadeType.ALL)
    @MapKeyColumn(name="key")
    @Column(name="value")
    @CollectionTable(name="ratecard", joinColumns=@JoinColumn(name="rate_card_id" , referencedColumnName="id"))
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	
}
