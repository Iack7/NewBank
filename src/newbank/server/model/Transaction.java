package newbank.server.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    this.time = Instant.now().toEpochMilli();
  }

  public String getTimeString() {
    DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a").withZone(ZoneId.systemDefault());
    return DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(this.time));
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
    return (this.getTimeString()
        + " \t|\t "
        + fromAccount.getCustomer().getUserID()
        + "\t|\t "
        + fromAccount.getAccountName()
        + "\t|\t "
        + toAccount.getCustomer().getUserID()
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

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
