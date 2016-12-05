package com.cabable.inventory.db;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.cabable.inventory.core.User;

public class DAOUtils {

	public static < E > void addRestrictionIfNotNull(Criteria criteria, String propertyName, E value) {
        if (value != null && (NumberUtils.isNumber(value.toString())?!Long.valueOf(value.toString()).equals(0L):true)) {
            criteria.add(Restrictions.eq(propertyName, value));
        }
    }
	
	public static ContextAwareDAO contextualizeDAO(User user, ContextAwareDAO dao){
		 dao.setUser(user);
		 return dao;
	}
}
