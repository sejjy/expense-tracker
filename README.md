## expense-tracker

Text-based expense tracker in Java

```text
               EXPENSE TRACKER               
[a]dd [v]iew [e]dit [d]elete [!c]ancel [q]uit

>> a
Add Amount: 259
Add Method: Card
Add Desc. : Dinner
-- Expense added --
>> a
Add Amount: 275
Add Method: Card
Add Desc. : Ticket
-- Expense added --
>> v
No.     | Amount      | Method          | Desc.
1       | ₱259.00     | Card            | Dinner
2       | ₱275.00     | Card            | Ticket
>> e
Edit No.: 1
Edit Amount (Enter to skip): 
Edit Method (Enter to skip): Cash
Edit Desc.  (Enter to skip): Lunch
-- Expense updated --
>> e
Edit No.: 2
Edit Amount (Enter to skip): 
Edit Method (Enter to skip): 
Edit Desc.  (Enter to skip): Movie Ticket
-- Expense updated --
>> v
No.     | Amount      | Method          | Desc.
1       | ₱259.00     | Cash            | Lunch
2       | ₱275.00     | Card            | Movie Ticket
>> d
Delete No.: !c
-- Cancelled --
>> q
```

#

### Requirements

- JDK version 11 or newer
  [[download](https://www.oracle.com/java/technologies/downloads/)]

#

### Compile and run

```sh
$ ./compile
$ ./run
```
