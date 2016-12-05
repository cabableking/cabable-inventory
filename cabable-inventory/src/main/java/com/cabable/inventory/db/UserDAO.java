package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.User;

import io.dropwizard.hibernate.AbstractDAO;
import liquibase.exception.DatabaseException;

public class UserDAO extends AbstractDAO<User> {
	
	  public UserDAO(SessionFactory factory) {
	        super(factory);
	    }

	    public Optional<User> verifyUser(String username, String password) {
	    	Query query =  namedQuery("User.verifyUser");
	    	query.setString("username", username);
	    	query.setString("password", password);
	    	User user = uniqueResult(query);
	    	return Optional.ofNullable(user);
	    }

	    public int blockUser(String username) {
	    	Query query =  namedQuery("User.blockUser");
	    	query.setString("username", username);
	    	return query.executeUpdate();
	    }
	    
	    public int changePassword(String username, String password, String newPassword){
	    	Query query =  namedQuery("User.changePassword");
	    	query.setString("username", username);
	    	query.setString("password", password);
	    	query.setString("newpassword", newPassword);
	    	return query.executeUpdate();
	    }
	    
	    public User create(User user){
	    	if(user!=null)
	    	 currentSession().save(user);
	    	return user;
	    }
	    
	    public boolean isDuplicate(String username) throws DatabaseException{
	    	if(username==null || username.isEmpty()){
	    		throw new DatabaseException("Someone trying SQL injection , null or empty passed in username");
	    	}
	    	Criteria criteria = criteria();
	    	DAOUtils.addRestrictionIfNotNull(criteria, "username", username);
	    	List<User> users = list(criteria);
	    	return !users.isEmpty();
	    }
	    
	    public List<User> get(User user){
	    	Criteria criteria = criteria();
	    	DAOUtils.addRestrictionIfNotNull(criteria, "operator_id", user.getOperator_id());
	    	DAOUtils.addRestrictionIfNotNull(criteria, "username", user.getUsername());
	    	DAOUtils.addRestrictionIfNotNull(criteria, "client_id", user.getClient_id());
	        return list(criteria);
	    }
}
