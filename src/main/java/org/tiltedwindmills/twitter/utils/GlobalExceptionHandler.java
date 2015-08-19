package org.tiltedwindmills.twitter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Advice to all application Controllers for how to handle exceptions.
 *
 * @author John Daniel
 */
@ControllerAdvice
public final class GlobalExceptionHandler {

    // log some stuff
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles errors.  :)
     *
     * @param request The inbound servlet request.
     * @param exception The exception that occurred.
     *
     * @return the appropriate response information.
     */
    @ExceptionHandler
    public ResponseEntity<?> handleException(final HttpServletRequest request, final Exception exception) {

        // TODO : Probably should return to a pretty page or something.

        LOG.warn("Error occurred for request '{}' : {}", request.getRequestURI(), exception);
        return new ResponseEntity<>("Error " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
