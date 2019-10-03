package com.example.springjpa.common;

import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	
	
	@Autowired
	protected MessageSource messageSource;
	
	
	/**
	 * Handles validation error
	 * @param e MethodArgumentNotValidException validation exception
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus( code = HttpStatus.BAD_REQUEST )
	public Status validationError(MethodArgumentNotValidException e, Locale locale) {

		String msg = messageSource.getMessage("error.invalid.input", null, locale);
		Status status = new Status(false, msg);
		
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
	public Status generalError(Exception ex, Locale locale) {
		LOGGER.error("Unknown error.", ex);
		
		String msg = messageSource.getMessage("error.unexpected.exception", null, locale);
		
		return new Status(false, msg);
	}

}
