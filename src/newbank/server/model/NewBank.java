package newbank.server.model;

import newbank.server.database.AccountDB;
import newbank.server.database.TransactionDB;
import newbank.server.database.UserDB;
import newbank.server.model.roles.Banker;
import newbank.server.model.roles.Customer;
import newbank.server.model.roles.User;

import java.util.Optional;
import java.util.Set;

public class NewBank {

  private UserDB users;
  private TransactionDB transactions = TransactionDB.getInstance();
  private AccountDB accountDB = AccountDB.getInstance();

  public NewBank() {
    users = new UserDB();
  }

  public synchronized User checkLogInDetails(String userId, String password) {
    User user = users.getUser(userId);
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
      try {
        if (user instanceof Customer) {
          Customer customer = (Customer) user;
          if (request.equals("SHOWMYACCOUNTS")) {
            return showMyAccounts(customer);
          } else if (request.startsWith("NEWACCOUNT")) {
            String accountName = request.substring(request.indexOf(" ") + 1);
            return makeNewAccount(customer, accountName);
          } else if (request.startsWith("SHOWTRANSACTIONS")) {
            return showTransactionsByCustomer(customer, request);
          } else if (request.startsWith("PAY")) {
            return initatePay(customer, request);
          } else if (request.startsWith("MOVE")) {
            return moveMoney(customer, request.split("\\s+"));
          } else if (request.startsWith("NEWPASSWORD")) {
            return setNewPassword(customer, request);
          }
        } else if (user instanceof Banker) {
          if (request.startsWith("SHOWMYACCOUNTS")) {
            String customerId = request.split("\\s+")[1];
            Customer customer = (Customer) users.getUser(customerId);
            return showMyAccounts(customer);
          } else if (request.startsWith("SHOWTRANSACTIONS")) {
            // SHOWTRANSACTIONS account
            String customerId = request.split("\\s+")[1];
            Customer customer = (Customer) users.getUser(customerId);
            return showTransactionsByCustomer(customer,request);
          } else if (request.startsWith("SHOW_TRANSACTIONS_BY_ACCOUNT")) {
            // SHOW_TRANSACTIONS_BY_ACCOUNT account
            String accountId = request.split("\\s+")[1];

            Account toAccount = this.accountDB.getAccountByNumber(accountId);
            if(null != toAccount){
              return showTransactionsByAccount(toAccount);
            }
          } else if (request.startsWith("CREATENEWUSER")) {
              String newUserId = request.split("\\s+")[1];
              String userType = request.split("\\s+")[2];
              return createNewUser(newUserId, userType);
          }
        }

        if (request.startsWith("LOGOUT")) {
          return "LOGOUT";
        }
      } catch (Exception e) {
        return "FAIL";
      }
    }
    return "FAIL";
  }

  private String showMyAccounts(Customer customer) {
    return customer.accountsToString();
  }

  private String showTransactionsByCustomer(Customer customer, String request) {
    String[] params = request.split("\\s");
    if (params.length == 2) {
      Optional<Account> account = customer.getAccount(params[1]);
      return account.isPresent()
          ? this.showTransactionsByAccount(account.get())
          : "Account Not Found";

    } else {
      return customer.showTransactions();
    }
  }

  private String showTransactionsByAccount(Account account) {
    return account.showTransactions();
  }

  private String makeNewAccount(Customer customer, String accountName) {
    boolean success = customer.addAccount(new Account(accountName, 0.0, customer));
    if (success) {
      users.updateUser(customer);
      return "SUCCESS";
    } else {
      return "Account already exists";
    }
  }

  private String initatePay(Customer customer, String request) {
    String[] parameters = request.split(" ");
    if (parameters.length == 3) {
      // PAY John 100
      String toCustomer = parameters[1];
      String amount = parameters[2];

      // Use default account when no account in mentioned.
      Optional<Account> fromAccount = customer.getDefaultAccount();
      Optional<Account> toAccount = this.getCustomer(toCustomer).getDefaultAccount();
      if (!fromAccount.equals(toAccount)) {
        try {
          return this.doTransfer(fromAccount, toAccount, amount);
        } catch (Exception e) {
          return "Invalid Entry";
        }
      } else {
        return "You are transferring to and from the same account";
      }
    }
    if (parameters.length == 4) {
      // PAY Savings John 100
      String fromAccountName = parameters[1];
      String toCustomer = parameters[2];
      String amount = parameters[3];

      // Use default account when no account in mentioned.
      Optional<Account> fromAccount = customer.getAccount(fromAccountName);
      Customer cust = this.getCustomer(toCustomer);
      if (null == cust) {
        return "Customer not found";
      } else {
        Optional<Account> toAccount = cust.getDefaultAccount();
        try {
          return this.doTransfer(fromAccount, toAccount, amount);
        } catch (Exception e) {
          return "Invalid Entry";
        }
      }
    }
    if (parameters.length == 5) {
      // PAY Savings John 100
      String fromAccountName = parameters[1];
      String toCustomer = parameters[2];
      String toAccountName = parameters[3];
      String amount = parameters[4];

      // Use default account when no account in mentioned.
      Optional<Account> fromAccount = customer.getAccount(fromAccountName);
      User user = this.users.getUser(toCustomer);
      if (null == user || !(user instanceof Customer)) {
        return "Customer not found";
      }
      Customer cust = (Customer) user;
      Optional<Account> toAccount = cust.getAccount(toAccountName);
      if (null == toAccount) {
        return "Account not found";
      } else {
        try {
          return this.doTransfer(fromAccount, toAccount, amount);
        } catch (Exception e) {
          return "Invalid Entry";
        }
      }
    }

    return "Invalid Entry";
  }

  private String doTransfer(
      Optional<Account> fromAccount, Optional<Account> toAccount, String amount) {
    double amountToTransfer = Double.parseDouble(amount);
    if (fromAccount.isPresent() && toAccount.isPresent()) {
      return transferAmount(fromAccount.get(), toAccount.get(), amountToTransfer);
    } else {
      return "Account not present";
    }
  }

  private Customer getCustomer(String customerId) {
    User user = this.users.getUser(customerId);
    if (!(user instanceof Customer)) {
      return null;
    }
    Customer cust = (Customer) user;
    return cust;
  }

  private String transferAmount(Account fromAccount, Account toAccount, double transferAmount) {
    boolean deductionComplete;
    boolean additionComplete = false;

    try {
      if ((fromAccount.getBalance() > transferAmount && transferAmount > 0)) {
        deductionComplete = fromAccount.deduct(transferAmount);
      } else {
        return "Insufficient Balance";
      }
      if (deductionComplete) {
        additionComplete = toAccount.credit(transferAmount);
        Transaction transaction = new Transaction(fromAccount, toAccount, transferAmount);
        transactions.addTransaction(transaction);
      }
    } catch (Exception e) {
      return "FAILED";
    }
    if (deductionComplete && additionComplete) {
      return "SUCCESS";
    } else {
      return "FAILED";
    }
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
    try {
      boolean deduct = fromAccount.deduct(parsedAmount);
      boolean credit = toAccount.credit(parsedAmount);
      if (deduct && credit) {
        Transaction transaction = new Transaction(fromAccount, toAccount, parsedAmount);
        transactions.addTransaction(transaction);
      }
    } catch (Exception e) {
      return "FAILED";
    }
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

  // Change password for any user
  private static String setNewPassword(User user, String request) {
    try {
      String currentPassword = request.split(" ")[1];
      String newPassword = request.split(" ")[2];
      return user.setNewPassword(currentPassword, newPassword);
    } catch (ArrayIndexOutOfBoundsException e) {
      return "Failed - incorrect usage (NEWPASSWORD <currentPassword> <newPassword>)";
    }
  }

  public String createNewUser(String userId, String userType) {
    // Check user id is unique
    if (this.users.userExists(userId)) {
      return "Failed to create user - user with user id " + userId + " already exists";
    } else {
      // Create user with default password "password" and add them to the database
        if (userType.equals("customer")) {
           Customer newUser = new Customer(userId, User.getMD5Hash("password"));
            this.users.addUser(newUser);
        } else if (userType.equals("banker")) {
            Banker newUser = new Banker(userId, User.getMD5Hash("password"));
            this.users.addUser(newUser);
        } else {
            return "Failed to create user - Invalid user type given (must be 'customer' or 'banker')";
        }
      return "User " + userId + " of type " + userType + " successfully created with default password 'password'";
    }
  }

  public UserDB getBankUsers() {
    return this.users;
  }


}
