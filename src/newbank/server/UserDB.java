package newbank.server;

import java.util.HashMap;

/*
 * This class is a database of all customers. The database allows (i) to add new custmers and (ii) to search the database by username.
 */
public class UserDB {

  private HashMap<String, User> users;

  public UserDB() {
    users = new HashMap<>();
    addTestData();
  }

  private void addTestData() {
    Customer bhagy = new Customer("bhagy", "secretWord");
    bhagy.addAccount(new Account("Main", 1000.0));
    addUser(bhagy);

    Customer christina = new Customer("Christina", "PASSWORD!!");
    christina.addAccount(new Account("Savings", 1500.0));
    addUser(christina);

    Customer john = new Customer("John", "UniOfBaths");
    john.addAccount(new Account("Checking", 250.0));
    addUser(john);
  }

  public void addUser(User user) {
    String userID = user.getUserID();
    users.put(userID, user);
  }

  public void updateUser(User user) {
    String userID = user.getUserID();
    users.replace(userID, user);
  }

  public User getUser(String userID) {
    userID = userID.toLowerCase();
    return users.get(userID);
  }
}
