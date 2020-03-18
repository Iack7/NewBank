# DESCRIPTION

This is the NewBank Java application.

This document details the protocol for interacting with the NewBank server.  

A customer enters the command below and sees the messages returned:

Command | Description
--------|------------
`SHOWMYACCOUNTS` | Returns a list of all the customers accounts along with their current balance e.g. Main: 1000.0 
`SHOWACCOUNTS <CustomerID>` | Returns all accounts of the customer with the provided customer ID. Only bankers can run this command.
`NEWACCOUNT <Name>` | e.g. `NEWACCOUNT Savings` returns `SUCCESS` or `FAIL`
`MOVE <Amount> <From> <To>` | e.g. `MOVE 100 Main Savings` returns `SUCCESS` or `FAIL`
`PAY <Person/Company> <Ammount>` | e.g. `PAY John 100` returns `SUCCESS` or `FAIL`
