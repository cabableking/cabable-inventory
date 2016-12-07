package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Plan;
import com.cabable.inventory.core.PlanType;

import io.dropwizard.hibernate.AbstractDAO;

public class PlanDAO extends AbstractDAO<Plan>{
	public PlanDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Plan> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Plan create(Plan rc) {
        return persist(rc);
    }
    
    public List<Plan> get(PlanType planname) {
    	Criteria criteria = criteria();
    	DAOUtils.addRestrictionIfNotNull(criteria, "type", planname);
    	return list(criteria);
    }

}
