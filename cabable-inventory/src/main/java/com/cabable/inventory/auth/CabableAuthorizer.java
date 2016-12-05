package com.cabable.inventory.auth;

import com.cabable.inventory.core.User;

import io.dropwizard.auth.Authorizer;

public class CabableAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRole() != null && user.getRole().equals(role);
    }
}
