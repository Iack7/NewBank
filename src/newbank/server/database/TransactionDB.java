package newbank.server.database;

import newbank.server.model.Account;
import newbank.server.model.Customer;
import newbank.server.model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDB {
  private ArrayList<Transaction> transactions;
  private Long transactionId;

  private TransactionDB(){
    this.transactions = new ArrayList<>();
    this.transactionId = 0L;
  }
  private static TransactionDB instance;

  public static synchronized TransactionDB getInstance() {
    if (instance == null) {
      instance = new TransactionDB();
    }
    return instance;
  }

  public void addTransaction(Transaction transaction) {
    transactionId ++;
    transaction.setTransactionId(transactionId);
    this.transactions.add(transaction);
  }

  public List<Transaction> getTransactionsByCustomer(Customer customer) {
    return this.transactions.stream()
        .filter(
            t ->
                t.getFromAccount().getCustomer() == customer
                    || t.getToAccount().getCustomer() == customer)
        .collect(Collectors.toList());
  }

  public List<Transaction> getTransactionsByAccount(Account account) {
    return this.transactions.stream()
        .filter(t -> t.getFromAccount() == account || t.getToAccount() == account)
        .collect(Collectors.toList());
  }
}
