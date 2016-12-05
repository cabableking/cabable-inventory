package com.cabable.inventory.resources;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cabable.inventory.api.Saying;
import com.cabable.inventory.core.Car;
import com.cabable.inventory.core.Device;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.CarDAO;
import com.cabable.inventory.db.DAOUtils;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.params.DateTimeParam;

@Path("/car")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class CarResource {
	    private static final Logger LOGGER = LoggerFactory.getLogger(CarResource.class);

	    private final CarDAO dao;

	    public CarResource(CarDAO dao) {
	    	this.dao = dao;
	    }

	    @POST
	    @Path("create")
	    @Timed(name = "get-requests")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Car createcar(@Auth User user,Car car) {
	        return dao.create(car);
	    }

	    @POST
	    @Path("update")
	    @Timed(name = "post-update-Car")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public Car update(@Auth User user,Car Car) {
	    	LOGGER.info("Updating:" +  Car ) ;
	        return dao.update(Car);
	    }
	    
	    @DELETE
	    @Path("delete")
	    @Timed(name = "post-delete-Car")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    @RolesAllowed({"ADMIN","SUPERADMIN"})
	    public String delete(@Auth User user,@QueryParam("car_reg_id") String car_reg_id) {
	    	Car Car = new Car();
	    	Car.setCar_reg_id(car_reg_id);
    		DAOUtils.contextualizeDAO(user, dao);
	    	LOGGER.info("Deleting :" +  car_reg_id ) ;
	        return "Deleted " + dao.delete(Car) + "Instances";
	    }
	    
	    @GET
	    @Path("get")
	    @Timed(name = "post-get-Car")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public Car get(@Auth User user,@QueryParam("car_reg_id") String car_reg_id) {
	    		Car Car = new Car();
	    		DAOUtils.contextualizeDAO(user, dao);
		    	Car.setCar_reg_id(car_reg_id);
		        return dao.findById(car_reg_id).get();
	    }
	    
	    @GET
	    @Path("getAll")
	    @Timed(name = "post-get-Car")
	    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
	    @UnitOfWork
	    public List<Car> get(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.getAll();
	    }
	}


