import java.sql.SQLException;
import java.util.List;

public class Main {
	private static final String DB_URL = "jdbc:sqlite:expenses.db";

	public static void main(String[] args) {
		try (ExpenseRepository repo = new ExpenseRepository(DB_URL)) {
			System.out.println("               EXPENSE TRACKER               ");
			System.out.println(
				"[a]dd [v]iew [e]dit [d]elete [c!]ancel [q]uit\n");

			for (;;) {
				char option = Input.promptOption(">> ");
				try {
					switch (option) {
						case 'a': addExpense(repo);    break;
						case 'v': viewExpenses(repo);  break;
						case 'e': editExpense(repo);   break;
						case 'd': deleteExpense(repo); break;
						case 'q': return;
						default:
							System.out.println("-- Unknown option --");
							break;
					}
				} catch (Input.CancelException e) {
					System.err.println("-- Cancelled -- ");
				} catch (SQLException e) {
					System.err.println("-- Database error: " +
					                   e.getMessage() + " --");
				}
			}
		} catch (SQLException e) {
			System.err.println("-- Fatal database error: " +
			                   e.getMessage() + " --");
			System.exit(1);
		}
	}

	private static void addExpense(ExpenseRepository repo)
			throws SQLException {
		double amount = Input.promptAmount("Add Amount: ");
		String method = Input.promptString("Add Method: ");
		String desc   = Input.promptString("Add Desc. : ");

		repo.add(amount, method, desc);
		System.out.println("-- Expense added --");
	}

	private static void viewExpenses(ExpenseRepository repo)
			throws SQLException {
		List<Expense> expenses = repo.getAll();
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to view --");
			return;
		}

		System.out.println("No.     | Amount      | Method          | Desc.");
		for (int i = 0; i < expenses.size(); i++) {
			Expense expense = expenses.get(i);
			System.out.printf("%-7d | ₱%-10.2f | %-15s | %s%n",
			                  i + 1,
			                  expense.getAmount(),
			                  expense.getMethod(),
			                  expense.getDesc());
		}
	}

	private static void editExpense(ExpenseRepository repo)
			throws SQLException {
		List<Expense> expenses = repo.getAll();
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to edit --");
			return;
		}

		int number = Input.promptNumber("Edit No.: ", expenses.size());
		Expense expense = expenses.get(number - 1);

		expense.setAmount(Input.promptAmount("Edit Amount (Enter to skip): ",
		                                     expense.getAmount()));
		expense.setMethod(Input.promptString("Edit Method (Enter to skip): ",
		                                     expense.getMethod()));
		expense.setDesc(Input.promptString("Edit Desc.  (Enter to skip): ",
		                                   expense.getDesc()));

		repo.update(expense);
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense(ExpenseRepository repo)
			throws SQLException {
		List<Expense> expenses = repo.getAll();
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to delete --");
			return;
		}

		int number = Input.promptNumber("Delete No.: ", expenses.size());
		repo.delete(expenses.get(number - 1).getId());
		System.out.println("-- Expense deleted --");
	}
}
