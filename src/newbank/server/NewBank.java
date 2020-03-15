package newbank.server;

import java.util.Optional;

public class NewBank {

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

  private String makeNewAccount(Customer customer, String accountName) {
    customer.addAccount(new Account(accountName, 0.0));
    customers.updateCustomer(customer);
    return "SUCCESS";
  }

  private String initatePay(Customer customer, String request) {
    String[] parameters = request.split(" ");
    if(parameters.length ==3){
      // PAY John 100
      String toCustomer = parameters[1];
      String amount = parameters[2];



      //Use default account when no account in mentioned.
      Account fromAccount = customer.getDefaultAccount();
      Account toAccount = this.customers.getCustomer(toCustomer).getDefaultAccount();
      try{
        long amountToTransfer = Long.parseLong(amount);
        return transferAmount(fromAccount, toAccount, Long.parseLong(amount));

      } catch(Exception e){
        return "Invalid Entry";
      }
    }
    if(parameters.length ==4){
      // PAY Savings John 100
      String fromAccountName = parameters[1];
      String toCustomer = parameters[2];
      String amount = parameters[3];

     //Use default account when no account in mentioned.
      Account fromAccount = customer.getAccount(fromAccountName);
      Account toAccount = this.customers.getCustomer(toCustomer).getDefaultAccount();
      try{
        long amountToTransfer = Long.parseLong(amount);
        return transferAmount(fromAccount, toAccount, Long.parseLong(amount));

      } catch(Exception e){
        return "Invalid Entry";
      }
    }

    return "Invalid Entry";
  }

  private String transferAmount(Account fromAccount, Account toAccount, double transferAmount) {
    boolean deductionComplete ;
    boolean additionComplete = false;

    try {
      if((fromAccount.getBalance() > transferAmount && transferAmount > 0)){
        deductionComplete = fromAccount.debit(transferAmount);
      } else{
        return "Insufficient Balance";
      }
      if (deductionComplete) {
        additionComplete = toAccount.credit(transferAmount);
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
