# SimpleBankAccount

- This is a simple Bank Account in html format
- It has a signUp and Login with error handling (basic BCrypt Authentication)
- You can create a new account with some money, deposit money, withdraw money and transfer money to another bank account
- All includes error handling
- It works with Spring boot
- The database is postgres
- To test the program you have to update your database and connection details in application.properties

Upcoming improvements:
- Password and username rules
- Making account details more readable
- Adding a home screen before login or signup 

Updates:
08.10.2024:
- Adding AccountNumber with balance on home screen
- Fixing some bugs

13.10.2024:
- Improving transaction management
- Adding custom exception for Deposit and Withdrawal
- If transaction fails (not sufficient credits or account does not exist), transaction will roll back and throws exception
- using entityManagerFactory to create Session

27.10.2024
- Adding account Details
- By choosing a user account the transfers and balance of this account will show up automatically, sorted by transfer date
- Upcoming: by clicking on the Account in the home screen show account details, but hide the details before

02.11.2014
- showing account details by clicking on the account is now implemented
- cleaning up code
- removing unnecessary code blocks
- making methods more readable, improving some parts of the code
