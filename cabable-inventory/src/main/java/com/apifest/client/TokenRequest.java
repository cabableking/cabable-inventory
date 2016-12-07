/*
 * Copyright 2013-2015, ApiFest project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apifest.client;

import javax.ws.rs.FormParam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRequest {
	
	@JsonProperty("grant_type")
	@FormParam("grant_type")
	@JsonIgnore
    private String grant_type;

	@JsonProperty("scope")
	@FormParam("scope")
    private String scope;

	@JsonProperty("client_id")
	@FormParam("client_id")
    private String client_id;

	@JsonProperty("client_secret")
	@FormParam("client_secret")
    private String client_secret;

	@JsonProperty("refresh_token")
	@FormParam("refresh_token")	
    private String refresh_token;
    
	@FormParam("username")	
	@JsonProperty("username")
	private String username; 
	
	
	public String getUsername(){
		return this.username;
	}
	
	@JsonProperty("password")
	@FormParam("password")
	private String password;
	
	public String getPassword(){
		return this.password;
	}
	
    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
