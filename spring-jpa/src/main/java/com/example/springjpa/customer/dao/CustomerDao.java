package com.example.springjpa.customer.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springjpa.customer.domain.Customer;

@Repository
public interface CustomerDao extends CrudRepository<Customer, Integer> {


}
