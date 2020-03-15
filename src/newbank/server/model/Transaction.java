package newbank.server.model;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
  private long transactionId;
  private long time;
  private Account fromAccount;
  private Account toAccount;
  private double amount;

  public Transaction(Account fromAccount, Account toAccount, double amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
    this.time = Calendar.getInstance().getTimeInMillis();
  }

  public String getTimeString() {
    Date date = new Date();
    date.setTime(this.time);
    return (date.toString());
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Account getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(Account fromAccount) {
    this.fromAccount = fromAccount;
  }

  public Account getToAccount() {
    return toAccount;
  }

  public void setToAccount(Account toAccount) {
    this.toAccount = toAccount;
  }

  public String toString() {
    Date date = new Date();
    date.setTime(this.time);
    return (date.toString()
        + " \t|\t "
        + fromAccount.getCustomer().getCustomerID()
        + "\t|\t "
        + fromAccount.getAccountName()
        + "\t|\t "
        + toAccount.getCustomer().getCustomerID()
        + "\t|\t "
        + toAccount.getAccountName()
        + "\t|\t "
        + amount);
  }

  public long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }
}
