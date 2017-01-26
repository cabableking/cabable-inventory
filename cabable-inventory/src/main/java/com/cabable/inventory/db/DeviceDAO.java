package com.cabable.inventory.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.cabable.inventory.core.Device;

import io.dropwizard.hibernate.AbstractDAO;

public class DeviceDAO extends ContextAwareDAO<Device>{
	public DeviceDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Device> findById(Long imei) {
        return Optional.ofNullable(list(namedQuery("Device.get").setLong("imei", imei)).get(0));
    }

    public Device create(Device device) {
        return persist(device);
    }
    
    public Device update(Device device) {
        return persist(device);
    }
    
    public int delete(Device device){
    	Query query =  namedQuery("Device.delete");
    	query.setLong("imei", device.getImei());
    	query.setLong("operator_id", this.getUser().getOperator_id());
    	return query.executeUpdate();
    }

    public List<Device> getAll(long op) {
        return list(namedQuery("Device.getAll").setLong("operator_id", op));
    }
}
