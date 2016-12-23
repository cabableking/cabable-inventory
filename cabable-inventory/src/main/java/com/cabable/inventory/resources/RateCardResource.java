	package com.cabable.inventory.resources;


import java.util.Arrays;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cabable.inventory.core.Plan;
import com.cabable.inventory.core.PlanType;
import com.cabable.inventory.core.RateCard;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.DAOUtils;
import com.cabable.inventory.db.PlanDAO;
import com.cabable.inventory.db.RateCardDAO;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/ratecard")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class RateCardResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(RateCardResource.class);

	    private final RateCardDAO dao;
	    private final PlanDAO plandao;

	    public RateCardResource(RateCardDAO dao, PlanDAO plandao) {
	    	this.dao = dao;
	    	this.plandao = plandao;
	    }

	    @POST
	    @Path("create")
	    @Timed(name = "post-create-rc")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public RateCard create(@Auth User user,RateCard rc) {
	    	LOGGER.info("Returning:" +  rc ) ;
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.create(rc);
	    }
	    
	    @POST
	    @Path("update")
	    @Timed(name = "post-update-rc")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public RateCard update(@Auth User user,RateCard rc) {
	    	rc.setOperator_id(user.getOperator_id());
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Updating:" +  rc ) ;
	        return dao.update(rc);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-rc")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public String delete(@Auth User user, @QueryParam("rate_card_id") long rate_card_id) {
	    	RateCard rc = new RateCard();
	    	rc.setRate_card_id(rate_card_id);
	    	rc.setOperator_id(user.getOperator_id());
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Deleting :" +  rate_card_id ) ;
	        return "Deleted " + dao.delete(rc) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-rc")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<RateCard> get(@Auth User user,RateCard ratecard) {
	    		DAOUtils.contextualizeDAO(user, dao);
		        return dao.getAll(ratecard);
	    }
	    
	    @GET
	    @Path("plans")
	    @Timed(name = "post-get-plan")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<Plan> plans(@Auth User user, @QueryParam(value="plantype") PlanType planname) {
		        return plandao.get(planname);
	    }
	    
	    @GET
	    @Path("plannames")
	    @Timed(name = "post-getNames-planO")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<PlanType> plannames(@Auth User user) {
		        return Arrays.asList(PlanType.values());
	    }
	    
	    
	  
	}


