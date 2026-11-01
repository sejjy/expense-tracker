import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static ArrayList<Expense> expenses = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("          EXPENSE TRACKER          ");
		System.out.println("[a]dd [v]iew [e]dit [d]elete [q]uit\n");

		for (;;) {
			char option = promptOption(">> ");
			switch (option) {
				case 'a': addExpense();    break;
				case 'v': viewExpenses();  break;
				case 'e': editExpense();   break;
				case 'd': deleteExpense(); break;
				case 'q': scanner.close(); return;
				default:
					System.out.println("-- Unknown option --");
					break;
			}
		}
	}

	private static void addExpense() {
		double amount = promptParseAmount("Add Amount: ");
		String method = promptMethod("Add Method: ");
		String desc   = promptDesc("Add Desc.: ");

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

	public static void editExpense() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to edit --");
			return;
		}

		int number    = promptParseNumber("Edit No.: ");
		double amount = promptParseAmount("Edit Amount: ");
		String method = promptMethod("Edit Method: ");
		String desc   = promptDesc("Edit Desc.: ");

		Expense expense = expenses.get(number - 1);
		expense.setAmount(amount);
		expense.setMethod(method);
		expense.setDesc(desc);
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to delete --");
			return;
		}

		int number = promptParseNumber("Delete No.: ");

		expenses.remove(number - 1);
		System.out.println("-- Expense deleted --");
	}

	private static char promptOption(String prompt) {
		for (;;) {
			String input = promptStr(prompt);
			if (input.length() == 1) {
				return input.charAt(0);
			}
			System.out.println("-- Invalid option --");
		}
	}

	private static int promptParseNumber(String prompt) {
		for (;;) {
			String input = promptStr(prompt);
			if (!isValidNumber(input)) {
				System.out.println("-- Invalid No. --");
				continue;
			}
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("!! Invalid No. !!");
			}
		}
	}

	private static double promptParseAmount(String prompt) {
		for (;;) {
			String input = promptStr(prompt);
			if (!isValidAmount(input)) {
				System.out.println("-- Invalid Amount --");
				continue;
			}
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("!! Invalid Amount !!");
			}
		}
	}

	private static String promptMethod(String prompt) {
		for (;;) {
			String input = promptStr(prompt);
			if (input.isBlank()) {
				System.out.println("-- Method cannot be empty --");
				continue;
			}
			return input;
		}
	}

	private static String promptDesc(String prompt) {
		for (;;) {
			String input = promptStr(prompt);
			if (input.isBlank()) {
				System.out.println("-- Desc. cannot be empty --");
				continue;
			}
			return input;
		}
	}

	private static String promptStr(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	public static boolean isValidNumber(String str) {
		try {
			int number = Integer.parseInt(str);
			return number >= 1 && number <= expenses.size();
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidAmount(String str) {
		try {
			double amount = Double.parseDouble(str);
			return amount >= 0.0 && Double.isFinite(amount);
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
