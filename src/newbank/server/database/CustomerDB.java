package newbank.server.database;
/*
 * This class is a database of all customers. The database allows (i) to add new custmers and (ii) to search the database by username.
 */

import newbank.server.model.Account;
import newbank.server.model.Customer;

import java.util.HashMap;

public class CustomerDB {

  private HashMap<String, Customer> customers;

  public CustomerDB() {
    customers = new HashMap<>();
    addTestData();
  }

  private void addTestData() {
    Customer bhagy = new Customer("bhagy", "secretWord");
    bhagy.addAccount(new Account("Main", 1000.0, bhagy));
    addCustomer(bhagy);

    Customer christina = new Customer("Christina", "PASSWORD!!");
    christina.addAccount(new Account("Savings", 1500.0, christina));
    addCustomer(christina);

    Customer john = new Customer("John", "UniOfBaths");
    john.addAccount(new Account("Savings", 1500.0, john));
    john.addAccount(new Account("Checking", 250.0, john));
    addCustomer(john);
  }

  public void addCustomer(Customer customer) {
    String customerID = customer.getCustomerID();
    customers.put(customerID, customer);
  }

  public void updateCustomer(Customer customer) {
    String customerID = customer.getCustomerID();
    customers.replace(customerID, customer);
  }

  public Customer getCustomer(String customerID) {
    customerID = customerID.toLowerCase();
    return customers.get(customerID);
  }
}
