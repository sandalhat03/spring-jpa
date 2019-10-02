package com.example.springjpa.order.service;

import java.util.List;

import com.example.springjpa.common.Status;
import com.example.springjpa.order.domain.Order;

public interface OrderService {
	
	List<Order> getOrdersByCustomerId(Integer customerId);
	
	Order saveOrder(Order order);
	
	Status deleteOrder(Integer orderId);

}
