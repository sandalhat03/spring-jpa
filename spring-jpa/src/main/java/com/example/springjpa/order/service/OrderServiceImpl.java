package com.example.springjpa.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springjpa.common.exception.CustomerNotExistingForOrderException;
import com.example.springjpa.common.exception.DeletingNotExistingOrderException;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.customer.service.CustomerService;
import com.example.springjpa.order.dao.OrderDao;
import com.example.springjpa.order.domain.Order;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDao orderDao;

	@Autowired
	CustomerService customerService;

	@Override
	public List<Order> getOrdersByCustomerId(Integer customerId) {

		return orderDao.findOderByCustomerId(customerId);
	}

	@Override
	public Order saveOrder(Order order) {

		Customer customer = customerService.getCustomer(order.getCustomerId());

		if (customer != null) {

			return orderDao.save(order);

		} else {

			throw new CustomerNotExistingForOrderException(order.getCustomerId());

		}
	}

	@Override
	public void deleteOrder(Integer orderId) {
		
		boolean orderExists = orderDao.findById(orderId).isPresent();
		
		if( !orderExists ) {
			throw new DeletingNotExistingOrderException(orderId);
		}

		orderDao.deleteById(orderId);

	}

}
