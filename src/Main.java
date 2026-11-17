import java.sql.SQLException;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		try {
			Database.create();
		} catch (SQLException e) {
			System.err.println("[ERR] Database.create: " + e.getMessage());
			return;
		}

		System.out.println("               EXPENSE TRACKER               ");
		System.out.println("[a]dd [l]ist [e]dit [d]elete [!c]ancel [q]uit\n");
		System.out.println("(*)Required");

		for (;;) {
			try {
				char option = Prompter.promptOption("=> ");
				switch (option) {
					case 'a': Main.addExpense();    break;
					case 'l': Main.listExpenses();  break;
					case 'e': Main.editExpense();   break;
					case 'd': Main.deleteExpense(); break;
					case 'q': return;
					default:
						System.out.println("-- Unknown option --");
						break;
				}
			} catch (Prompter.CancelException e) {
				System.err.println("-- Cancelled --");
			} catch (SQLException e) {
				System.err.println("[ERR] Database: " + e.getMessage());
			}
		}
	}

	private static void addExpense() throws SQLException {
		double amount = Prompter.promptAmount("(*)Add amount: ");
		String method = Prompter.promptRequired("(*)Add method: ");
		String desc   = Prompter.promptRequired("(*)Add desc. : ");

		Database.add(amount, method, desc);
		System.out.println("-- Expense added --");
	}

	private static void listExpenses() throws SQLException {
		List<Expense> list = Database.list();

		if (list.isEmpty()) {
			System.err.println("-- No expenses to list --");
			return;
		}

		System.out.println("No.     | Amount      | Method          | Desc.");

		for (int i = 0; i < list.size(); i++) {
			Expense expense = list.get(i);
			System.out.printf("%-7d | ₱%-10.2f | %-15s | %s%n",
			                  i + 1,
			                  expense.getAmount(),
			                  expense.getMethod(),
			                  expense.getDesc());
		}
	}

	private static void editExpense() throws SQLException {
		List<Expense> list = Database.list();

		if (list.isEmpty()) {
			System.err.println("-- No expenses to edit --");
			return;
		}

		int number = Prompter.promptNumber("(*)Edit no.   : ", list.size());
		Expense expense = list.get(number - 1);

		double amount = Prompter.promptAmount("   Edit amount: ", expense.getAmount());
		String method = Prompter.prompt("   Edit method: ", expense.getMethod());
		String desc   = Prompter.prompt("   Edit desc. : ", expense.getDesc());

		expense.setAmount(amount);
		expense.setMethod(method);
		expense.setDesc(desc);

		Database.edit(expense);
		System.out.println("-- Expense edited --");
	}

	private static void deleteExpense() throws SQLException {
		List<Expense> list = Database.list();

		if (list.isEmpty()) {
			System.err.println("-- No expenses to delete --");
			return;
		}

		int number = Prompter.promptNumber("(*)Delete no.: ", list.size());
		Expense expense = list.get(number - 1);

		Database.delete(expense.getId());
		System.out.println("-- Expense deleted --");
	}
}
