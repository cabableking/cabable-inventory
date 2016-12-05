package com.cabable.inventory.auth;

import java.util.List;
import java.util.Optional;

import com.apifest.client.OAuthClient;
import com.apifest.client.TokenValidationResponse;
import com.cabable.inventory.core.User;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class OAuthAuthenticator implements Authenticator<String, User> {
	
	OAuthClient client; 
    
    public OAuthAuthenticator(OAuthClient client) {
    	this.client = client;
	}
    
    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
    	TokenValidationResponse tokenResponse = client.validateToken(credentials);
    	if(tokenResponse!=null){
    		if(tokenResponse.getValid()){
    			User user = new User();
        		user.setClient_id(tokenResponse.getClientId());
        		user.setRole(tokenResponse.getScope());
        		List<User> list = client.getUsers(user);
        		if(!list.isEmpty()){
        			user = list.get(0);
        		}else{
        			throw new AuthenticationException("Cannot correlate any user with this access token. Invalid access token. Kidhar se churaya ? ");
        		}
        		return Optional.of(user);
    		}else{
        		throw new AuthenticationException("Incorrect access token. Go fuck yourself");
        	}
    	}else{
    		throw new AuthenticationException("Incorrect access token. Go fuck yourself");
    	}
    	
    }
}
