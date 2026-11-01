## expense-tracker

Text-based expense tracker in Java

```text
          EXPENSE TRACKER          
[a]dd [v]iew [e]dit [d]elete [q]uit

>> v
-- No expenses to view --
>> a
Add Amount: 275
Add Method: Card
Add Desc. : Ticket
-- Expense added --
>> a
Add Amount: 259
Add Method: Card
Add Desc. : Breakfast
-- Expense added --
>> v
No.     | Amount      | Method          | Desc.
1       | ₱275.00     | Card            | Ticket
2       | ₱259.00     | Card            | Breakfast
>> e
Edit No.: 1
Edit Amount (Enter to skip): 
Edit Method (Enter to skip): 
Edit Desc.  (Enter to skip): Movie Ticket
-- Expense updated --
>> e
Edit No.: 2
Edit Amount (Enter to skip): 
Edit Method (Enter to skip): Cash
Edit Desc.  (Enter to skip): Lunch
-- Expense updated --
>> v
No.     | Amount      | Method          | Desc.
1       | ₱275.00     | Card            | Movie Ticket
2       | ₱259.00     | Cash            | Lunch
>> q
```

#

### Compile and run

```sh
$ javac -d bin src/*.java
$ java -cp bin Main
```

#

### To-do

- [ ] Store expenses in a database
