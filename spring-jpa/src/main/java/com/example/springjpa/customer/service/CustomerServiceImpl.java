package com.example.springjpa.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springjpa.common.Status;
import com.example.springjpa.common.exception.DeletingCustomerWithExistingOrdersException;
import com.example.springjpa.customer.dao.CustomerDao;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.order.service.OrderService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	OrderService orderService;
	

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
		
		boolean emptyOrders = orderService.getOrdersByCustomerId(customerId).isEmpty();
		
		if( emptyOrders ) {
		
			customerDao.deleteById(customerId);
			
			return Status.SUCCESS;
			
		} else {
			
			throw new DeletingCustomerWithExistingOrdersException();
		}
	}

}
