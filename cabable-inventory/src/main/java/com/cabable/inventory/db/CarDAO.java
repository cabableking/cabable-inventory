package com.cabable.inventory.db;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Car;

public class CarDAO extends ContextAwareDAO<Car>{
	public CarDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Car> findById(String id) {
        return Optional.ofNullable(list(namedQuery("Car.get").setString("car_reg_id", id)).get(0));
    }

    public Serializable create(Car car) {
    	return currentSession().save(car);
    }

    public Car update(Car Car) {
        return persist(Car);
    }
    
    public int delete(Car car){
    	Query query =  namedQuery("Car.delete");
    	query.setString("car_reg_id", car.getCar_reg_id());
    	query.setLong("operator_id", this.getUser().getOperator_id());
    	return query.executeUpdate();
    }

    public List<Car> getAll() {
        return list(namedQuery("Car.getAll"));
    }
}
