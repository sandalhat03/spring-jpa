package com.example.springjpa.customer.service;

import java.util.List;

import com.example.springjpa.customer.domain.Customer;

public interface CustomerService {

	
	/**
	 * Lists all customers
	 * 
	 * @return List of all Customer
	 */
	List<Customer> getCustomers();
	
	
	/**
	 * Retrieves customer by ID
	 * 
	 * @param customerId Customer ID
	 * @return Customer by given customer ID
	 */
	Customer getCustomer(Integer customerId);
	
	
	/**
	 * Creates or Updates customer
	 * 
	 * @param customer Customer to save
	 * @return Created or Updated Customer
	 */
	Customer saveCustomer(Customer customer);
	
	
	/**
	 * Deletes a customer
	 * 
	 * @param customerId Customer ID
	 */
	void deleteCustomer(Integer customerId);
	
}
