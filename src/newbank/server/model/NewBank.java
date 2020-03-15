package newbank.server.model;

import newbank.server.database.AccountDB;
import newbank.server.database.CustomerDB;
import newbank.server.database.TransactionDB;

public class NewBank {

  private CustomerDB customers;
  private TransactionDB transactions = TransactionDB.getInstance();
  private AccountDB accountDB = AccountDB.getInstance();

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

      } else if (request.startsWith("SHOWRANSACTIONS")) {
        return showTransactionsByCustomer(customer, request);

      } else if (request.startsWith("NEWACCOUNT")) {
        String accountName = request.substring(request.indexOf(" ") + 1);
        return makeNewAccount(customer, accountName);

      } else if (request.startsWith("PAY")) {
        return initatePay(customer, request);

      } else if (request.startsWith("LOGOUT")) {
        return "LOGOUT";
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
      Account account = customer.getAccount(params[1]);
      return this.showTransactionsByAccount(account);

    } else {
      return customer.showTransactions();
    }
  }

  private String showTransactionsByAccount(Account account) {
    return account.showTransactions();
  }

  private String makeNewAccount(Customer customer, String accountName) {
    customer.addAccount(new Account(accountName, 0.0, customer));
    customers.updateCustomer(customer);
    return "SUCCESS";
  }

  private String initatePay(Customer customer, String request) {
    String[] parameters = request.split(" ");
    if (parameters.length == 3) {
      // PAY John 100
      String toCustomer = parameters[1];
      String amount = parameters[2];

      // Use default account when no account in mentioned.
      Account fromAccount = customer.getDefaultAccount();
      Account toAccount = this.customers.getCustomer(toCustomer).getDefaultAccount();
      try {
        long amountToTransfer = Long.parseLong(amount);
        return transferAmount(fromAccount, toAccount, amountToTransfer);

      } catch (Exception e) {
        return "Invalid Entry";
      }
    }
    if (parameters.length == 4) {
      // PAY Savings John 100
      String fromAccountName = parameters[1];
      String toCustomer = parameters[2];
      String amount = parameters[3];

      // Use default account when no account in mentioned.
      Account fromAccount = customer.getAccount(fromAccountName);
      Customer cust = this.customers.getCustomer(toCustomer);
      if (null == cust) {
        return "Customer not found";
      } else {
        Account toAccount = cust.getDefaultAccount();
        try {
          long amountToTransfer = Long.parseLong(amount);
          return transferAmount(fromAccount, toAccount, amountToTransfer);

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
      Account fromAccount = customer.getAccount(fromAccountName);
      Customer cust = this.customers.getCustomer(toCustomer);
      if (null == cust) {
        return "Customer not found";
      }
      Account toAccount = cust.getAccount(toAccountName);
      if(null == toAccount){
        return  "Account not found";
      } else {
         try {
          long amountToTransfer = Long.parseLong(amount);
          return transferAmount(fromAccount, toAccount, amountToTransfer);

        } catch (Exception e) {
          return "Invalid Entry";
        }
      }
    }

    return "Invalid Entry";
  }

  private String transferAmount(Account fromAccount, Account toAccount, double transferAmount) {
    boolean deductionComplete;
    boolean additionComplete = false;

    try {
      if ((fromAccount.getBalance() > transferAmount && transferAmount > 0)) {
        deductionComplete = fromAccount.debit(transferAmount);
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
}
