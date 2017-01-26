package com.cabable.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.AuthenticationException;

/**
 * <p>Provider to provide the following to Jersey framework:</p>
 * <ul>
 * <li>Provision of general runtime exception to response mapping</li>
 * </ul>
 */
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  @Override
  public Response toResponse(AuthenticationException exception) {

    // Build default response
    Response defaultResponse = Response
      .serverError()
      .status(Response.Status.UNAUTHORIZED)
      .entity(ExceptionEntity.LOGIN.setMessage(exception.getMessage()))
      .build();

    // Use the default
    LOGGER.error(exception.getMessage());
    return defaultResponse;

  }

}
