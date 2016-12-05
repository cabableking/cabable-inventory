package com.cabable.inventory.resources;


import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apifest.client.OAuthClient;
import com.cabable.inventory.core.Operator;
import com.cabable.inventory.core.Role;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.DAOUtils;
import com.cabable.inventory.db.OperatorDAO;
import com.cabable.inventory.db.UserDAO;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/operator")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class OperatorResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorResource.class);

	    private final OperatorDAO dao;
	    private final UserDAO userdao; 
	    private final OAuthClient oauthclient;

	    public OperatorResource(OperatorDAO dao, UserDAO userdao, OAuthClient oAuthClient) {
	    	this.dao = dao;
	    	this.userdao = userdao;
	    	this.oauthclient = oAuthClient;
	    }

	    @POST
	    @Path("create")
	    @Timed(name = "get-requests")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"SUPERADMIN"})
	    public Operator createOperator(@Auth User user, Operator op) {
	    	//create operator in DB. 
	        Operator newOp =  dao.create(op);
	        
	        //set up user
	        User opUser = new User(op.getEmail_id(), Role.ADMIN.toString());
	        opUser.setPassword((op.getName() + Base64.getEncoder().encodeToString(RandomUtils.nextBytes(5))));
	        
	        //register operator in OAuth 
	        OAuth2Resource resource = new OAuth2Resource(oauthclient, userdao);
	        opUser = resource.register(opUser, null, "System created ID", op.getName(), op.getWebsite());
	        newOp.setUserDetails(opUser);
	        return newOp;
	    }
	    

	    @POST
	    @Path("update")
	    @Timed(name = "post-update-Operator")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Operator update(@Auth User user,Operator Operator) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Updating:" +  Operator ) ;
	        return dao.update(Operator);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-Operator")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"SUPERADMIN"})
	    public String delete(@Auth User user,@QueryParam("id") int id) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	Operator operator = new Operator();
	    	operator.setId(id);
	    	LOGGER.info("Deleting :" +  id ) ;
	        return "Deleted " + dao.delete(operator) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-Operator")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public Operator get(@Auth User user,@QueryParam("id") long id) {
    		DAOUtils.contextualizeDAO(user, dao);
	    		Operator op = new Operator();
		    	op.setId(id);
		        return dao.findById(id).get();
	    }
	    
	    @GET
	    @Path("getAll")
	    @Timed(name = "post-get-Operator")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<Operator> get(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.getAll();
	    }

	}

