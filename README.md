# NewBank

NewBank is a Java application. This document details the protocol for interacting with the NewBank server. A customer enters the command below and sees the messages returned:

Command | Description
--------|------------
`SHOWMYACCOUNTS` | Returns a list of all the customers accounts along with their current balance e.g. Main: 1000.0 
`SHOWACCOUNTS <CustomerID>` | Returns all accounts of the customer with the provided customer ID. Only bankers can run this command.
`NEWACCOUNT <Name>` | e.g. `NEWACCOUNT Savings` returns `SUCCESS` or `FAIL`
`MOVE <Amount> <From> <To>` | e.g. `MOVE 100 Main Savings` returns `SUCCESS` or `FAIL`
`PAY <Person/Company> <Ammount>` | e.g. `PAY John 100` returns `SUCCESS` or `FAIL`

## Code Syle

Please install the plugin or the JAR file of the [Google Java Format](https://github.com/google/google-java-format) before you check in the code. This will help the merge process a lot.

If you decide to install the JAR file. Please put it in the root of this directory (don't worry I have added the JAR to ignore list). The following command should be executed:

`java -jar google-java-format-1.7-all-deps.jar --replace src/newbank/server/*.java` NOTE: This command will format only the server part (replace server with client for the client part).
