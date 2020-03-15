package newbank.server;

import java.util.HashMap;
import java.util.Map;

public class AccountDB {

  private Map<Long, Account> accounts;
  private Long accountId;
  private static AccountDB instance;

  private AccountDB() {}

  public void initializeAccountDB() {
    this.accountId = 1000000L;
    this.accounts = new HashMap<>();
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

  public Long addAccount(Account account) {
    if (accountId == null) {
      accountId = 1291239L;
      accounts = new HashMap<>();
    }
    accountId++;
    Long accountNumber = accountId ;
    account.setAccountNumber(accountNumber);
    accounts.put(accountNumber, account);
    return accountNumber;
  }

  public Account getAccountByNumber(String accountNumber) {
    long number = Long.parseLong(accountNumber);
    return accounts.get(number);
  }

  public Account getAccountByNumber(Long accountNumber) {
    return accounts.get(accountNumber);
  }
}
