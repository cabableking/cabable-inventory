/*
 *  Copyright (C) 2015  oauth2-dropwizard project
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

package com.cabable.inventory.resources;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apifest.client.OAuthApplication;
import com.apifest.client.OAuthApplicationResponse;
import com.apifest.client.OAuthClient;
import com.apifest.client.OAuthScope;
import com.apifest.client.OAuthScopeResponse;
import com.apifest.client.OAuthTokenResponse;
import com.apifest.client.OAuthTokenRevocationRequest;
import com.apifest.client.OAuthTokenRevocationResponse;
import com.apifest.client.TokenRequest;
import com.apifest.client.TokenValidationResponse;
import com.cabable.inventory.IgnoreOAuthFilter;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.UserDAO;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import liquibase.exception.DatabaseException;


/**
 * Oauth2 resource that handles :
 * /login: authenticates user and returns an auth cookie
 * /refresh: refresh authentication using the refresh token
 * /whoami: returns the user id
 * /logout: invalidates the authentication cookie
 *
 * @author Shubham Agrawal 
 */
@Path(value = "/auth")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2Resource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Resource.class);

	OAuthClient client ; 
	UserDAO dao;

	public OAuth2Resource(OAuthClient client, UserDAO dao) {
		this.client = client;
		this.dao=dao;
	}

	@Path("/scopes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@RolesAllowed("SUPERADMIN")
	public OAuthScopeResponse createScope(OAuthScope scope) {
		Response scopeResponse = client.registerScope(scope);
		if(scopeResponse.getStatus() != 200){
			throw new WebApplicationException("Not able to register scope", 400);
		}
		
		return scopeResponse.readEntity(OAuthScopeResponse.class);
	}

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@IgnoreOAuthFilter
	public OAuthTokenResponse login(TokenRequest params, @Context final HttpServletRequest request) {
		LOGGER.info("Login Request: " + params.getClient_id());
		
		if(params.getGrant_type()==null){
			params.setGrant_type("password");
		}
		//verify password
		switch(params.getGrant_type()){
		case "password":
			Optional<User> userOp = dao.verifyUser(params.getUsername(), params.getPassword());
			if(!userOp.isPresent()){
				throw new WebApplicationException("Incorrect username or password", 401);
			}else{
				params.setClient_id(userOp.get().getClient_id());
				params.setClient_secret(userOp.get().getClient_secret());
			}
			break;
		default: 
			throw new WebApplicationException("Only password grant type allowed in Oauth", 500);
		}

		//get access token 
		return client.fetchToken(params);
	}
	
	
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@RolesAllowed("SUPERADMIN")
	public User register(User params, @Context final HttpServletRequest request, @QueryParam("description") String description, @QueryParam("name") String name, @QueryParam("website") String uri ) {
		if(params.getOperator_id()<=0 || (params.getName()==null || params.getName().isEmpty()) || (params.getUsername()==null || params.getUsername().isEmpty())){
			throw new WebApplicationException("Operator ID / username / password / name compulsory", 400);
		}
		
		//check if password is null
		if(params.getPassword()==null || params.getPassword().isEmpty()){
			params.setPassword(params.getName() + Base64.getEncoder().encodeToString(RandomUtils.nextBytes(5)));
			LOGGER.info("System generated password");
		}
		
		//check is username is duplicate
		try {
			if(dao.isDuplicate(params.getUsername())){
				throw new WebApplicationException("Duplicate username, please try something else.", 400);
			}
		} catch (DatabaseException e) {
			throw new WebApplicationException("Blank username, Dont try cute stuff. You ip is " + request.getRemoteAddr() + "I will find you, and i will kill you. ", 401);
		};
		
		//register an oauth application. 
		OAuthApplication application = new OAuthApplication();
		application.setDescription(description);
		application.setName(params.getUsername());
		application.setRedirect_uri((uri==null)?"http://" + params.getName():uri);
		application.setScope(params.getRole());
		application.setStatus(1);
		Map<String, String> mymap = new HashMap<>();
		mymap.put("operator_id", Long.toString(params.getOperator_id()));
		application.setApplication_details(mymap);

		//register an application on oauth
		Response response = client.registerApplication(application);
		
		if(response.getStatus()==200){
			OAuthApplicationResponse re = response.readEntity(OAuthApplicationResponse.class);
			params.setClient_id(re.getClient_id());
			params.setClient_secret(re.getClient_secret());
		}else{
			throw new WebApplicationException(response);
		}
		
		//create user
		return dao.create(params);
	}

	@Path("/refreshtoken")
	@POST
	@IgnoreOAuthFilter
	/*
	 * needs client_id, client_secret and refresh_token as json parameters. 
	 * Other parameters are optional. 
	 */
	public OAuthTokenResponse refreshToken( TokenRequest tokenRequest) {
		if(tokenRequest.getGrant_type()==null){
			tokenRequest.setGrant_type("refresh_token");
		}
		if(!"refresh_token".equals(tokenRequest.getGrant_type())){
			throw new WebApplicationException("Only refresh tokens allowed with this API call ", 401);
		}else{
			return client.fetchToken(tokenRequest) ;
		}
	}

	@Path("/validatetoken")
	@GET
	@IgnoreOAuthFilter
	@RolesAllowed("SUPERADMIN")
	public TokenValidationResponse validateToken(String accessToken) {
		// TODO Auto-generated method stub
		 return client.validateToken(accessToken);
	}
	
	@Path("/logout")
	@GET
	public String logout(@Auth User user, @QueryParam("access_token") String accessToken) {
		OAuthTokenRevocationRequest req = new OAuthTokenRevocationRequest();
		req.setAccess_token(accessToken);
		req.setClient_id(user.getClient_id());
		OAuthTokenRevocationResponse resp = client.removeToken(req);
		return resp.getRevoked();
	}
	
	@Path("/changepassword")
	@POST
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	public String changePassword(@Auth User user, @QueryParam("new_password") String password){
		int i=0;
		if(user!=null && user.getId()!=0){
			 i = dao.changePassword(user.getUsername(), user.getPassword(), password);
		}
		if(i>0){
			return "Password changed successfully.";
		}else{
			throw new WebApplicationException("Password change failed");
		}
	}

}
