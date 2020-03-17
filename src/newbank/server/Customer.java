package newbank.server;

import java.util.ArrayList;

public class Customer extends User{
	
	private ArrayList<Account> accounts;
	
	public Customer(String customerID, String password) {
		super(customerID, password);
		accounts = new ArrayList<>();
	}
	
	
	@Override
	public String getUserType() {
		return "customer";
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString()+"\n";
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}
	
}
