package com.example.springjpa;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * Base controller containing common error handlers
 * 
 * @author fsolijon
 *
 */
public class BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	
	/**
	 * Handles validation error
	 * @param e MethodArgumentNotValidException validation exception
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus( code = HttpStatus.BAD_REQUEST )
	public Status validationError(MethodArgumentNotValidException e) {

		Status status = new Status(false, Status.INVALID_INPUT);
		status.setFieldErrors(
				e.getBindingResult().getFieldErrors().stream()
				.map(err -> {
					Status.FieldError field = new Status.FieldError();
					field.setName(err.getField());
					field.setValue(err.getRejectedValue());
					field.setMessage(err.getDefaultMessage());
					return field;
				})
				.collect(Collectors.toList()));
		
		return status;
	}
	

	/**
	 * Handling for generic exceptions.
	 * @param ex Exception encountered.
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseStatus( code = HttpStatus.BAD_GATEWAY )
	public Status generalError(Exception ex) {
		LOGGER.error("Unknown error.", ex);
		
		return Status.FAILED_UNKNOWN_REASON;
	}

}
