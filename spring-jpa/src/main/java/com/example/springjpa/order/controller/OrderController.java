package com.example.springjpa.order.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjpa.common.BaseController;
import com.example.springjpa.common.Status;
import com.example.springjpa.common.exception.CustomerNotExistingForOrderException;
import com.example.springjpa.common.exception.DeletingNotExistingOrderException;
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
public class OrderController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	OrderService orderService;
	
	
	/**
	 * Create an order or update an existing customer's order.
	 * 
	 * @param order Order information.
	 * @return Updated/Created Order.
	 */
	@PostMapping
	public Order saveOrder(@Valid @RequestBody Order order) {
		
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
		
		orderService.deleteOrder(orderId);
		
		return Status.SUCCESS;
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

	
	/**
	 * Handling for saving of Order for not existing Customer
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ CustomerNotExistingForOrderException.class })
	@ResponseStatus( code = HttpStatus.CONFLICT )
	public Status orderSaveCustomerNotExistingError(CustomerNotExistingForOrderException ex, Locale locale) {
		LOGGER.warn("Save Order error. {}", ex.getMessage());
		
		String msg = messageSource.getMessage("error.save.order.nonexisting.customer", 
				new Object[]{ex.getCustomerId()}, locale);

		return new Status(false, msg);
	}
	
	
	/**
	 * Handling for deleting Order
	 * @return Status containing error details.
	 */
	@ExceptionHandler({ DeletingNotExistingOrderException.class })
	@ResponseStatus( code = HttpStatus.CONFLICT )
	public Status orderDeleteError(DeletingNotExistingOrderException ex, Locale locale) {
		LOGGER.warn("Delete Order error. {}", ex.getMessage());
		
		String msg = messageSource.getMessage("error.delete.nonexisting.order", 
					new Object[]{ex.getOrderId()}, locale);
		
		return new Status(false, msg);
	}

}
