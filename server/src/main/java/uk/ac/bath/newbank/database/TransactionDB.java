package uk.ac.bath.newbank.database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.bath.newbank.model.Account;
import uk.ac.bath.newbank.model.Transaction;
import uk.ac.bath.newbank.model.roles.Customer;

public class TransactionDB {
  private ArrayList<Transaction> transactions;
  private Long transactionId;
  private static TransactionDB instance;

  private TransactionDB() {
    this.transactions = new ArrayList<>();
    this.transactionId = 0L;
  }

  public static synchronized TransactionDB getInstance() {
    if (instance == null) {
      instance = new TransactionDB();
    }
    return instance;
  }

  public void addTransaction(Transaction transaction) {
    transactionId++;
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
