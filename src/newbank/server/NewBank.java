package newbank.server;

import java.util.HashMap;

public class NewBank {
		
	private customerDB customers;
	
	public NewBank() {
		customers = new customerDB();
	}
	
	public synchronized Customer checkLogInDetails(String customerID, String password) {
		Customer customer = customers.getCustomer(customerID);
		if (customer == null) {
			return null;
		} else if ( customer.checkPassword(password) ) {
			return customer;
		} else {
			return null;
		}
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(String customerID, String request) {
		Customer customer = customers.getCustomer(customerID);
		if( customer != null ) {
			switch(request) {
			
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer.getCustomerID() );
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(String customerID) {
		return customers.getCustomer(customerID).accountsToString();
	}

}
