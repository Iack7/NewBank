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
	
	private boolean removeRequest(User user, int RequestID) {
		//If the request with the provided request ID is from the user, remove.
		Request request = requests.get( RequestID );
		if (request.getRequestor() == user) {
			requests.remove( RequestID );
			return True;			
		}
		return False;
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
			int ID = Integer.parseInt( request.split("\\s+")[2] );
			removeRequest(user, ID);
			return "Implement a function to remove requests here.";
		} else if (command.equals("SHOW") ) {
			return requests.printRequests();
		} else if (command.equals("ACCEPT")) {
			return "Implement a function to accept requests here.";			
		}

		return "FAIL2";
	}
	
}