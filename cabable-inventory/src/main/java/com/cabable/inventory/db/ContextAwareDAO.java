package com.cabable.inventory.db;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.User;

import io.dropwizard.hibernate.AbstractDAO;

public abstract class ContextAwareDAO<E> extends AbstractDAO<E> {

	private User user; 
	
	public ContextAwareDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	protected User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	@Override
	protected Query namedQuery(String queryName) throws HibernateException {
		Query query = super.namedQuery(queryName);
		return query.setLong("operator_id", user.getOperator_id());
	}
	
}
