package newbank.server;
/*
 * This class is a database of all customers. The database allows (i) to add new custmers and (ii) to search the database by username.
 */

import java.util.HashMap;

public class UserDB {
	
	private HashMap<String,User> users;

	public UserDB() {
		users = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		// Password = "secretWord"
		Customer bhagy = new Customer("bhagy", "a11263470cc4ebdcf55c16cc8752e9dc");
		bhagy.addAccount(new Account("Main", 1000.0));
		addUser(bhagy);

		// Password = "PASSWORD!!"
		Customer christina = new Customer("Christina", "4265f8f5fbfe0bfec65323c6207b799e");
		christina.addAccount(new Account("Savings", 1500.0));
		addUser(christina);

		// Password = "UniOfBaths"
		Customer john = new Customer("John", "4de5731b374a36a3ffbd8933bfd095ef");
		john.addAccount(new Account("Checking", 250.0));
		addUser(john);
	}
	
	
	public void addUser(User user) {
		String userID = user.getUserID();
		users.put(userID, user);
	}
	

	public void updateUser(User user) {
		String userID = user.getUserID();
		users.replace(userID, user);
	}

	public User getUser(String userID) {
		userID = userID.toLowerCase();
		return users.get(userID);
	}

}
