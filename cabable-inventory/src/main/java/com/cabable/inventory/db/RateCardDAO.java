package com.cabable.inventory.db;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.RateCard;

public class RateCardDAO extends ContextAwareDAO<RateCard>{
	public RateCardDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<RateCard> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Serializable create(RateCard rc) {
    	rc.setOperator_id(this.getUser().getOperator_id());
        return save(rc);
    }
    
    public RateCard update(RateCard rc) {
    	rc.setOperator_id(this.getUser().getOperator_id());
        return persist(rc);
    }
    
    public int delete(RateCard op){
    	Query query =  namedQuery("RateCard.delete");
    	query.setLong("rate_card_id", op.getRate_card_id());
    	return query.executeUpdate();
    }

    public List<RateCard> getAll() {
    	return list(namedQuery("RateCard.getAll"));
    }
    
    public List<RateCard> getAll(RateCard rc) {
    	Criteria criteria = criteria();
    	DAOUtils.addRestrictionIfNotNull(criteria, "id", (rc!=null)?rc.getRate_card_id():null);
    	DAOUtils.addRestrictionIfNotNull(criteria, "plan_id", (rc!=null)?rc.getPlan_id():null);
    	DAOUtils.addRestrictionIfNotNull(criteria, "rcname", (rc!=null)?rc.getName():null);
    	DAOUtils.addRestrictionIfNotNull(criteria, "operator_id", getUser().getOperator_id());
    	return list(criteria);
    }

}
