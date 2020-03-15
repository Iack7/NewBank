package newbank.server.model;

import newbank.server.database.TransactionDB;

import java.util.List;

public class Account {

  private long accountNumber;
  private String accountName;
  private double balance;
  private TransactionDB transactionDB = TransactionDB.getInstance();

  private Customer customer;

  public Account(String accountName, double openingBalance, Customer customer) {
    this.accountName = accountName;
    this.balance = openingBalance;
    this.customer = customer;
  }

  public String toString() {
    return (customer.getCustomerID()
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
    if (amount > 0) {
      this.balance = this.balance + amount;
      completionStatus = true;
    } else {
      throw new Exception("Invalid Entry");
    }
    return completionStatus;
  }

  public boolean debit(double amount) throws Exception {
    boolean transferComplete = false;

    if (this.balance > amount && amount > 0) {
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
      String name;
      if (transaction.getFromAccount() == this
              && transaction.getToAccount().getCustomer()
              != transaction.getFromAccount().getCustomer()) {
        transactionType = "DEBIT";
        s +=
                transaction.getTransactionId()
                        + " \t|\t "
                        + transaction.getTimeString()
                        + " \t|\t "
                        + transactionType
                        + "\t| To: "
                        + transaction.getToAccount().getCustomer().getCustomerID()
                        + "\t| Account Number: "
                        + transaction.getToAccount().getAccountNumber()
                        + "\t|\t "
                        + "\n";
      } else if (transaction.getToAccount()  == this
              && transaction.getToAccount().getCustomer()
              != transaction.getFromAccount().getCustomer()) {
        transactionType = "CREDIT";
        s +=
                transaction.getTransactionId()
                        + " \t|\t "
                        + transaction.getTimeString()
                        + " \t|\t "
                        + transactionType
                        + "\t| From: "
                        + transaction.getFromAccount().getCustomer().getCustomerID()
                        + "\t| Account Number: "
                        + transaction.getFromAccount().getAccountNumber()
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
                        + "\t|\t "
                        + "\n";
      }
    }
    return s;
  }

  public String showTransactions() {
    String s = "";
    List<Transaction> transactions = transactionDB.getTransactionsByAccount(this);

    s = this.printTransaction(transactions);
    return s;
  }
}
