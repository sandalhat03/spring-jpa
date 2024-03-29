package com.example.springjpa.customer.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.springjpa.SpringJpaConfiguration;
import com.example.springjpa.customer.dao.CustomerDao;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.customer.service.CustomerService;
import com.example.springjpa.customer.service.CustomerServiceImpl;
import com.example.springjpa.order.domain.Order;
import com.example.springjpa.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@Import(value = SpringJpaConfiguration.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private CustomerDao customerDao;
    
    @MockBean
    private OrderService orderService;
    
    @TestConfiguration
    static class CustomerControllerTestConfiguration {
  
        @Bean
        public CustomerService customerService() {
            return new CustomerServiceImpl();
        }
        
    }
    
    /**
     * Helper method to convert Object to JSON String
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
	@Test
	public void testCustomerList_SuccessCustomerRetrieval() throws Exception {
		
		Mockito.when(customerDao.findAll()).thenReturn(Arrays.asList(new Customer(), new Customer(), new Customer()));
		
		mvc.perform(get("/api/customers")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}
	
	
	@Test
	public void testCustomerList_SuccessCustomerRetrieval_Empty() throws Exception {
		
		Mockito.when(customerDao.findAll()).thenReturn(new ArrayList<Customer>());
		
		mvc.perform(get("/api/customers")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}
	
	
	@Test
	public void testCustomerList_UnexpectedError() throws Exception {
		
		Mockito.when(customerDao.findAll()).thenThrow(RuntimeException.class);
		
		mvc.perform(get("/api/customers")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Internal server error")));
	}
	
	
	@Test
	public void testCustomerList_UnexpectedError_Locale_ph() throws Exception {
		
		Mockito.when(customerDao.findAll()).thenThrow(RuntimeException.class);
		
		mvc.perform(get("/api/customers?lang=ph")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Problema sa server")));
	}
	
	
	@Test
	public void testCustomerSave_Success() throws Exception {
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		Mockito.when(customerDao.save(Mockito.any(Customer.class))).thenReturn(expectedCustomer);
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(expectedCustomer.getId())))
				.andExpect(jsonPath("$.firstName", is(expectedCustomer.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(expectedCustomer.getLastName())))
				.andExpect(jsonPath("$.city", is(expectedCustomer.getCity())))
				.andExpect(jsonPath("$.country", is(expectedCustomer.getCountry())))
				.andExpect(jsonPath("$.phone", is(expectedCustomer.getPhone())));
	}
	
	
	@Test
	public void testCustomerSave_FailedMissingFirstName() throws Exception {
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName(null);
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("firstName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("First Name is required")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedBlankFirstName() throws Exception {
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("");
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("firstName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("First Name is required")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedLongFirstName() throws Exception {
		
		final String text_41_chars = "_123456789_123456789_123456789_123456789X";
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName(text_41_chars); 
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("firstName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_41_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 40 characters only")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedMissingLasttName() throws Exception {
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName(null);
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("lastName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Last Name is required")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedBlankLasttName() throws Exception {
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName("");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("lastName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Last Name is required")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedLongLasttName() throws Exception {
		
		final String text_41_chars = "_123456789_123456789_123456789_123456789X";
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName(text_41_chars);
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("lastName")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_41_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 40 characters only")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedLongCity() throws Exception {
		
		final String text_41_chars = "_123456789_123456789_123456789_123456789X";
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity(text_41_chars);
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("city")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_41_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 40 characters only")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedLongCountry() throws Exception {
		
		final String text_41_chars = "_123456789_123456789_123456789_123456789X";
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry(text_41_chars);
		expectedCustomer.setPhone("123123");
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("country")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_41_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 40 characters only")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerSave_FailedLongPhone() throws Exception {
		
		final String text_21_chars = "_123456789_123456789X";
		
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName("Test First Name");
		expectedCustomer.setLastName("Test Last Name");
		expectedCustomer.setCity("Manila");
		expectedCustomer.setCountry("Philippines");
		expectedCustomer.setPhone(text_21_chars);
		
		mvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedCustomer)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("phone")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_21_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 20 characters only")));
		
		Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
	}
	
	
	@Test
	public void testCustomerDelete_Success() throws Exception {
		
		Mockito.when(customerDao.findById(Mockito.anyInt())).thenReturn(Optional.of(new Customer()));
		
		mvc.perform(delete("/api/customers/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.reason", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors", isEmptyOrNullString()));
		
		Mockito.verify(customerDao, Mockito.times(1)).deleteById(Mockito.any(Integer.class));
	}
	
	
	@Test
	public void testCustomerDelete_FailedExistingOrder() throws Exception {
		
		Mockito.when(customerDao.findById(Mockito.anyInt())).thenReturn(Optional.of(new Customer()));
		Mockito.when(orderService.getOrdersByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(new Order()));
		
		mvc.perform(delete("/api/customers/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Kindly delete customer's orders first before deleting customer.")));
		
		Mockito.verify(customerDao, Mockito.never()).deleteById(Mockito.any(Integer.class));
	}
	
	
	@Test
	public void testCustomerDelete_FailedExistingOrder_Locale_ph() throws Exception {
		
		Mockito.when(customerDao.findById(Mockito.anyInt())).thenReturn(Optional.of(new Customer()));
		Mockito.when(orderService.getOrdersByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(new Order()));
		
		mvc.perform(delete("/api/customers/2?lang=ph")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Unahing tanggalin ang mga order ng customer bago burahin ang customer.")));
		
		Mockito.verify(customerDao, Mockito.never()).deleteById(Mockito.any(Integer.class));
	}
	
	
	@Test
	public void testCustomerDelete_FailedNotExistingCustomer() throws Exception {
		
		Mockito.when(customerDao.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		
		mvc.perform(delete("/api/customers/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Can not delete customer. Customer [2] does not exists.")));
		
		Mockito.verify(orderService, Mockito.never()).getOrdersByCustomerId(Mockito.anyInt());
		Mockito.verify(customerDao, Mockito.never()).deleteById(Mockito.any(Integer.class));
	}

}
