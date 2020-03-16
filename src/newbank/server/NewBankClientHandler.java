package newbank.server;

import newbank.server.model.Customer;
import newbank.server.model.NewBank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = new NewBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			while(true) {
				// ask for user name
				out.println("Enter Username");
				String userName = in.readLine();
				// ask for password
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				// authenticate user and get customer ID token from bank for use in subsequent requests
				Customer customer = bank.checkLogInDetails(userName, password);
				// if the user is authenticated then get requests from the user and process them 
				if(customer != null) {
					out.println("Log In Successful. What do you want to do?");
					out.println("Available options: ");
					out.println("1. Show Accounts ");
					out.println("\t Usage: SHOWMYACCOUNTS");

					out.println("2. Create New Account");
					out.println("\t Usage: NEWACCOUNT Savings");

					out.println("3. Pay/Transfer Money");
					out.println("\t Usage: PAY John 10");
					out.println("\t \t PAY <Customer> <Amount> "
							+ "\t\t\t:::Default account will be used in this case ");
					out.println("\t Usage: PAY Savings John 10");
					out.println("\t \t PAY <FromAccount> <Customer> <Amount>"
							+ "\t\t\t:::From Customer's Selected account to default account of Recipient  will be used in this case ");
					out.println("\t Usage: PAY Savings John Main 10");
					out.println("\t \t PAY <AccountFrom> <Customer> <AccountNumber> <Amount>"
							+"\t\t\t:::From Customer's Selected account to  Recipient's account by AccountNumber ");

					out.println("4. Show Transactions");
					out.println("\t Usage: SHOWTRANSACTIONS");

					out.println("0. Logout");
					out.println("\t Usage: LOGOUT");

					boolean continueRequest = true;
					while(continueRequest) {
						String request = in.readLine();
						System.out.println("Request from " + customer.getCustomerID());
						String response = bank.processRequest(customer.getCustomerID(), request);
						if(response.equals("LOGOUT")){
							customer = null;
							continueRequest = false;
						} else{
							out.println(response);
						}
					}
				} else {
					out.println("Log In Failed");
					out.println("Please try again!\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
