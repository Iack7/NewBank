# NewBank

NewBank is a Java application. This document details the protocol for interacting with the NewBank server. A customer enters the command below and sees the messages returned:

## Commands for role Customer

| Command | Description | Example
| --- | --- | --- |
| `SHOWMYACCOUNTS` | Description: Returns a list of all the customers accounts along with their current balance |
| `NEWACCOUNT <Name>` | Description: Creates a new account. Returns `SUCCESS` or `FAIL` | Example: `NEWACCOUNT Savings`
| `SHOWTRANSACTIONS` | Description: List all transactions |
| `MOVE <Amount> <From> <To>` | Description: moves money from one account to another. Returns `SUCCESS` or `FAIL` | Example: `MOVE 100 Main Savings` 
| `PAY <Person/Company> <Ammount>` | Description: Pay a person amount of money. Will create a transfer for the transaction that then need to be approved, on success the OTP for approving the transfer will be printed in the server console. | Example: `PAY John 100`
| `NEWPASSWORD <OldPassword> <NewPassword>` | Description: Sets a new password. To set a new password the user has to provide the old password. | Example: `NEWPASSWORD secretWord newSecretWord`
| `SHOWPENDING`| Description: Customers can see their pending transfers. 
| `APPROVE <transactionId> <OTP>` | Description: Approves and performs the transfer, when given the correct OTP. Only the customer of the 'from' account can approve a tranfer. | Example: `APPROVE 1 1234`
| `LOGOUT` | Description: Logout the customer. |


## Commands for role Banker

| Command | Description | Example
| --- | --- | --- |
| `SHOWMYACCOUNTS <CustomerID>` | Description: Returns a list of all the customers accounts along with their current balance | Example: `SHOWMYACCOUNTS Thomas`
| `SHOWACCOUNTS <CustomerID>` | Returns all accounts of the customer with the provided customer ID. Only bankers can run this command. |
| `SHOWTRANSACTIONS <CustomerID>` | Description: List all transactions of the chosen customer |
| `SHOW_TRANSACTIONS_BY_ACCOUNT <AccountID>` | Description: List all transactions of the chosen account. |
| `LOGOUT` | Description: Logout the banker. |


## Code Style

The code formatting is part of maven build process.
