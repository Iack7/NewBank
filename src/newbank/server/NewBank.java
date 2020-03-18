package newbank.server;

import java.util.ArrayList;
import java.util.Optional;

public class NewBank {

  private static final String SPACE = "\\s+";

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
          String accountName = request.substring(request.indexOf(SPACE) + 1);
          return makeNewAccount(customer, accountName);
        }
        // Execute SHOWACCOUNTS if the user is a customer
      } else if (request.startsWith("SHOWACCOUNTS")) {
        if (user.getUserType().equals("banker")) {
          return "FAIL"; // Here we have to implement the SHOWACCOUNTS function bankers.
        }
      } else if (request.startsWith("MOVE")) {
        Customer customer = (Customer) user;
        return moveMoney(customer, request.split(SPACE));
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

  /**
   * The command MOVE moves the money between accounts.
   *
   * @param customer to which moves the moeny.
   * @param command MOVE.
   * @return SUCCES or FAIL
   */
  // TODO method is to big should be refactored
  private String moveMoney(Customer customer, String[] command) {
    if (customer == null) {
      System.err.println("MOVE command: Customer is not available!");
      return "FAIL";
    }

    // We return failure if the command doesn't match 4 arguments
    if (command == null || command.length != 4) {
      System.err.println("MOVE command: Only 3 arguments are expected");
      return "FAIL";
    }

    // accessing the array directly is safe
    // declare the variable effective final explicitely, to use them in Lambda Expression
    final String amount = command[1];
    final double parsedAmount;

    try {
      parsedAmount = Double.parseDouble(amount);
    } catch (NumberFormatException nfe) {
      System.err.printf("MOVE command: Expected number but was %s \n", amount);
      return "FAIL";
    }

    final String from = command[2];
    final String to = command[3];

    // TODO: The from and to if detected that there are the same will return SUCCESS, we have to see
    // if we want to allow this.
    if (from.equals(to)) {
      System.err.println("MOVE command: The 'from' and 'to' are the same, no move is performed!");
      return "SUCCESS";
    }

    // TODO: Account uniqeness is still open
    ArrayList<Account> accounts = customer.getAccounts();
    Account fromAccount = findAccount(accounts, from);
    if (fromAccount == null) {
      System.err.printf("MOVE command: An account with the given name: %s was not found!\n", from);
      return "FAIL";
    }

    Account toAccount = findAccount(accounts, to);
    if (toAccount == null) {
      System.err.printf("MOVE command: An account with the given name: %s was not found!\n", to);
      return "FAIL";
    }

    // Final step
    fromAccount.setOpeningBalance(fromAccount.getOpeningBalance() - parsedAmount);
    customer.addAccount(fromAccount);

    toAccount.setOpeningBalance(toAccount.getOpeningBalance() + parsedAmount);
    customer.addAccount(toAccount);

    return "SUCCESS";
  }

  // TODO: Code should be adapted after we define an identity for class Account
  private static Account findAccount(ArrayList<Account> accounts, String accountName) {
    Optional<Account> fromAccount =
        accounts.stream()
            // The account.getAccountName() should be passed to avoid NPE
            .filter(account -> account != null && accountName.equals(account.getAccountName()))
            .findFirst(); // gives an optional

    return fromAccount.isPresent() ? fromAccount.get() : null;
  }
}
