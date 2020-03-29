package newbank.server.marketplace;

import newbank.server.marketplace.database.RequestsDB;
import newbank.server.marketplace.model.Request;
import newbank.server.model.roles.User;


public class MarketplaceHandler {
	private static MarketplaceHandler instance;
	private RequestsDB requests;	
	
	public MarketplaceHandler() {
		requests = new RequestsDB();
	}
	
	public static synchronized MarketplaceHandler getInstance() {
	    if (instance == null) {
	      instance = new MarketplaceHandler();
	    }
	    return instance;
	  }
	  
	
	private void addRequest(User user, Double requestedAmount) {
		Request request = new Request(user, requestedAmount);
		requests.add(request);	
	}
	
	private void removeRequest(int RequestID) {
		//We still have to implement this function.
	}
	
	private void acceptRequest() {
		//We still have to implement this function.
	}
	
	
	public String processCommand(User user, String request) {
		String command = request.split("\\s+")[1];
		if (command.equals("ADD") ) {
			double requestedAmount = Double.parseDouble( request.split("\\s+")[2] );
			addRequest(user, requestedAmount);
			return "SUCCESS";
		} else if (command.equals("REMOVE") ) {
			return "Implement a function to remove requests here.";
		} else if (command.equals("SHOW") ) {
			return requests.printRequests();
		} else if (command.equals("ACCEPT")) {
			return "Implement a function to accept requests here.";			
		}

		return "FAIL2";
	}
	
}