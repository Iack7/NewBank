package newbank.server;

import java.util.Optional;
import java.util.Set;

public class NewBank {

  private static final String SPACE = " ";

  private CustomerDB customers;

  public NewBank() {
    customers = new CustomerDB();
  }

  public synchronized Customer checkLogInDetails(String customerID, String password) {
    Customer customer = customers.getCustomer(customerID);
    if (customer == null) {
      return null;
    } else if (customer.checkPassword(password)) {
      return customer;
    } else {
      return null;
    }
  }

  // commands from the NewBank customer are processed in this method
  public synchronized String processRequest(String customerID, String request) {
    Customer customer = customers.getCustomer(customerID);
    if (customer != null) {
      if (request.equals("SHOWMYACCOUNTS")) {
        return showMyAccounts(customer);
      } else if (request.startsWith("NEWACCOUNT")) {
        String accountName = request.substring(request.indexOf(SPACE) + 1);
        return makeNewAccount(customer, accountName);
      } else if (request.startsWith("MOVE")) {
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
    customers.updateCustomer(customer);
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

    Set<Account> accounts = customer.getAccounts();
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
  private static Account findAccount(Set<Account> accounts, String accountName) {
    Optional<Account> fromAccount =
        accounts.stream()
            // The account.getAccountName() should be passed to avoid NPE
            .filter(account -> account != null && accountName.equals(account.getAccountName()))
            .findFirst(); // gives an optional

    return fromAccount.isPresent() ? fromAccount.get() : null;
  }
}
