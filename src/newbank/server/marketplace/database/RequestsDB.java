package newbank.server.marketplace.database;

import newbank.server.marketplace.model.Request;
import java.util.ArrayList;

public class RequestsDB {

	ArrayList<Request> requests;
    
	public RequestsDB() {
		requests = new ArrayList<>();
	}
	
	
	public void add(Request request) {
		requests.add(request);
	}
	
	public String printRequests() {
		String s = "Request ID\tCustomer\tRequested amount\n";
		for (int i=0; i<requests.size(); i++) {
			int ID = i+1;
			String row = String.format("%d", ID) +"\t\t"+ requests.get(i).toString();
			s += row+"\n";
		}
		return s;
	}
}