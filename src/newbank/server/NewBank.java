package newbank.server;

public class NewBank {
		
	private CustomerDB customers;
	
	public NewBank() {
		customers = new CustomerDB();
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
			if (request.equals("SHOWMYACCOUNTS")) {
				return showMyAccounts(customer );
			} else if (request.startsWith("NEWACCOUNT")) {
				String accountName = request.substring( request.indexOf(" ")+1);
				return makeNewAccount(customer, accountName);
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(Customer customer) {
		return customer.accountsToString();
	}
	
	private String makeNewAccount(Customer customer, String accountName) {
		customer.addAccount( new Account(accountName, 0.0) );
		customers.updateCustomer(customer);
		return "SUCCESS";
	}
	

}
