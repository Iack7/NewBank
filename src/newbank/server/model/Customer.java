package newbank.server.model;

import newbank.server.database.AccountDB;
import newbank.server.database.TransactionDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Customer {

  private ArrayList<Long> accountNumbers;
  private String customerID;
  private String password;
  private AccountDB accountDB = AccountDB.getInstance();
  private TransactionDB transactionDB = TransactionDB.getInstance();

  public Customer(String customerID, String password) {
    accountNumbers = new ArrayList<>();
    this.customerID = customerID.toLowerCase();
    this.password = password;
  }

  public String accountsToString() {
    String s = "";
    for (Long a : accountNumbers) {
      Account ac = accountDB.getAccountByNumber(a);
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
                + transactionType
                + "\t| To: "
                + transaction.getToAccount().getCustomer().getCustomerID()
                + "\t| Account Number: "
                + transaction.getToAccount().getAccountNumber()
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
    List<Transaction> transactions = transactionDB.getTransactionsByCustomer(this);

    s = this.printTransaction(transactions);
    return s;
  }

  public void addAccount(Account account) {
    Long accountNumber = accountDB.addAccount(account);
    accountNumbers.add(accountNumber);
  }

  /*
   * A function to set a new password. Password change is only allowed, if the old password is provided.
   * */
  public boolean setNewPassword(String oldPassword, String newPassword) {
    if (oldPassword == this.password) {
      this.password = newPassword;
      return true;
    } else {
      return false;
    }
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  public String getCustomerID() {
    return this.customerID;
  }

  public Account getDefaultAccount() {
    long account = this.accountNumbers.get(0);
    return accountDB.getAccountByNumber(account);
  }

  public Account getAccount(String accountName) {
    Optional<Long> accountNumber =
        this.accountNumbers.stream()
            .filter(
                ac -> accountDB.getAccountByNumber(ac).getAccountName().equals(accountName.trim()))
            .findFirst();
    return accountDB.getAccountByNumber(accountNumber.orElse(this.accountNumbers.get(0)));
  }
}
