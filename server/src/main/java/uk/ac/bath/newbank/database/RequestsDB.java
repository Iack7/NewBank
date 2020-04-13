package uk.ac.bath.newbank.database;

import java.util.ArrayList;
import uk.ac.bath.newbank.model.Request;

public class RequestsDB {

  ArrayList<Request> requests;

  public RequestsDB() {
    requests = new ArrayList<>();
  }

  public void add(Request request) {
    requests.add(request);
  }

  public Request get(int ID) {
    return requests.get(ID - 1);
  }

  public Request remove(int ID) {
    return requests.remove(ID - 1);
  }

  public String printRequests() {
    String s = "Request ID\tCustomer\tRequested amount\n";
    for (int i = 0; i < requests.size(); i++) {
      int ID = i + 1;
      String row = String.format("%d", ID) + "\t\t" + requests.get(i).toString();
      s += row + "\n";
    }
    return s;
  }
}
