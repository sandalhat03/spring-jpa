package com.example.springjpa.customer.service;

import java.util.List;

import com.example.springjpa.Status;
import com.example.springjpa.customer.domain.Customer;

public interface CustomerService {

	List<Customer> getCustomers();
	
	Customer getCustomer(Integer customerId);
	
	Customer saveCustomer(Customer customer);
	
	Status deleteCustomer(Integer customerId);
	
}
