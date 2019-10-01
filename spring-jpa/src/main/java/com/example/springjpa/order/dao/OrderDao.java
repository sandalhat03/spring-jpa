package com.example.springjpa.order.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springjpa.order.domain.Order;

@Repository
public interface OrderDao extends CrudRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE o.customerId = ?1")
	List<Order> findOderByCustomerId(Integer customerId);
}
