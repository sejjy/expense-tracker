import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static ArrayList<Expense> expenses = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("       -- EXPENSE TRACKER --       ");
		System.out.println("[a]dd [v]iew [e]dit [d]elete [q]uit\n");

		for (;;) {
			char code = promptChar(">>> ");
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
		double amount;
		String method;
		String desc;

		if ((amount = promptDouble("Add Amount: ")) ==   -1) return;
		if ((method = promptString("Add Method: ")) == null) return;
		if ((desc   = promptString("Add Desc.: "))  == null) return;

		expenses.add(new Expense(amount, method, desc));
		System.out.println("-- Expense added --");
	}

	private static void viewExpenses() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to view --");
			return;
		}

		System.out.println("No.     | Amount      | Method          | Desc.");

		for (int i = 0; i < expenses.size(); i++) {
			Expense expense = expenses.get(i);
			System.out.printf("%-7d | ₱%-10.2f | %-15s | %s\n",
			                  i + 1,
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

		int index;
		double amount;
		String method;
		String desc;

		if ((index  = promptInt("Edit Expense #: ")) ==   -1) return;
		if ((amount = promptDouble("Edit Amount: ")) ==   -1) return;
		if ((method = promptString("Edit Method: ")) == null) return;
		if ((desc   = promptString("Edit Desc.: "))  == null) return;

		Expense expense = expenses.get(index);
		expense.update(amount, method, desc);
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense() {
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to delete --");
			return;
		}

		int index;
		if ((index = promptInt("Delete Expense #: ")) == -1) return;

		expenses.remove(index);
		System.out.println("-- Expense deleted --");
	}

	private static char promptChar(String prompt) {
		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();

			if (input.isEmpty() || input.length() != 1)
				continue;
			return Character.toLowerCase(input.charAt(0));
		}
	}

	private static int promptInt(String prompt) {
		int index;

		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();

			if (input.equalsIgnoreCase("q"))
				return -1;

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

			return index - 1;
		}
	}

	private static double promptDouble(String prompt) {
		double amount;

		for (;;) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();

			if (input.equalsIgnoreCase("q"))
				return -1;

			try {
				amount = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("-- Invalid Amount --");
				continue;
			}

			if (!Double.isFinite(amount) || amount < 0) {
				System.out.println("-- Invalid Amount --");
				continue;
			}

			return amount;
		}
	}

	private static String promptString(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase("q"))
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
