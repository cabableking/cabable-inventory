package com.cabable.exception;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Provider to provide the following to Jersey framework:</p>
 * <ul>
 * <li>Provision of general runtime exception to response mapping</li>
 * </ul>
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

  @Override
  public Response toResponse(RuntimeException runtime) {

    // Build default response
    Response defaultResponse = Response
      .serverError()
      .entity(ExceptionEntity.DATA)
      .build();

    // Check for any specific handling
    if (runtime instanceof WebApplicationException) {
      return handleWebApplicationException(runtime, defaultResponse);
    }
   
    // Use the default
    LOGGER.error(runtime.getMessage());
    return defaultResponse;

  }

  private Response handleWebApplicationException(RuntimeException exception, Response defaultResponse) {
    WebApplicationException webAppException = (WebApplicationException) exception;

    // No logging
    if (webAppException.getResponse().getStatus() == 401) {
      return Response
        .status(Response.Status.UNAUTHORIZED)
        .entity(ExceptionEntity.LOGIN)
        .build();
    }
    if (webAppException.getResponse().getStatus() == 404) {
      return Response
        .status(Response.Status.NOT_FOUND)
        .build();
    }

    // Debug logging

    // Warn logging

    // Error logging
    LOGGER.error(exception.getMessage());

    return defaultResponse;
  }

}
