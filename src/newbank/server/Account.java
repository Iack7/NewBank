package newbank.server;

public class Account {

	private long accountNumber;

	private String accountName;
	private double balance;


	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.balance = openingBalance;
	}

	public String toString() {
		return (String.format("%09d", accountNumber  ) + ": " +  accountName + ": " + balance);
	}

	public double getBalance(){
		return this.balance;
	}

 	public boolean credit(double amount) throws Exception{
		boolean completionStatus= false;
		if(amount > 0 ){
			this.balance = this.balance + amount;
			completionStatus = true;
		} else {
			throw new Exception("Invalid Entry");
		}
		return completionStatus;
	}


	public boolean debit(double amount) throws Exception{
		boolean transferComplete= false;

		if(this.balance > amount && amount > 0 ){
			this.balance = this.balance  -  amount;
			transferComplete = true;
		} else {
			if(amount<0){
				throw new Exception("Invalid Entry");
			}
			if(this.balance < amount){
				throw new Exception("Insufficient Balance");
			}
		}
		return transferComplete;
	}


	public String getAccountName() {
		return accountName;
	}

	public long getAccountNumber(){
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber){
		this.accountNumber = accountNumber;
	}
}
