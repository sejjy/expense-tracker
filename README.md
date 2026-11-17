## expense-tracker

Text-based expense tracker in Java

```text
               EXPENSE TRACKER               
[a]dd [l]ist [e]dit [d]elete [!c]ancel [q]uit

(*)Required
=> a
(*)Add amount: 259
(*)Add method: Card
(*)Add desc. : Dinner
-- Expense added --
=> a
(*)Add amount: 275
(*)Add method: Card
(*)Add desc. : Ticket
-- Expense added --
=> l
No.     | Amount      | Method          | Desc.
1       | ₱259.00     | Card            | Dinner
2       | ₱275.00     | Card            | Ticket
=> e
(*)Edit no.   : 1
   Edit amount: 
   Edit method: Cash
   Edit desc. : Lunch
-- Expense edited --
=> e
(*)Edit no.   : 2
   Edit amount: 
   Edit method: 
   Edit desc. : Movie Ticket
-- Expense edited --
=> l
No.     | Amount      | Method          | Desc.
1       | ₱259.00     | Cash            | Lunch
2       | ₱275.00     | Card            | Movie Ticket
=> d
(*)Delete no.: !c
-- Cancelled --
=> q
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
