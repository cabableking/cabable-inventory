package com.cabable.inventory.resources;


import java.io.Serializable;
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

import com.cabable.inventory.core.Device;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.DAOUtils;
import com.cabable.inventory.db.DeviceDAO;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/device")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class DeviceResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceResource.class);

	    private final DeviceDAO dao;

	    public DeviceResource(DeviceDAO dao) {
	    	this.dao = dao;
	    }

	    @POST
	    @Path("create")
	    @Timed(name = "post-create-device")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Serializable create(@Auth User user,Device device) {
	    	LOGGER.info("Returning:" +  device ) ;
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.create(device);
	    }
	    
	    @POST
	    @Path("update")
	    @Timed(name = "post-update-device")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Device update(@Auth User user,Device device) {
	    	device.setOperator_id(user.getOperator_id());
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Updating:" +  device ) ;
	        return dao.update(device);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-device")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public String delete(@Auth User user, @QueryParam("imei") long imei) {
	    	Device device = new Device();
	    	device.setImei(imei);
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Deleting :" +  imei ) ;
	        return "Deleted " + dao.delete(device) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-device")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public Device get(@Auth User user,@QueryParam("imei") long imei) {
	    		Device device = new Device();
	    		DAOUtils.contextualizeDAO(user, dao);
		    	device.setImei(imei);
		        return dao.findById(imei).get();
	    }
	    
	    @GET
	    @Path("getAll")
	    @Timed(name = "post-get-device")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<Device> get(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.getAll(user.getOperator_id());
	    }
	  
	}


