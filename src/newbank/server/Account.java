package newbank.server;

import java.util.Objects;

public class Account {

  private String accountName;

  private double openingBalance;

  public Account(String accountName, double openingBalance) {
    this.accountName = accountName;
    this.openingBalance = openingBalance;
  }

  /**
   * Getter for Account Name
   *
   * @return the accountName
   */
  public String getAccountName() {
    return accountName;
  }

  /**
   * Getter for Opening Balance
   *
   * @return the openingBalance
   */
  public double getOpeningBalance() {
    return openingBalance;
  }

  /**
   * Setter for Opening Balance
   *
   * @param openingBalance
   */
  public void setOpeningBalance(double openingBalance) {
    this.openingBalance = openingBalance;
  }

  @Override
  public String toString() {
    return (accountName + ": " + openingBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountName);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Account) {
      Account account = (Account) o;
      return this.accountName != null && this.accountName.equals(account.accountName);
    }
    return false;
  }
}
