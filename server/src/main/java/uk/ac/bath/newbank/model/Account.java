package uk.ac.bath.newbank.model;

import java.util.List;
import java.util.Objects;
import uk.ac.bath.newbank.database.TransactionDB;
import uk.ac.bath.newbank.model.roles.Customer;

public class Account {

  private long accountNumber;
  private String accountName;
  private double balance;
  private TransactionDB transactionDB = TransactionDB.getInstance();

  private Customer customer;

  @Override
  public int hashCode() {
    return Objects.hash(accountName, accountNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Account) {
      Account account = (Account) o;
      return this.accountName != null
          && this.accountName.equals(account.accountName)
          && this.accountNumber == (account.accountNumber);
    }
    return false;
  }

  public Account(String accountName, double openingBalance, Customer customer) {
    this.accountName = accountName;
    this.balance = openingBalance;
    this.customer = customer;
  }

  public String toString() {
    return (customer.getUserID()
        + " : "
        + String.format("%09d", accountNumber)
        + ": "
        + accountName
        + ": "
        + balance);
  }

  public double getBalance() {
    return this.balance;
  }

  public boolean credit(double amount) throws Exception {
    boolean completionStatus = false;
    if (isAmountValid(amount)) {
      this.balance = this.balance + amount;
      completionStatus = true;
    } else {
      throw new Exception("Invalid Entry");
    }
    return completionStatus;
  }

  public boolean deduct(double amount) throws Exception {
    boolean transferComplete = false;

    if (this.balance > amount && isAmountValid(amount)) {
      this.balance = this.balance - amount;
      transferComplete = true;
    } else {
      if (amount < 0) {
        throw new Exception("Invalid Entry");
      }
      if (this.balance < amount) {
        throw new Exception("Insufficient Balance");
      }
    }
    return transferComplete;
  }

  private boolean isAmountValid(double amount) {
    // Not valid if it is smaller than 0
    if (amount <= 0) {
      return false;
    }

    String[] split = Double.toString(amount).split("\\.");
    int decimalLength = split[1].length();

    if (decimalLength > 2) {
      return false;
    }

    return true;
  }

  public String getAccountName() {
    return accountName;
  }

  public long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String printTransaction(List<Transaction> transactions) {
    String s = "";
    for (Transaction transaction : transactions) {
      String transactionType;
      if (transaction.getFromAccount() == this
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
      } else if (transaction.getToAccount() == this
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
                + transaction.getAmount()
                + " \t|\t "
                + transactionType
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
    List<Transaction> transactions = transactionDB.getTransactionsByAccount(this);

    if (!transactions.isEmpty()) {
      s = this.printTransaction(transactions);
    } else {
      s = "No Transactions found for this account";
    }
    return s;
  }

  public String showPendingTransactions() {
    String s = "";
    List<Transaction> transactions = transactionDB.getTransactionsByAccount(this);

    if (!transactions.isEmpty()) {
      for (Transaction t : transactions) {
        if (t.getStatus() == Transaction.Status.COMPLETE) {
          transactions.remove(t);
        }
      }
      if (transactions.isEmpty()) {
        s = "No pending transactions for this account.";
      } else {
        s = this.printTransaction(transactions);
      }
    } else {
      s = "No Transactions found for this account";
    }
    return s;
  }
}
