package com.cabable.inventory.resources;


import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apifest.client.OAuthApplication;
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
	    @UnitOfWork
	    @RolesAllowed({"SUPERADMIN"})
	    @Consumes(MediaType.MULTIPART_FORM_DATA)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Operator createOperator(@Auth User user, MultiPart multipart) {
	    	
	    	List<BodyPart> parts = multipart.getBodyParts();
	    	Operator operator = new Operator();
	    	for(BodyPart part : parts){
	    		switch(part.getContentDisposition().getParameters().get("name")){
	    		case "name":
	    			operator.setName(part.getEntityAs(String.class));break;
	    		case "address1":
	    			operator.setAddress(part.getEntityAs(String.class));break;
	    		case "phoneNumber1":
	    			operator.setPhone_number((part.getEntityAs(String.class)));break;
	    		case "phoneNumber2":
	    			operator.setPhone_number_2(part.getEntityAs(String.class));break;
	    		case "email1":
	    			operator.setEmail_id(part.getEntityAs(String.class));break;
	    		case "email2":
	    			operator.setAlternate_email_id(part.getEntityAs(String.class));break;
	    		case "logo":
	    			       InputStream source = part.getEntityAs(InputStream.class);
	    			        byte[] bytes;
	    					try {
	    						Image image = ImageIO.read(source);
	    						if(image == null) {
	    							throw new WebApplicationException("Not a valid Image");
	    					    }
	    						bytes = IOUtils.toByteArray(source);
	    						if(((bytes.length)/1024)/1024 > 10 ){
	    							throw new WebApplicationException("Image cannot be greater than 10MB ");
	    						}
	    					} catch (IOException e) {
	    						throw new WebApplicationException("Error while saving logo image");
	    					}
	    			        operator.setLogo(bytes);
	    			        break;
	    		case "documents":
	    			InputStream inDocs = part.getEntityAs(InputStream.class);
	    	        try {
	    	        	boolean isZipped = new ZipInputStream(inDocs).getNextEntry() != null;
	    	        	if(!isZipped){
	    	        		throw new WebApplicationException("Not a valid zip file for documents, please retry");
	    	        	}
	    				operator.setDocuments_link(IOUtils.toByteArray(inDocs));
	    			} catch (IOException e) {
	    				throw new WebApplicationException("Error while saving documents zip file");
	    			}
	    	        break;
	    		case "city":
	    			operator.setCity(part.getEntityAs(String.class));break;
	    		case "state":
	    			operator.setState(part.getEntityAs(String.class));break;
	    		case "country":
	    			operator.setCountry(part.getEntityAs(String.class));break;
	    		case "website":
	    			operator.setWebsite(part.getEntityAs(String.class));break;
	    		}
	    		
	    	}
	    	
	    	LocalDate todayLocalDate = LocalDate.now();
	    	
	    	operator.setEnd_date(java.sql.Date.valueOf( todayLocalDate.plusYears(1) ));
	    	//create operator in DB. 
	        Operator newOp =  dao.create(operator);
	        
	        //set up user
	        User opUser = new User(operator.getEmail_id(), Role.ADMIN.toString());
	        opUser.setPassword((Base64.getEncoder().encodeToString(RandomUtils.nextBytes(5))));
	        opUser.setOperator_id(newOp.getId());
	        
	        //register operator in OAuth 
	        OAuth2Resource resource = new OAuth2Resource(oauthclient, userdao);
	        opUser = resource.register(opUser, null, "System created ID", operator.getName(), operator.getWebsite());
	        newOp.setUserDetails(opUser);
	        return newOp;
	    }
	    
	    
	    @POST	
	    @Path("update")
	    @Timed(name = "post-update-Operator")
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
	    @RolesAllowed({"SUPERADMIN"})
	    @UnitOfWork
	    public Operator get(@Auth User user,@QueryParam("id") long id) {
    		DAOUtils.contextualizeDAO(user, dao);
	    		Operator op = new Operator();
		    	op.setId(id);
		        return dao.findById(id).get();
	    }
	    
	    @GET
	    @Path("getAll")
	    @RolesAllowed({"SUPERADMIN", "ADMIN"})
	    @Timed(name = "post-get-Operator")
	    @UnitOfWork
	    public List<Operator> get(@Auth User user) {
    		DAOUtils.contextualizeDAO(user, dao);
	        return dao.getAll();
	    }
	    
	    @PUT
	    @Path("activateUser")
	    @Timed(name = "post-put-activateoperator")
	    @UnitOfWork
	    @RolesAllowed({"SUPERADMIN"})	    
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response activateUser(@Auth User user, @QueryParam("user_id") String user_id){
	    	OAuthApplication application = new OAuthApplication();
	    	User myUser = new User();
	    	myUser.setUsername(user_id);
	    	List<User> userList = userdao.get(myUser);
	    	if(!userList.isEmpty()){
	    		application.setStatus(1);
	    		oauthclient.updateApplication(application, userList.get(0).getClient_id());
	    		return Response.ok().status(200).build();
	    	}else{
	    		throw new WebApplicationException("User could not be activated, user does not exist.", Response.Status.NOT_FOUND);
	    	}
	    	
	    }

	}


