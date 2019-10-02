package com.example.springjpa.customer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Customer", catalog = "spring_jpa")
@JsonInclude(Include.NON_NULL)
public class Customer {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotBlank(message = "{error.required.first.name}")
	@Size(max = 40, message = "{error.max.40.chars}")
	@Column(name = "FirstName")
	private String firstName;

	@NotBlank(message = "{error.required.last.name}")
	@Size(max = 40, message = "{error.max.40.chars}")
	@Column(name = "LastName")
	private String lastName;

	@Size(max = 40, message = "{error.max.40.chars}")
	@Column(name = "City")
	private String city;

	@Size(max = 40, message = "{error.max.40.chars}")
	@Column(name = "Country")
	private String country;

	@Size(max = 20, message = "{error.max.20.chars}")
	@Column(name = "Phone")
	private String phone;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
