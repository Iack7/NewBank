package newbank.server;

import java.util.ArrayList;
import java.util.Optional;

public class Customer {

  private ArrayList<Long> accountNumbers;
  private String customerID;
  private String password;
  private AccountDB accountDB = AccountDB.getInstance();

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
