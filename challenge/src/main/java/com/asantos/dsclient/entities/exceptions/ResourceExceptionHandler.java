package com.asantos.dsclient.entities.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.asantos.dsclient.resources.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError standardError = new StandardError();
		standardError.setTimestamp(Instant.now());
		standardError.setStatus(status.value());
		standardError.setError("Resource not found");
		standardError.setMessage(exception.getMessage());
		standardError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(standardError);

	}

}