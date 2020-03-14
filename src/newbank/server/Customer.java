package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	private String customerID;
	private String password;
	
	public Customer(String customerID, String password) {
		accounts = new ArrayList<>();
		this.customerID = customerID;
		this.password = password;
		
		System.out.println(this.customerID);
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}
	
	
	
	/*
	 * A function to set a new password. Password change is only allowed, if the old password is provided.
	 * */
	public boolean setNewPassword(String oldPassword, String newPassword) {
		if (oldPassword == this.password) {
			this.password = newPassword;
			return true;
		} else {
			return false;
		}
	}
	
	public String getCustomerID() {
		return this.customerID;
	}
	
	
}
