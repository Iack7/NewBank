package uk.ac.bath.newbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import uk.ac.bath.newbank.model.NewBank;
import uk.ac.bath.newbank.model.roles.Banker;
import uk.ac.bath.newbank.model.roles.Customer;
import uk.ac.bath.newbank.model.roles.User;

public class NewBankClientHandler extends Thread {

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
      while (true) {
        // ask for user name
        out.println("Enter Username");
        String userName = in.readLine();
        // ask for password
        out.println("Enter Password");
        String password = in.readLine();
        out.println("Checking Details...");
        // authenticate user and get customer ID token from bank for use in subsequent requests
        User user = bank.checkLogInDetails(userName, password);
        // if the user is authenticated then get requests from the user and process them
        if (user != null) {
          out.println("Log In Successful. What do you want to do?");
          out.println("Available options: ");
          if (user instanceof Customer) {
            out.println("1. Show Accounts ");
            out.println("\t Usage: SHOWMYACCOUNTS");
            out.println("2. Create New Account");
            out.println("\t Usage: NEWACCOUNT Savings");
            out.println("3. Pay/Transfer Money");
            out.println("\t Usage: PAY John 10");
            out.println(
                "\t \t PAY <Customer> <Amount> "
                    + "\t\t\t:::Default account will be used in this case ");
            out.println("\t Usage: PAY Savings John 10");
            out.println(
                "\t \t PAY <FromAccount> <Customer> <Amount>"
                    + "\t\t\t:::From Customer's Selected account to default account of Recipient  will be used in this case ");
            out.println("\t Usage: PAY Savings John Main 10");
            out.println(
                "\t \t PAY <AccountFrom> <Customer> <AccountNumber> <Amount>"
                    + "\t\t\t:::From Customer's Selected account to  Recipient's account by AccountNumber ");

            out.println("4. Show Pending Transfers");
            out.println("\t Usage: SHOWPENDING");

            out.println("5. Approve Pending Transfer");
            out.println("\t Usage: APPROVE <TransferID> <OneTimePin>");

            out.println("6. Show Transactions");
            out.println("\t Usage: SHOWTRANSACTIONS");

            out.println("7. Move Money ");
            out.println("\t Usage: MOVE <Amount> <From> <To>");

          } else if (user instanceof Banker) {
            out.println("1. Show Accounts ");
            out.println("\t Usage: SHOWMYACCOUNTS <CustomerId>");
            out.println("2. Show Transactions");
            out.println("\t Usage: SHOW_TRANSACTIONS_BY_ACCOUNT <AccountId>");
            out.println("3. Create a new User");
            out.println("\t Usage: CREATENEWUSER <NewUserId> <customer|banker>");
          }

          out.println("6. Change Password");
          out.println("\t Usage: NEWPASSWORD <currentPassword> <newPassword>");

          out.println("0. Logout");
          out.println("\t Usage: LOGOUT");

          boolean continueRequest = true;
          while (continueRequest) {
            String request = in.readLine();
            System.out.println("Request from " + user.getUserID());
            String response = bank.processRequest(user.getUserID(), request);
            if (response.equals("LOGOUT")) {
              user = null;
              continueRequest = false;
            } else {
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
    } finally {
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
