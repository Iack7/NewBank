# NewBank

NewBank is a Java application. This document details the protocol for interacting with the NewBank server. A customer enters the command below and sees the messages returned:


####################################
        For role Customer
####################################
SHOWMYACCOUNTS
Description: Returns a list of all the customers accounts along with their current balance 

NEWACCOUNT <Name>
Description: Creates a new account. Returns SUCCESS or FAIL
Example: NEWACCOUNT Savings

SHOWTRANSACTIONS
Description: List all transactions

MOVE <Amount> <From> <To>
Description: moves money from one account to another. Returns SUCCESS or FAIL
Example: MOVE 100 Main Savings 

PAY <Person/Company> <Ammount>
Description: Pay a person amount of money. Returns SUCCESS or FAIL.
Example: PAY John 100

NEWPASSWORD <OldPassword> <NewPassword>
Description: Sets a new password. To set a new password the user has to provide the old password.
Example: NEWPASSWORD secretWord newSecretWord

LOGOUT
Description: Logout the customer.


####################################
        For role Banker
####################################

SHOWMYACCOUNTS <CustomerID>
Description: Returns a list of all the customers accounts along with their current balance 
Example: SHOWMYACCOUNTS Thomas

SHOWACCOUNTS <CustomerID>
Returns all accounts of the customer with the provided customer ID. Only bankers can run this command.

SHOWTRANSACTIONS <CustomerID>
Description: List all transactions of the chosen customer

SHOW_TRANSACTIONS_BY_ACCOUNT <AccountID>
Description: List all transactions of the chosen account.

LOGOUT
Description: Logout the banker.







## Code Syle

Please install the plugin or the JAR file of the [Google Java Format](https://github.com/google/google-java-format) before you check in the code. This will help the merge process a lot.

If you decide to install the JAR file. Please put it in the root of this directory (don't worry I have added the JAR to ignore list). The following command should be executed:

`java -jar google-java-format-1.7-all-deps.jar --replace src/newbank/server/*.java` NOTE: This command will format only the server part (replace server with client for the client part).
