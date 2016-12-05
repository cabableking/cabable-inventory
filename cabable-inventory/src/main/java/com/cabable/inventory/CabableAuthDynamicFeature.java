package com.cabable.inventory;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import io.dropwizard.auth.AuthDynamicFeature;

public class CabableAuthDynamicFeature extends AuthDynamicFeature{

	public CabableAuthDynamicFeature(ContainerRequestFilter authFilter) {
		super(authFilter);
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {

		final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
		final boolean annotationOnMethod = am.isAnnotationPresent(IgnoreOAuthFilter.class);
		if(!annotationOnMethod)
			super.configure(resourceInfo, context);
		else
			return;
	}

}
