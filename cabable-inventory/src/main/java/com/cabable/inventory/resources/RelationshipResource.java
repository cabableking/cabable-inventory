package com.cabable.inventory.resources;


import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cabable.inventory.core.Relationship;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.DAOUtils;
import com.cabable.inventory.db.RelationshipDAO;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/relationship")
@Produces(MediaType.APPLICATION_JSON)
public class RelationshipResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipResource.class);

	    private final RelationshipDAO dao;

	    public RelationshipResource(RelationshipDAO dao) {
	    	this.dao = dao;
	    }

	    
	    @PUT
	    @Path("startonboarding")
	    @Timed(name = "post-create-device")
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Serializable startonboarding(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	if(user.getOperator_id()==0){
	    		throw new WebApplicationException("Operator Id is missing for adding user", 400);
	    	}
	    	Relationship relCr = new Relationship(user.getOperator_id());
	    	relCr.setCreated_on(new Date(System.currentTimeMillis()));
	    	return dao.create(relCr);
	    }
	    
	    @POST
	    @Path("update")
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    @Timed(name = "post-update-device")
	    @UnitOfWork
	    public Relationship update(@Auth User user,Relationship rel) {
	    	if(rel.getId()==0){
	    		throw new WebApplicationException("Onboarding ID is a mandatory parameter", 400);
	    	}
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Updating:" +  rel ) ;
	        return dao.update(rel);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-rel")
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public String delete(@QueryParam("id") int id, @Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	Relationship rel = new Relationship(user.getOperator_id());
	    	rel.setId(id);
	    	LOGGER.info("Deleting :" +  id ) ;
	        return "Deleted " + dao.delete(rel) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-device")
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public List<Relationship> get(Relationship rel ,@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	rel.setOperator_id(user.getOperator_id());
	    	return dao.get(rel);
	    }
	    
	    @PUT
	    @Path("add")
	    @Timed(name = "post-get-device")
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public List<Relationship> add(Relationship rel ,@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	    	rel.setOperator_id(user.getOperator_id());
	    	return dao.get(rel);
	    }
	  
	}


