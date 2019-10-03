package com.example.springjpa.order.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import com.example.springjpa.SpringJpaConfiguration;
import com.example.springjpa.customer.domain.Customer;
import com.example.springjpa.customer.service.CustomerService;
import com.example.springjpa.order.dao.OrderDao;
import com.example.springjpa.order.domain.Order;
import com.example.springjpa.order.service.OrderService;
import com.example.springjpa.order.service.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@Import(value = SpringJpaConfiguration.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private OrderDao dao;
    
    @MockBean
    private CustomerService customerService;
    
    @TestConfiguration
    static class OrderControllerTestConfiguration {
  
        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl();
        }
    }
    
    /**
     * Helper method to convert Object to JSON String
     */
    public static String asJsonString(final Order obj) {
        try {
        	Map<String, Object> map = new HashMap<String, Object>();
        	put(map, "id", obj.getId());
        	put(map, "customerId", obj.getCustomerId());
        	put(map, "orderDate", obj.getOrderDate().toString());
        	put(map, "orderNumber", obj.getOrderNumber());
        	put(map, "totalAmount", obj.getTotalAmount());
        	
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Make sure that null data does not create an entry
     */
    private static void put(Map<String, Object> map, String name, Object data) {
    	if( data != null ) {
    		if( data instanceof LocalDateTime 
    				|| data instanceof BigDecimal ) {
        		data = data.toString();
        	}
    		map.put(name, data);
    	}
    }
    
    
	@Test
	public void testOrderList_SuccessCustomerOrderRetrieval() throws Exception {
		
		Mockito.when(dao.findOderByCustomerId(Mockito.eq(10))).thenReturn(Arrays.asList(new Order(), new Order(), new Order()));
		
		mvc.perform(get("/api/orders?customerId=10")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	
	@Test
	public void testOrderList_SuccessCustomerOrderRetrieval_Empty() throws Exception {
		
		Mockito.when(dao.findOderByCustomerId(Mockito.eq(10))).thenReturn(new ArrayList<Order>());
		
		mvc.perform(get("/api/orders?customerId=10")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}
	
	
	@Test
	public void testOrderList_UnexpectedError() throws Exception {
		
		Mockito.when(dao.findOderByCustomerId(Mockito.eq(10))).thenThrow(RuntimeException.class);
		
		mvc.perform(get("/api/orders?customerId=10")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Internal server error")));
	}
	
	
	@Test
	public void testOrderList_UnexpectedError_Locale_ph() throws Exception {
		
		Mockito.when(dao.findOderByCustomerId(Mockito.eq(10))).thenThrow(RuntimeException.class);
		
		mvc.perform(get("/api/orders?customerId=10&lang=ph")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Problema sa server")));
	}
	
	
	@Test
	public void testOrderSave_Success() throws Exception {
	
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.of(2000, 12, 12, 13, 45, 5, 1));
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.99"));
		
		Mockito.when(customerService.getCustomer(expectedOrder.getCustomerId())).thenReturn(new Customer());
		
		Mockito.when(dao.save(Mockito.any(Order.class))).thenReturn(expectedOrder);
		
		mvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(expectedOrder.getId())))
				.andExpect(jsonPath("$.customerId", is(expectedOrder.getCustomerId())))
				.andExpect(jsonPath("$.orderDate", is(expectedOrder.getOrderDate().toString())))
				.andExpect(jsonPath("$.orderNumber", is(expectedOrder.getOrderNumber())))
				.andExpect(jsonPath("$.totalAmount", is(expectedOrder.getTotalAmount().toString())));
	}
	
	
	@Test
	public void testOrderSave_FailedNotExistingCustomer() throws Exception {
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.of(2000, 12, 12, 13, 45, 5, 1));
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.99"));
		
		Mockito.when(customerService.getCustomer(expectedOrder.getCustomerId())).thenReturn(null);
		
		mvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Can not save order. Customer does not exists.")));
		
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderSave_FailedNotExistingCustomer_Locale_ph() throws Exception {
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.of(2000, 12, 12, 13, 45, 5, 1));
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.99"));
		
		Mockito.when(customerService.getCustomer(expectedOrder.getCustomerId())).thenReturn(null);
		
		mvc.perform(post("/api/orders?lang=ph")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Hindi maaaring i-save ang order. Hindi mahanap ang customer.")));
		
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderSave_FailedMissingCustomerId() throws Exception {
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(null);
		expectedOrder.setOrderDate(LocalDateTime.now());
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.99"));
		
		mvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("customerId")))
				.andExpect(jsonPath("$.fieldErrors[0].value", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Customer ID is required")));
		
		Mockito.verify(customerService, Mockito.never()).getCustomer(Mockito.anyInt());
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderSave_FailedLongOrderNumber() throws Exception {
		
		String text_11_chars = "_0123456789X";
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.now());
		expectedOrder.setOrderNumber(text_11_chars);
		expectedOrder.setTotalAmount(new BigDecimal("999.99"));
		
		mvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("orderNumber")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(text_11_chars)))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Maximum of 10 characters only")));

		Mockito.verify(customerService, Mockito.never()).getCustomer(Mockito.anyInt());
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderSave_FailedLongPrecisionTotalAmount() throws Exception {
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.now());
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.999"));
		
		mvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Invalid input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("totalAmount")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(expectedOrder.getTotalAmount().doubleValue())))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Decimal precision up to 2 places only")));

		Mockito.verify(customerService, Mockito.never()).getCustomer(Mockito.anyInt());
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderSave_FailedLongPrecisionTotalAmount_Locale_ph() throws Exception {
		
		Order expectedOrder = new Order();
		expectedOrder.setId(99);
		expectedOrder.setCustomerId(10);
		expectedOrder.setOrderDate(LocalDateTime.now());
		expectedOrder.setOrderNumber("11111");
		expectedOrder.setTotalAmount(new BigDecimal("999.999"));
		
		mvc.perform(post("/api/orders?lang=ph")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(expectedOrder)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.reason", is("Problema sa input")))
				.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
				.andExpect(jsonPath("$.fieldErrors[0].name", is("totalAmount")))
				.andExpect(jsonPath("$.fieldErrors[0].value", is(expectedOrder.getTotalAmount().doubleValue())))
				.andExpect(jsonPath("$.fieldErrors[0].message", is("Hanggang 2 precision lang")));

		Mockito.verify(customerService, Mockito.never()).getCustomer(Mockito.anyInt());
		Mockito.verify(dao, Mockito.never()).save(Mockito.any(Order.class));
	}
	
	
	@Test
	public void testOrderrDelete_Success() throws Exception {
		
		mvc.perform(delete("/api/orders/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.reason", isEmptyOrNullString()))
				.andExpect(jsonPath("$.fieldErrors", isEmptyOrNullString()));
		
		Mockito.verify(dao, Mockito.times(1)).deleteById(Mockito.any(Integer.class));
	}

}
