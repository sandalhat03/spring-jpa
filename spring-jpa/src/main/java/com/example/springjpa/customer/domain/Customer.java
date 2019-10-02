package com.example.springjpa.customer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Customer", catalog = "spring_jpa")
public class Customer {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotBlank(message = "First Name is required")
	@Size(max = 40, message = "Maximum of 40 characters only")
	@Column(name = "FirstName")
	private String firstName;

	@NotBlank(message = "Last Name is required")
	@Size(max = 40, message = "Maximum of 40 characters only")
	@Column(name = "LastName")
	private String lastName;

	@Size(max = 40, message = "Maximum of 40 characters only")
	@Column(name = "City")
	private String city;

	@Size(max = 40, message = "Maximum of 40 characters only")
	@Column(name = "Country")
	private String country;

	@Size(max = 20, message = "Maximum of 20 characters only")
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
