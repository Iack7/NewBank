package newbank.server;
/*
 * This class is a database of all customers. The database allows (i) to add new custmers and (ii) to search the database by username.
 */

import java.util.HashMap;

public class customerDB {
	
	private HashMap<String,Customer> customers;

	public customerDB() {
		customers = new HashMap<>();
		addTestData();
	}
	

	private void addTestData() {
		Customer bhagy = new Customer("bhagy", "secretWord");
		bhagy.addAccount(new Account("Main", 1000.0));
		addCustomer(bhagy);
		
		Customer christina = new Customer("Christina", "PASSWORD!!");
		christina.addAccount(new Account("Savings", 1500.0));
		addCustomer(christina);
		
		Customer john = new Customer("John", "UniOfBaths");
		john.addAccount(new Account("Checking", 250.0));
		addCustomer(john);
	}
	
	
	private void addCustomer(Customer customer) {
		String username = customer.getUsername();
		customers.put(username, customer);
	}
	
	public Customer checkLogInDetails(String userName, String password) {
		return null;
	}
	
}
