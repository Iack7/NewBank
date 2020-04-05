package newbank.server.marketplace.model;

import newbank.server.model.roles.User;

public class Request {

	User requestor;
	Double requestedAmount;
    
	public Request(User requestor, Double requestedAmount) {
		this.requestor = requestor;
		this.requestedAmount = requestedAmount;
	}
	
	public User getRequestor() {
		return requestor;
	}
	
	public String toString() {
		return (requestor.getUserID()
		        + "\t"
		        + requestedAmount.toString());
	}
	
}