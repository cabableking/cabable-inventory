package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Driver;

import io.dropwizard.hibernate.AbstractDAO;

public class DriverDAO extends ContextAwareDAO<Driver>{
	public DriverDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Driver> findById(String id) {
        return Optional.ofNullable(list(namedQuery("Driver.get").setString("driver_license_no", id)).get(0));
    }

    public Driver create(Driver driver) {
        return persist(driver);
    }
    
    public Driver update(Driver driver) {
        return persist(driver);
    }
    
    public int delete(Driver driver) {
        Query q =  namedQuery("Driver.delete");
        q.setString("driver_license_no", driver.getDriver_license_no());
        return q.executeUpdate();
    }

    public List<Driver> getAll() {
        return list(namedQuery("Driver.getAll"));
    }
}
