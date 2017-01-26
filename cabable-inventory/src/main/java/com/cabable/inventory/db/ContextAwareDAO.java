package com.cabable.inventory.db;

import java.io.Serializable;

import javax.ws.rs.WebApplicationException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.ContextAwareEntity;
import com.cabable.inventory.core.Role;
import com.cabable.inventory.core.User;

import io.dropwizard.hibernate.AbstractDAO;

public abstract class ContextAwareDAO<E extends ContextAwareEntity> extends AbstractDAO<E> {

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
	
	private boolean isAuthorized(long thatUserOpId){
		return (user.getOperator_id() == thatUserOpId || user.getRole().equals(Role.SUPERADMIN) || thatUserOpId==0);
	}
	
	@Override
	protected Query namedQuery(String queryName) throws HibernateException {
		Query query = super.namedQuery(queryName);
		return query.setLong("operator_id", user.getOperator_id());
	}
	
	@Override
	protected E get(Serializable id) {
		// TODO Auto-generated method stub
		ContextAwareEntity entity =  super.get(id);
		if(isAuthorized(entity.getOperator_id())){
			return (E)entity;
		}else{
			throw new WebApplicationException("Operator does not have access to this resource",403);
		}
	}
	
	@Override
	protected E persist(ContextAwareEntity entity) throws HibernateException {
		
		if(isAuthorized(entity.getOperator_id())){
			entity.setOperator_id(user.getOperator_id());
			return (E)super.persist((E)entity);
		}else{
			throw new WebApplicationException("Operator does not have access to this resource",403);
		}
		
	}
	
	protected Serializable save(ContextAwareEntity entity) throws HibernateException{
		if(isAuthorized(entity.getOperator_id())){
			entity.setOperator_id(user.getOperator_id());
			return currentSession().save(entity);
		}else{
			throw new WebApplicationException("Operator does cannot save to this resource",403);
		}
	}
	
}
