package com.example.springjpa.order.service;

import java.util.List;

import com.example.springjpa.order.domain.Order;

public interface OrderService {
	
	
	/**
	 * List all orders by specified customer.
	 * 
	 * @param customerId Customer ID
	 * @return List of Orders of the given customer
	 */
	List<Order> getOrdersByCustomerId(Integer customerId);
	
	
	/**
	 * Creates or Updates order
	 * 
	 * @param order Order to save
	 * @return Created or Updated Order
	 */
	Order saveOrder(Order order);
	
	
	/**
	 * Deletes an Order
	 * 
	 * @param orderId Order ID
	 */
	void deleteOrder(Integer orderId);

}
