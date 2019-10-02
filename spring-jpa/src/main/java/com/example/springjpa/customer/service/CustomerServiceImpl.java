package com.example.springjpa.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.springjpa.Status;
import com.example.springjpa.customer.dao.CustomerDao;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.exception.DeletingCustomerWithExistingOrdersException;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;

	@Override
	public List<Customer> getCustomers() {
		
		List<Customer> customers = new ArrayList<>();
		
		customerDao.findAll()
			.forEach(customers::add);
		
		return customers;
	}

	@Override
	public Customer getCustomer(Integer customerId) {
		
		Customer customer = customerDao.findById(customerId)
				.orElse(null);
		
		return customer;
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		
		customer = customerDao.save(customer);
		
		return customer;
	}

	@Override
	public Status deleteCustomer(Integer customerId) {
		
		try {
			
			customerDao.deleteById(customerId);
			
			return Status.SUCCESS;
			
		} catch (DataIntegrityViolationException e) {

			throw new DeletingCustomerWithExistingOrdersException(e);
		}
	}

}
