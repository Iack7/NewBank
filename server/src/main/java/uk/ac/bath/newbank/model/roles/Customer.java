package uk.ac.bath.newbank.model.roles;

import java.util.*;
import uk.ac.bath.newbank.database.AccountDB;
import uk.ac.bath.newbank.database.TransactionDB;
import uk.ac.bath.newbank.model.Account;
import uk.ac.bath.newbank.model.Transaction;

public class Customer extends User {

  private Set<Account> accounts;
  private AccountDB accountDB = AccountDB.getInstance();
  private TransactionDB transactionDB = TransactionDB.getInstance();

  public Customer(String userId, String password) {
    super(userId, password);
    accounts = new HashSet<>();
  }

  @Override
  public String getUserType() {
    return "customer";
  }

  public String accountsToString() {
    String s = "";

    for (Account ac : accounts) {
      s += ac.toString() + "\n";
    }
    return s;
  }

  public String printTransaction(List<Transaction> transactions) {
    String s = "";
    for (Transaction transaction : transactions) {
      String transactionType;
      String name;
      if (transaction.getFromAccount().getCustomer() == this
          && transaction.getToAccount().getCustomer()
              != transaction.getFromAccount().getCustomer()) {
        transactionType = "DEBIT";
        s +=
            transaction.getTransactionId()
                + " \t|\t "
                + transaction.getTimeString()
                + " \t|\t "
                + transaction.getAmount()
                + " \t|\t "
                + transactionType
                + "\t| To: "
                + transaction.getToAccount().getCustomer().getUserID()
                + "\t| Account Number: "
                + transaction.getToAccount().getAccountNumber()
                + "\t| Status: "
                + transaction.getStatus()
                + "\t|\t "
                + "\n";
      } else if (transaction.getToAccount().getCustomer() == this
          && transaction.getToAccount().getCustomer()
              != transaction.getFromAccount().getCustomer()) {
        transactionType = "CREDIT";
        s +=
            transaction.getTransactionId()
                + " \t|\t "
                + transaction.getTimeString()
                + " \t|\t "
                + transaction.getAmount()
                + " \t|\t "
                + transactionType
                + "\t| From: "
                + transaction.getFromAccount().getCustomer().getUserID()
                + "\t| Account Number: "
                + transaction.getFromAccount().getAccountNumber()
                + "\t| Status: "
                + transaction.getStatus()
                + "\t|\t "
                + "\n";
      } else if (transaction.getToAccount().getCustomer()
          == transaction.getFromAccount().getCustomer()) {
        transactionType = "MOVED";
        s +=
            transaction.getTransactionId()
                + " \t|\t "
                + transaction.getTimeString()
                + " \t|\t "
                + transaction.getTimeString()
                + " \t|\t "
                + transaction.getAmount()
                + "\t| From: "
                + transaction.getFromAccount().getAccountName()
                + "("
                + transaction.getFromAccount().getAccountNumber()
                + ")"
                + "\t| To: "
                + transaction.getToAccount().getAccountName()
                + "("
                + transaction.getToAccount().getAccountNumber()
                + ")"
                + "\t| Status: "
                + transaction.getStatus()
                + "\t|\t "
                + "\n";
      }
    }
    return s;
  }

  public String showTransactions() {
    String s = "";
    List<Transaction> transactions = transactionDB.getTransactionsByCustomer(this);

    s = this.printTransaction(transactions);
    return s;
  }

  public String showPendingTransactions() {
    String s = "";
    List<Transaction> transactions = transactionDB.getTransactionsByCustomer(this);
    for (Transaction t : transactions) {
      if (t.getStatus() == Transaction.Status.COMPLETE) {
        transactions.remove(t);
      }
    }
    if (transactions.isEmpty()) {
      s = "No pending transactions.";
    } else {
      s = this.printTransaction(transactions);
    }
    return s;
  }

  public boolean addAccount(Account account) {
    boolean exists =
        this.accounts.stream().anyMatch(ac -> ac.getAccountName().equals(account.getAccountName()));
    if (!exists) {
      Account acc = accountDB.addAccount(account);
      accounts.add(acc);
      return true;
    } else {
      return false;
    }
  }

  public Optional<Account> getDefaultAccount() {
    return this.accounts.stream().findFirst();
  }

  public Optional<Account> getAccount(String accountName) {
    return this.accounts.stream()
        .filter(ac -> ac.getAccountName().equals(accountName.trim()))
        .findAny();
  }

  /**
   * Getter for the Accounts.
   *
   * @return the accounts
   */
  public Set<Account> getAccounts() {
    return accounts;
  }
}
