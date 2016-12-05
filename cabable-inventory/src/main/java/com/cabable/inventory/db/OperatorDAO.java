package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Operator;

import io.dropwizard.hibernate.AbstractDAO;

public class OperatorDAO extends ContextAwareDAO<Operator>{
	public OperatorDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Operator> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Operator create(Operator op) {
        return persist(op);
    }
    
    public Operator update(Operator op) {
        return persist(op);
    }
    
    public int delete(Operator op){
    	Query query =  namedQuery("Operator.delete");
    	query.setLong("id", op.getId());
    	return query.executeUpdate();
    }

    public List<Operator> getAll() {
        return list(namedQuery("Operator.getAll"));
    }

}
