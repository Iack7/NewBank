package newbank.server;

public class NewBank {

  private UserDB users;

  public NewBank() {
    users = new UserDB();
  }

  public synchronized User checkLogInDetails(String userID, String password) {
    User user = users.getUser(userID);
    if (user == null) {
      return null;
    } else if (user.checkPassword(password)) {
      return user;
    } else {
      return null;
    }
  }

  // commands from the NewBank customer are processed in this method
  public synchronized String processRequest(String userID, String request) {
    User user = users.getUser(userID);
    if (user != null) {

      // Execute SHOWMYACCOUNTS if the user is a customer
      if (request.equals("SHOWMYACCOUNTS")) {
        if (user.getUserType().equals("customer")) {
          Customer customer = (Customer) user;
          return showMyAccounts(customer);
        }
        // Execute NEWACCOUNT if the user is a customer
      } else if (request.startsWith("NEWACCOUNT")) {
        if (user.getUserType().equals("customer")) {
          Customer customer = (Customer) user;
          String accountName = request.substring(request.indexOf(" ") + 1);
          return makeNewAccount(customer, accountName);
        }
        // Execute SHOWACCOUNTS if the user is a customer
      } else if (request.startsWith("SHOWACCOUNTS")) {
        if (user.getUserType().equals("banker")) {
          return "FAIL"; // Here we have to implement the SHOWACCOUNTS function bankers.
        }
      }
    }
    return "FAIL";
  }

  private String showMyAccounts(Customer customer) {
    return customer.accountsToString();
  }

  private String makeNewAccount(Customer customer, String accountName) {
    customer.addAccount(new Account(accountName, 0.0));
    users.updateUser(customer);
    return "SUCCESS";
  }
}
