package uk.ac.bath.newbank.model;

import uk.ac.bath.newbank.model.roles.Customer;

public class Request {

  Customer requestor;
  Double requestedAmount;

  public Request(Customer requestor, Double requestedAmount) {
    this.requestor = requestor;
    this.requestedAmount = requestedAmount;
  }

  public Customer getRequestor() {
    return requestor;
  }

  public Double getRequestedAmount() {
    return requestedAmount;
  }

  public String toString() {
    return (requestor.getUserID() + "\t" + requestedAmount.toString());
  }
}
