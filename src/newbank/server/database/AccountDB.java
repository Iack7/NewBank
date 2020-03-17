package newbank.server.database;

import newbank.server.model.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountDB {

  private Map<Long, Account> accounts;
  private Long accountId;
  private static AccountDB instance;

  private AccountDB() {
    accountId = 1291239L;
    accounts = new HashMap<>();
  }

  public static synchronized AccountDB getInstance() {
    if (instance == null) {
      instance = new AccountDB();
    }
    return instance;
  }

  public Map<Long, Account> getAccounts() {
    return accounts;
  }

  public Account addAccount(Account account) {
    accountId++;
    Long accountNumber = accountId;
    account.setAccountNumber(accountNumber);
    accounts.put(accountNumber, account);
    return account;
  }

  public Account getAccountByNumber(String accountNumber) {
    long number = Long.parseLong(accountNumber);
    return accounts.get(number);
  }

  public Account getAccountByNumber(Long accountNumber) {
    return accounts.get(accountNumber);
  }
}
