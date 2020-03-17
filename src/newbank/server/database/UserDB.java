package newbank.server.database;
/*
 * This class is a database of all customers. The database allows (i) to add new custmers and (ii) to search the database by username.
 */

import newbank.server.model.Account;
import newbank.server.model.roles.Banker;
import newbank.server.model.roles.Customer;
import newbank.server.model.roles.User;

import java.util.HashMap;

public class UserDB {

  private HashMap<String, User> users;

  public UserDB() {
    users = new HashMap<>();
    addTestData();
  }

  private void addTestData() {
    Customer bhagy = new Customer("bhagy", "secretWord");
    bhagy.addAccount(new Account("Main", 1000.0, bhagy));
    addUser(bhagy);

    Customer christina = new Customer("Christina", "PASSWORD!!");
    christina.addAccount(new Account("Savings", 1500.0, christina));
    addUser(christina);

    Customer john = new Customer("John", "UniOfBaths");
    john.addAccount(new Account("Savings", 1500.0, john));
    john.addAccount(new Account("Checking", 250.0, john));
    addUser(john);
    Banker donald = new Banker("Donald", "Donald");
    addUser(donald);
  }

  public void addUser(User user) {
    String userId = user.getUserID();
    users.put(userId, user);
  }

  public void updateUser(User user) {
    String userId = user.getUserID();
    users.replace(userId, user);
  }

  public User getUser(String userId) {
    userId = userId.toLowerCase();
    return users.get(userId);
  }
}
