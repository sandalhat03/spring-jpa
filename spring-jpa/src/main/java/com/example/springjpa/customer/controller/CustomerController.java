package com.example.springjpa.customer.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjpa.Status;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.customer.service.CustomerService;
import com.example.springjpa.exception.DeletingCustomerWithExistingOrdersException;

/**
 * 
 * Rest controller endpoint for handling 
 * customer related operations
 * 
 * @author fsolijon
 *
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	CustomerService customerService;

	
	/**
	 * Retrieve all existing customers
	 * 
	 * @return List of existing Customers
	 */
	@GetMapping
	public List<Customer> getCustomers() {

		List<Customer> customers = customerService.getCustomers();

		return customers;
	}

	/**
	 * Retrieve customer given customer ID
	 * 
	 * @param customerId Integer customer ID
	 * @return Customer for the given customer ID
	 */
	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable("customerId") Integer customerId) {

		Customer customer = customerService.getCustomer(customerId);

		return customer;
	}

	/**
	 * Create a customer or update an existing customer.
	 * 
	 * @param customer Customer information.
	 * @return Updated/Created Customer.
	 */
	@PostMapping
	public Customer saveCustomer(@Valid @RequestBody Customer customer) {

		customer = customerService.saveCustomer(customer);

		return customer;
	}

	/**
	 * Delete an existing customer by customer ID
	 * 
	 * @param customerId Integer customer ID
	 * @return Status of customer deletion./
	 */
	@DeleteMapping("/{customerId}")
	public Status deleteCustomer(@PathVariable("customerId") Integer customerId) {

		return customerService.deleteCustomer(customerId);
	}

	
	/**
	 * Handling for deleting a Customer with existing Orders
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ DeletingCustomerWithExistingOrdersException.class })
	@ResponseStatus( code = HttpStatus.CONFLICT )
	public Status customerDeleteError() {

		return Status.FAILED_DELETE_CUSTOMER_WITH_ORDERS;
	}
	
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
