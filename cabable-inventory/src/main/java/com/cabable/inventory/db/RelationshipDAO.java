package com.cabable.inventory.db;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Relationship;

public class RelationshipDAO extends ContextAwareDAO<Relationship>{
	public RelationshipDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Relationship> findById(String id) {
        return Optional.ofNullable(get(id));
    }

    public Serializable create(Relationship relationship) {
        return save(relationship);
    }

    public Relationship update(Relationship relationship) {
        return persist(relationship);
    }
    
    public int delete(Relationship relationship){
    	Query query =  namedQuery("Relationship.delete");
    	query.setLong("id", relationship.getId());
    	return query.executeUpdate();
    }
    
    public List<Relationship> get(Relationship rel) {
    	Criteria criteria = criteria();
    	DAOUtils.addRestrictionIfNotNull(criteria, "car_reg_id", rel.getCar_reg_id());
    	DAOUtils.addRestrictionIfNotNull(criteria, "driver_license_no", rel.getDriver_license_no());
    	DAOUtils.addRestrictionIfNotNull(criteria, "device_imei", rel.getDevice_imei());
    	DAOUtils.addRestrictionIfNotNull(criteria, "operator_id", getUser().getOperator_id());
    	DAOUtils.addRestrictionIfNotNull(criteria, "is_complete", rel.isIs_complete());
        return list(criteria);
    }
    
    public List<Relationship> getAll() {
        return list(namedQuery("Relationship.getAll"));
    }
    
   
}
