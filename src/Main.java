import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		ArrayList<Expense> expenses = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		System.out.printf("EXPENSE TRACKER   [a]dd  [v]iew  [e]dit  [d]elete  [q]uit%n%n");

		for (;;) {
			System.out.print(">> ");
			String input = scanner.nextLine().trim();
			if (input.isEmpty() || input.length() != 1)
				continue;

			char code = Character.toLowerCase(input.charAt(0));
			switch (code) {
				case 'a': addExpense(expenses, scanner);    break;
				case 'v': viewExpenses(expenses);           break;
				case 'e': editExpense(expenses, scanner);   break;
				case 'd': deleteExpense(expenses, scanner); break;
				case 'q':
					scanner.close();
					System.out.println("Bye!");
					return;
				default:
					System.out.println("-- Invalid code --");
					break;
			}
		}
	}

	private static void addExpense(ArrayList<Expense> expenses, Scanner scanner) {
		double amount;
		String method, description;

		for (;;) {
			System.out.print("Amount (q to cancel): ");
			String input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase("q"))
				return;
			try {
				amount = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Amount --");
				continue;
			}
			if (!Double.isFinite(amount) || amount <= 0) {
				System.out.println("-- Invalid Amount --");
				continue;
			}

			System.out.print("Method: ");
			method = scanner.nextLine().trim();

			System.out.print("Description: ");
			description = scanner.nextLine().trim();

			expenses.add(new Expense(amount, method, description));
			System.out.println("-- Expense added --");
		}
	}

	private static void viewExpenses(ArrayList<Expense> expenses) {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to view --");
			return;
		}

		System.out.println("#       | Amount      | Method          | Description");
		for (int i = 0; i < expenses.size(); i++) {
			Expense expense = expenses.get(i);
			System.out.printf("%-7d | ₱%-10.2f | %-15s | %s%n",
			                  i + 1,
			                  expense.getAmount(),
			                  expense.getMethod(),
			                  expense.getDescription());
		}
	}

	private static void editExpense(ArrayList<Expense> expenses, Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to edit --");
			return;
		}

		int index;
		String input;

		for (;;) {
			System.out.print("Edit Expense # (q to cancel): ");
			input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase("q"))
				return;
			try {
				index = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Expense # --");
				continue;
			}

			if (index < 1 || index > expenses.size()) {
				System.out.println("-- Expense # not found --");
				continue;
			}
			break;
		}

		double amount;

		for (;;) {
			System.out.print("Amount (q to cancel): ");
			input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase("q"))
				return;
			try {
				amount = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Amount --");
				continue;
			}
			if (!Double.isFinite(amount) || amount <= 0) {
				System.out.println("-- Invalid Amount --");
				continue;
			}
			break;
		}

		System.out.print("Method (q to cancel): ");
		String method = scanner.nextLine().trim();
		if (method.equalsIgnoreCase("q"))
			return;

		System.out.print("Description (q to cancel): ");
		String description = scanner.nextLine().trim();
		if (description.equalsIgnoreCase("q"))
			return;

		Expense expense = expenses.get(index - 1);
		expense.update(amount, method, description);
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense(ArrayList<Expense> expenses, Scanner scanner) {
		int index;

		for (;;) {
			if (expenses.isEmpty()) {
				System.out.println("-- No expenses to delete --");
				return;
			}

			System.out.print("Delete Expense # (q to cancel): ");
			String input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase("q"))
				return;
			try {
				index = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Expense # --");
				continue;
			}

			if (index < 1 || index > expenses.size()) {
				System.out.println("-- Expense # not found --");
				continue;
			}

			expenses.remove(index - 1);
			System.out.println("-- Expense deleted --");
		}
	}
}

class Expense {
	private double amount;
	private String method, description;

	Expense(double amount, String method, String description) {
		this.amount = amount;
		this.method = method;
		this.description = description;
	}

	public double getAmount() { return amount; }
	public String getMethod() { return method; }
	public String getDescription() { return description; }

	public void update(double amount, String method, String description) {
		this.amount = amount;
		this.method = method;
		this.description = description;
	}
}
