import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static ArrayList<Expense> expenses = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("       -- EXPENSE TRACKER --       ");
		System.out.println("[a]dd [v]iew [e]dit [d]elete [q]uit\n");

		for (;;) {
			char code = promptCode(">>> ");
			switch (code) {
				case 'a': addExpense();    break;
				case 'v': viewExpenses();  break;
				case 'e': editExpense();   break;
				case 'd': deleteExpense(); break;
				case 'q': scanner.close(); return;
			}
		}
	}

	private static void addExpense() {
		double amount = promptAmount("Add Amount: ");
		if (amount == -1)
			return;

		String method = promptMethod("Add Method: ");
		if (method == null)
			return;

		String desc = promptDesc("Add Desc.: ");
		if (desc == null)
			return;

		expenses.add(new Expense(amount, method, desc));
		System.out.println("-- Expense added --");
	}

	private static void viewExpenses() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to view --");
			return;
		}

		System.out.println("No.     | Amount      | Method          | Desc.");

		for (int index = 0; index < expenses.size(); index++) {
			Expense expense = expenses.get(index);
			System.out.printf("%-7d | ₱%-10.2f | %-15s | %s\n",
			                  index + 1,
			                  expense.getAmount(),
			                  expense.getMethod(),
			                  expense.getDesc());
		}
	}

	private static void editExpense() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to edit --");
			return;
		}

		int number = promptNumber("Edit No.: ");
		if (number == -1)
			return;

		double amount = promptAmount("Edit Amount: ");
		if (amount == -1)
			return;

		String method = promptMethod("Edit Method: ");
		if (method == null)
			return;

		String desc = promptDesc("Edit Desc.: ");
		if (desc == null)
			return;

		Expense expense = expenses.get(number - 1);
		expense.update(amount, method, desc);
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to delete --");
			return;
		}

		int number = promptNumber("Delete No.: ");
		if (number == -1)
			return;

		expenses.remove(number - 1);
		System.out.println("-- Expense deleted --");
	}

	private static char promptCode(String prompt) {
		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			if (!input.isBlank())
				return input.charAt(0);
		}
	}

	private static int promptNumber(String prompt) {
		int number;

		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			if (input.equals("q"))
				return -1;

			try {
				number = Integer.parseInt(input);
				if (number >= 1 && number <= expenses.size())
					return number;
				System.out.println("-- Enter a valid No. --");
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid No. --");
			}
		}
	}

	private static double promptAmount(String prompt) {
		double amount;

		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			if (input.equals("q"))
				return -1;

			try {
				amount = Double.parseDouble(input);
				if (Double.isFinite(amount) && amount >= 0)
					return amount;
				System.out.println("-- Enter a valid Amount --");
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Amount --");
			}
		}
	}

	private static String promptMethod(String prompt) {
		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			if (input.equals("q"))
				return null;
			if (!input.isBlank())
				return input;
			System.out.println("-- Enter a Method --");
		}
	}

	private static String promptDesc(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine().trim();
		if (input.equals("q"))
			return null;
		return input;
	}
}

class Expense {
	private double amount;
	private String method;
	private String desc;

	Expense(double amount, String method, String desc) {
		update(amount, method, desc);
	}

	public void update(double amount, String method, String desc) {
		this.amount = amount;
		this.method = method;
		this.desc   = desc;
	}

	public double getAmount() { return amount; }
	public String getMethod() { return method; }
	public String getDesc()   { return desc; }
}
