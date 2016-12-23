package com.cabable.inventory.core;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name="user")
@NamedQueries(
	{
		@NamedQuery(name="User.verifyUser",query="select u from User u where password=:password and username=:username"),
		@NamedQuery(name="User.blockUser",query="update User u set isActive=false where username=:username"),
		@NamedQuery(name="User.changePassword", query="update User u set password=:newpassword where username=:username and password=:password"),
	}
)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="user")
public class User implements Principal {
	
	@Column
	@NotNull
    private String role;

	@Id
	@Column
	@NotNull
    private String username; 
	
	@Column
	@JsonInclude(Include.NON_NULL)
    private String password;
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private String client_id;
	
	@Column
	@JsonInclude(Include.NON_NULL)
	private String client_secret;
	
	@Column
	@Min(1)
	private long operator_id;
	
	@Column
	private boolean isActive;
    
    public User(String username,String role) {
    	this.username = username;
        this.role = role;
    }
    
    public User(){
    	super();
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }

    public String getRole() {
        return role;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getName() {
		return this.username;
	}
}
