package com.cabable.inventory.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cabable.inventory.core.OAuthRedisConfig;
import com.cabable.inventory.core.Role;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class OAuthRedisDAO {
	
	private static final String CLIENT_CREDENTIALS_PREFIX_NAME = "cc";
	private static final String SCOPE_CREDENTIALS_PREFIX_NAME = "sc";
	private final Jedis jedis;
	
	public OAuthRedisDAO(OAuthRedisConfig config){
		jedis = new Jedis(config.getUrl());

	}
	
	public List<Role> getScopes(){
		    ScanParams sp = (new ScanParams()).match(SCOPE_CREDENTIALS_PREFIX_NAME+"*").count(1000);
	        String cursor = ScanParams.SCAN_POINTER_START;
	        List<Role> roles = new ArrayList<>();
	        do {
		        ScanResult<String> result = jedis.scan(cursor, sp);
		        cursor = result.getStringCursor();
		        for (String entry : result.getResult()) {
					Map<String, String> appMap = jedis.hgetAll(entry);
					roles.add(Role.valueOf(appMap.get("scope")));
				}
	        } while (!ScanParams.SCAN_POINTER_START.equals(cursor));
	        
	        return roles;
		}
	
	public List<Map<String, String>> getApplications(){
		 ScanParams sp = (new ScanParams()).match(CLIENT_CREDENTIALS_PREFIX_NAME+"*").count(1000);
	        String cursor = ScanParams.SCAN_POINTER_START;
	        List listOfApps = new ArrayList<>();
	        do {
		        ScanResult<String> result = jedis.scan(cursor, sp);
		        cursor = result.getStringCursor();
		        for (String entry : result.getResult()) {
					Map<String, String> appMap = jedis.hgetAll(entry);
					if (!appMap.isEmpty()) {
						listOfApps.add(appMap);
					}
				}
	        } while (!ScanParams.SCAN_POINTER_START.equals(cursor));
	        return listOfApps;
	}
	
	public boolean initializeScopes(){
		
//		for(Role role: Role.values()){
//			   Map<String, String> scopeMap = new HashMap<String, String>();
//		        scopeMap.put("id", role.toString());:
//		        scopeMap.put(Role.DESCRIPTION_FIELD, role.getDescription());
//		        scopeMap.put(Role.CC_EXPIRES_IN_FIELD, String.valueOf(role.getCc_expires_in()));
//		        scopeMap.put(Role.PASS_EXPIRES_IN_FIELD, String.valueOf(role.getPass_expires_in()));
//		        scopeMap.put(Role.REFRESH_EXPIRES_IN_FIELD, String.valueOf(role.getRefresh_expires_in()));
//		        jedis.hmset(SCOPE_CREDENTIALS_PREFIX_NAME + role.toString(), scopeMap);
//		}
//		
		return true;
	}


	
}
