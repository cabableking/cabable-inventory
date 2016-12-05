package com.cabable.inventory.resources;


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

import com.cabable.inventory.core.Driver;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.DAOUtils;
import com.cabable.inventory.db.DriverDAO;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/driver")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class DriverResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(CarResource.class);

	    private final DriverDAO dao;

	    public DriverResource(DriverDAO dao) {
	    	this.dao = dao;
	    }

	    @POST
	    @Path("create")
	    @Timed(name = "get-requests")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Driver create(@Auth User user, Driver Driver) {
	        return dao.create(Driver);
	    }

	    @POST
	    @Path("update")
	    @Timed(name = "post-update-Driver")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Driver update(@Auth User user,Driver Driver) {
	    	LOGGER.info("Updating:" +  Driver ) ;
	        return dao.update(Driver);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-Driver")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public String delete(@Auth User user,@QueryParam("driver_license_no") String driver_license_no) {
	    	Driver Driver = new Driver();
    		DAOUtils.contextualizeDAO(user, dao);
	    	Driver.setDriver_license_no(driver_license_no);
	    	LOGGER.info("Deleting driver:" +  driver_license_no ) ;
	        return "Deleted " + dao.delete(Driver) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-Driver")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public Driver get(@Auth User user,@QueryParam("driver_license_no") String driver_license_no) {
	    		Driver Driver = new Driver();
	    		DAOUtils.contextualizeDAO(user, dao);
		    	Driver.setDriver_license_no(driver_license_no);
		        return dao.findById(driver_license_no).get();
	    }
	    
	    @GET
	    @Path("getAll")
	    @Timed(name = "post-get-Driver")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<Driver> get(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.getAll();
	    }
	   
	}


