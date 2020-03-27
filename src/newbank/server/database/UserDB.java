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
    // Password = "secretWord"
    Customer bhagy = new Customer("bhagy", "a11263470cc4ebdcf55c16cc8752e9dc");
    bhagy.addAccount(new Account("Main", 1000.0, bhagy));
    addUser(bhagy);

    // Password = "PASSWORD!!"
    Customer christina = new Customer("Christina", "4265f8f5fbfe0bfec65323c6207b799e");
    christina.addAccount(new Account("Savings", 1500.0, christina));
    addUser(christina);

    // Password = "UniOfBaths"
    Customer john = new Customer("John", "4de5731b374a36a3ffbd8933bfd095ef");
    john.addAccount(new Account("Savings", 1500.0, john));
    john.addAccount(new Account("Checking", 250.0, john));
    addUser(john);

    // Password = Donald
    Banker donald = new Banker("Donald", "d6460d863cc7403c4d48eb8682d87784");
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

  public Boolean userExists(String userId) {
    userId = userId.toLowerCase();
    return users.containsKey(userId);
  }

}