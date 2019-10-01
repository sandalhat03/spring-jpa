package com.example.springjpa.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjpa.Status;
import com.example.springjpa.order.domain.Order;
import com.example.springjpa.order.service.OrderService;

/**
 * 
 * Rest controller endpoint for handling 
 * customer's order related operations
 * 
 * @author fsolijon
 *
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	/**
	 * Create an order or update an existing customer's order.
	 * 
	 * @param order Order information.
	 * @return Updated/Created Order.
	 */
	@PostMapping
	public Order saveOrder(@RequestBody Order order) {
		
		return orderService.saveOrder(order);
	}
	
	/**
	 * Delete an existing order by order ID
	 * 
	 * @param orderId Integer order ID
	 * @return Status of order deletion.
	 */
	@DeleteMapping("/{orderId}")
	public Status deleteOrder(@PathVariable("orderId") Integer orderId) {
		
		return orderService.deleteOrder(orderId);
	}
	
	/**
	 * Retrieve all orders of the specified customer.
	 * 
	 * @param customerId Integer customer ID
	 * @return List of Orders for the given customer ID
	 */
	@GetMapping
	public List<Order> getCustomerOrders(@RequestParam("customerId") Integer customerId) {
		
		return orderService.getOrdersByCustomerId(customerId);
	}

}
