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

import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.cabable.inventory.core.User;
import com.cabable.inventory.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

public class OAuthClient {
    private String oauthUrl;
    private UserDAO userdao;

    @SuppressWarnings("unused")
    private OAuthClient() {
    	
    }

    public OAuthClient(String oauthUrl, UserDAO userdao) {
        this.oauthUrl = oauthUrl;
        this.userdao = userdao;
    }

    public Response registerScope(OAuthScope scope) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).createScope(scope);
        return response;
    }

    public Response registerApplication(OAuthApplication application) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).createApplication(application);
        return response;
    }

    public void removeScope(String scopeId) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).removeScope(scopeId);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Cannot delete scope " + scopeId);
        }
    }

    public void updateApplication(OAuthApplication application, String clientId) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).updateApplication(clientId, application);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Cannot remove application " + clientId);
        }
    }

    public OAuthTokenResponse fetchToken(TokenRequest tokenRequest) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).fetchToken(tokenRequest);
        if (response.getStatus() != 200 && response.getStatus() != 400) {
            return null;
        }
        return response.readEntity(OAuthTokenResponse.class);
    }

    public TokenValidationResponse validateToken(String accessToken) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        Response response = target.proxy(OAuthServer.class).validateToken(accessToken);
        return response.readEntity(TokenValidationResponse.class);
    }
    
    @UnitOfWork
    public List<User> getUsers(User user){
    	return userdao.get(user);
    }
}
