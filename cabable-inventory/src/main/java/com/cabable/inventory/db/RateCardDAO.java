package com.cabable.inventory.db;

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

    public RateCard create(RateCard rc) {
        return persist(rc);
    }
    
    public RateCard update(RateCard op) {
        return persist(op);
    }
    
    public int delete(RateCard op){
    	Query query =  namedQuery("RateCard.delete");
    	query.setLong("rate_card_id", op.getRate_card_id());
    	return query.executeUpdate();
    }

    public List<RateCard> getAll(RateCard rc) {
    	Criteria criteria = criteria();
    	DAOUtils.addRestrictionIfNotNull(criteria, "id", rc.getRate_card_id());
    	return list(criteria);
    }

}
