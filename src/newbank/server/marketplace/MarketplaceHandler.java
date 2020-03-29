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
	
	private void removeRequest() {
		
	}
	
	private void getRequests() {
		
	}
	
	public String processCommand(User user, String request) {
		String command = request.split("\\s+")[1];
		if (command.equals("ADD") ) {
			double requestedAmount = Double.parseDouble( request.split("\\s+")[2] );
			addRequest(user, requestedAmount);
			return "New Request";
		} else if (command.equals("REMOVE") ) {
			return "Remove Request";
		} else if (command.equals("SHOW") ) {
			return "Show Request";
		}

		return "FAILURE";
	}
	
}