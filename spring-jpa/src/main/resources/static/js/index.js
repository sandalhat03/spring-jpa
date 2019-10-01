$( document ).ready(function() {
	
	const toJson = function(data) {
		var json = {};
	    $.each(data, function() {
	        json[this.name] = this.value || '';
	    });
	    return JSON.stringify(json);
	}
	
	const resetForm = function(form) {
		form.removeData("sourceRow");
		form.find(':input')
		  .not(':button, :submit, :reset')
		  .val('')
		  .prop('checked', false)
		  .prop('selected', false);
	}
	
	const populateTableRow = function(row, prefix, data) {
		row.data( data );
		$.each(data, function(key, value) {
			row.find("." + prefix + key).html(value);
	    });
	}
	
	const tableCustomers = $("#tableCustomers tbody");
	const hiddenCustomerRow = $("#tableCustomers .hidden.row");
	const noCustomerDataRow = $("#tableCustomers .no-data");
	
	const populateCustomerTable = function(){
		$.ajax({
			type: "GET",
			url: "/api/customers",
		    dataType : "json",
		    contentType: "application/json; charset=utf-8",
		    success : function(res) {
		    	if (res && res.length > 0) {
		    		noCustomerDataRow.hide();
			    	$.each(res, function(i, data){
			    		const newRow = hiddenCustomerRow.clone();
			    		tableCustomers.append( newRow );
			    		newRow.show();
			    		populateTableRow( newRow, "cust_", data );
			    	});
		    	} else {
		    		noCustomerDataRow.show();
		    	}
		    }
		});
		
	}
	
	const tableOrders = $("#tableOrders tbody");
	const hiddenOrdersRow = tableOrders.find("tr.hidden.row");
	const noOrdersDataRow = tableOrders.find("tr.no-data");
    
	const resetOrdersTable = function() {
		tableOrders.find("tr.hidden.row").remove();
	}
	
	const modalError = $("#modalError");
	
	$("#btnAddCustomer").click(function(){
		resetForm($("#formSaveCustomer"));
		$("#modalAddCustomer").modal();
	});
	
	$("#btnSaveCustomer").click(function(event){
		event.preventDefault();
		const customerForm = $("#formSaveCustomer");
		const data = customerForm.serializeArray();
		var jsonData = toJson(data);
		$.ajax({
			type: "POST",
			url: "/api/customers",
			data: jsonData,
		    dataType : "json",
		    contentType: "application/json; charset=utf-8",
		    success : function(res) {
		    	if(res.id) {
		    		noCustomerDataRow.hide();
		    		
		    		const sourceRow = customerForm.data("sourceRow");
		    		if( sourceRow ) {
		    			populateTableRow( sourceRow, "cust_", res );
		    		} else {
			    		const newRow = hiddenCustomerRow.clone();
			    		tableCustomers.append( newRow );
			    		newRow.show();
			    		populateTableRow( newRow, "cust_", res );
		    		}
		    	}
		    	$.modal.getCurrent().close();
		    }
		});
	});
	
	populateCustomerTable();
	
	tableCustomers
		.on("click", 
			".btnEditCustomer", 
			function(){
				const parentRow = $(this).closest("tr");
				const customerForm = $("#formSaveCustomer");
				resetForm(customerForm);
				customerForm.data("sourceRow", parentRow);
				$.each( parentRow.data(), function(key, value){
					customerForm.find(":input[name='" + key + "']").val(value || "");
				});
				$("#modalAddCustomer").modal();
				
			})
		.on("click", 
			".btnDeleteCustomer", 
			function(){
				const parentRow = $(this).closest("tr");
				$.ajax({
					type: "DELETE",
					url: "/api/customers/" + parentRow.data().id,
				    dataType : "json",
				    contentType: "application/json; charset=utf-8",
				    success : function(res) {
				    	console.log(res);
				    	parentRow.remove();
				    }
				});
			})
		.on("click", 
			".btnViewOrders", 
			function(){
				const parentRow = $(this).closest("tr");
				$.ajax({
					type: "GET",
					url: "/api/orders?customerId=" + parentRow.data().id,
				    dataType : "json",
				    contentType: "application/json; charset=utf-8",
				    success : function(res) {
			    		resetOrdersTable();
			    		
				    	if (res && res.length > 0) {
				    		noOrdersDataRow.hide();
					    	$.each(res, function(i, data){
					    		const newRow = hiddenOrdersRow.clone();
					    		tableOrders.append( newRow );
					    		newRow.show();
					    		populateTableRow( newRow, "ord_", data );
					    	});
				    	} else {
				    		noOrdersDataRow.show();
				    	}
				    	
			    		$("#modalOrders")
			    			.data("sourceRow", parentRow)
			    			.modal();
				    }
				});
			});
	
	tableOrders
		.on("click", 
			".btnEditOrder", 
			function(){
				const parentRow = $(this).closest("tr");
				const ordersForm = $("#formSaveOrder");
				const sourceRow = $("#modalOrders").data("sourceRow");
				const customerId = sourceRow.data("id");
				
				resetForm(ordersForm);
				
				ordersForm.find("input[name='customerId']").val(customerId);
				ordersForm.data("sourceRow", parentRow);
				$.each( parentRow.data(), function(key, value){
					ordersForm.find(":input[name='" + key + "']").val(value || "");
				});
				$("#modalAddOrder").modal({
					closeExisting: false
				});
				
			})
		.on("click", 
			".btnDeleteOrder", 
			function(){
				const parentRow = $(this).closest("tr");
				$.ajax({
					type: "DELETE",
					url: "/api/orders/" + parentRow.data().id,
				    dataType : "json",
				    contentType: "application/json; charset=utf-8",
				    success : function(res) {
				    	console.log(res);
				    	parentRow.remove();
				    }
				});
			});
	
	$("#btnAddOrder").click(function(){
		const ordersForm = $("#formSaveOrder");
		const sourceRow = $("#modalOrders").data("sourceRow");
		const customerId = sourceRow.data("id");
		
		resetForm(ordersForm);
		
		ordersForm.find("input[name='customerId']").val(customerId);
		$("#modalAddOrder").modal({
			closeExisting: false
		});
	});
	
	$("#btnSaveOrder").click(function(event){
		event.preventDefault();
		const ordersForm = $("#formSaveOrder");
		const data = ordersForm.serializeArray();
		var jsonData = toJson(data);
		$.ajax({
			type: "POST",
			url: "/api/orders",
			data: jsonData,
		    dataType : "json",
		    contentType: "application/json; charset=utf-8",
		    success : function(res) {
		    	if(res.id) {
		    		noOrdersDataRow.hide();
		    		
		    		const sourceRow = ordersForm.data("sourceRow");
		    		if( sourceRow ) {
		    			populateTableRow( sourceRow, "ord_", res );
		    		} else {
			    		const newRow = hiddenOrdersRow.clone();
			    		tableOrders.append( newRow );
			    		newRow.show();
			    		populateTableRow( newRow, "ord_", res );
		    		}
		    	}
		    	$.modal.getCurrent().close();
		    }
		});
	});
	
	$( document ).ajaxError(function( event, jqxhr, settings, thrownError ) {

		modalError.find("h4").html(jqxhr.responseJSON.reason);
		modalError.modal();
		
	});
});