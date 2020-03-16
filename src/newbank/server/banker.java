package newbank.server;

import java.util.ArrayList;

public class Banker extends User {
	
	public Banker(String bankerID, String password) {
		super(bankerID, password);
	}
	
	@Override
	public String getUserType() {
		return "banker";
	}
	
	
	/*
	 * Here we have to implement some functionality for the banker.
	 * */
	
}
