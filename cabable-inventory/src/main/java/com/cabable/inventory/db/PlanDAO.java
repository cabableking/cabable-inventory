package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Plan;

public class PlanDAO extends ContextAwareDAO<Plan>{
	public PlanDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Plan> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Plan create(Plan rc) {
        return persist(rc);
    }
    
    public List<Plan> get(String planname) {
    	Criteria criteria = criteria();
    	DAOUtils.addRestrictionIfNotNull(criteria, "name", planname);
    	return list(criteria);
    }

}
