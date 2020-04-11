package uk.ac.bath.newbank;

import java.util.Optional;
import uk.ac.bath.newbank.database.RequestsDB;
import uk.ac.bath.newbank.model.Account;
import uk.ac.bath.newbank.model.Request;
import uk.ac.bath.newbank.model.roles.Customer;

public class MarketplaceHandler {
  private static MarketplaceHandler instance;
  private RequestsDB requests;

  public MarketplaceHandler() {
    requests = new RequestsDB();
  }

  public static synchronized MarketplaceHandler getInstance() {
    if (instance == null) {
      instance = new MarketplaceHandler();
    }
    return instance;
  }

  private void addRequest(Customer customer, Double requestedAmount) {
    Request request = new Request(customer, requestedAmount);
    requests.add(request);
  }

  private boolean removeRequest(Customer customer, int RequestID) {
    // If the request with the provided request ID is from the user, remove.
    Request request = requests.get(RequestID);
    if (request.getRequestor() == customer) {
      requests.remove(RequestID);
      return true;
    }
    return false;
  }

  private String acceptRequest(Customer customer, int RequestID) {
    /*
     * This method is used to accept a loan request and to transfer the requested amount.
     *
     * This method is unfinished. In particular, we do not currently add the transfer to the transactions database.
     * Also, there is currently no way to save the loan and to remind the requestor to repay.
     */
    Request request = requests.get(RequestID);
    Customer requestor = request.getRequestor();
    double requestedAmount = request.getRequestedAmount();

    // Customers cannot accept their own requests
    if (requestor == customer) {
      return "You cannot accept your own request.";
    }
    // Does the user have enough money to pay for the requested amount?

    // Pay requestor the requested amount of money

    // Use default account.
    Optional<Account> fromAccount = customer.getDefaultAccount();
    Optional<Account> toAccount = requestor.getDefaultAccount();
    if (!fromAccount.equals(toAccount)) {
      try {
        return doTransfer(fromAccount, toAccount, requestedAmount);
      } catch (Exception e) {
        return "Invalid Entry";
      }
    } else {
      return "You are transferring to and from the same account";
    }
  }

  private String doTransfer(
      Optional<Account> fromAccount, Optional<Account> toAccount, double amountToTransfer) {
    if (fromAccount.isPresent() && toAccount.isPresent()) {
      return transferAmount(fromAccount.get(), toAccount.get(), amountToTransfer);
    } else {
      return "Account not present";
    }
  }

  private String transferAmount(Account fromAccount, Account toAccount, double transferAmount) {
    /*
     * This code is essentially copied from NewBank.java and likely needs refactoring. Can we shomehow reuse the code?
     */
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

  public String processCommand(Customer customer, String request) {
    String command = request.split("\\s+")[1];
    if (command.equals("ADD")) {
      double requestedAmount = Double.parseDouble(request.split("\\s+")[2]);
      addRequest(customer, requestedAmount);
      return "SUCCESS";
    } else if (command.equals("REMOVE")) {
      int ID = Integer.parseInt(request.split("\\s+")[2]);
      // return Integer.toString( ID );
      if (removeRequest(customer, ID)) {
        return "SUCCESS";
      }
    } else if (command.equals("SHOW")) {
      return requests.printRequests();
    } else if (command.equals("ACCEPT")) {
      int ID = Integer.parseInt(request.split("\\s+")[2]);
      return acceptRequest(customer, ID);
    }

    return "FAIL";
  }
}
