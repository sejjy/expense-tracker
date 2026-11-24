import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
	private static final Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		try {
			Database.create();
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			return;
		}

		System.out.print("""
			               EXPENSE TRACKER
			[a]dd [l]ist [e]dit [d]elete [!c]ancel [q]uit

			Fields with (*) are required
			""");

		for (;;) {
			try {
				switch (prompt("=> ")) {
					case "a" -> addExpense();
					case "l" -> listExpenses();
					case "e" -> editExpense();
					case "d" -> deleteExpense();
					case "q" -> { return; }
					default  -> System.err.println("-- Unknown option --");
				}
			} catch (CancelException e) {
				System.out.println("-- Cancelled --");
			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
			}
		}
	}

	private static void addExpense() throws SQLException {
		double amount = promptAmount("(*)Add amount: ");
		String method = prompt("   Add method: ");
		String desc   = prompt("   Add desc. : ");

		Database.add(amount, method, desc);
		System.out.println("-- Expense added --");
	}

	private static void listExpenses() throws SQLException {
		List<Expense> list = Database.list();
		if (listEmpty(list, "list")) return;

		System.out.println("No.     | Amount         | Method          | Desc.");

		for (int i = 0; i < list.size(); i++) {
			Expense expense = list.get(i);
			System.out.printf(Locale.US, "%-7d | PHP %-10.2f | %-15s | %s%n",
			                  i + 1,
			                  expense.getAmount(),
			                  expense.getMethod(),
			                  expense.getDesc());
		}
	}

	private static void editExpense() throws SQLException {
		List<Expense> list = Database.list();
		if (listEmpty(list, "edit")) return;

		Expense expense = list.get(promptNumber("(*)Edit no.   : ", list.size()) - 1);

		expense.setAmount(promptAmount("   Edit amount: ", expense.getAmount()));
		expense.setMethod(prompt("   Edit method: ", expense.getMethod()));
		expense.setDesc(prompt("   Edit desc. : ", expense.getDesc()));

		Database.edit(expense);
		System.out.println("-- Expense edited --");
	}

	private static void deleteExpense() throws SQLException {
		List<Expense> list = Database.list();
		if (listEmpty(list, "delete")) return;

		Expense expense = list.get(promptNumber("(*)Delete no.: ", list.size()) - 1);

		Database.delete(expense.getId());
		System.out.println("-- Expense deleted --");
	}

	private static boolean listEmpty(List<Expense> list, String action) {
		if (list.isEmpty()) System.err.println("-- No expenses to " + action + " --");
		return list.isEmpty();
	}

	private static class CancelException extends RuntimeException {}

	private static String prompt(String msg) {
		System.out.print(msg);
		if (!in.hasNextLine()) {
			System.out.println();
			System.exit(1);
		}
		String input = in.nextLine().strip();
		if (input.equals("!c")) throw new CancelException();
		return input;
	}

	private static String prompt(String msg, String defaultValue) {
		String input = prompt(msg);
		return input.isBlank() ? defaultValue : input;
	}

	private static double promptAmount(String msg) {
		return promptAmount(msg, null);
	}

	private static double promptAmount(String msg, Double defaultValue) {
		for (;;) {
			String input = prompt(msg);
			if (input.isBlank() && defaultValue != null) return defaultValue;
			try {
				double amount = Double.parseDouble(input);
				if (amount > 0 && Double.isFinite(amount)) return amount;
			} catch (NumberFormatException ignored) {}
			System.err.println("-- Invalid amount --");
		}
	}

	private static int promptNumber(String msg, int max) {
		for (;;) {
			try {
				int number = Integer.parseInt(prompt(msg));
				if (number >= 1 && number <= max) return number;
			} catch (NumberFormatException ignored) {}
			System.err.println("-- Invalid no. --");
		}
	}
}
