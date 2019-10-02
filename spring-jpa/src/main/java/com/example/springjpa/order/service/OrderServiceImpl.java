package com.example.springjpa.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.springjpa.Status;
import com.example.springjpa.exception.CustomerNotExistingForOrderException;
import com.example.springjpa.order.dao.OrderDao;
import com.example.springjpa.order.domain.Order;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDao orderDao;

	@Override
	public List<Order> getOrdersByCustomerId(Integer customerId) {

		return orderDao.findOderByCustomerId(customerId);
	}

	@Override
	public Order saveOrder(Order order) {

		try {
			
			return orderDao.save(order);

		} catch (DataIntegrityViolationException e) {

			throw new CustomerNotExistingForOrderException();
		}
	}

	@Override
	public Status deleteOrder(Integer orderId) {

		orderDao.deleteById(orderId);

		return Status.SUCCESS;

	}

}
