import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static final String DB_URL = "jdbc:sqlite:expenses.db";

	static final ArrayList<Expense> expenses = new ArrayList<>();
	static final Scanner scanner = new Scanner(System.in);
	static Connection connection;

	public static void main(String[] args) throws SQLException {
		connection = DriverManager.getConnection(DB_URL);
		String sql = "CREATE TABLE IF NOT EXISTS expenses" +
		             "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			          "amount REAL NOT NULL, " +
			          "method TEXT NOT NULL, " +
			          "description TEXT NOT NULL)";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		}

		System.out.println("          EXPENSE TRACKER          ");
		System.out.println("[a]dd [v]iew [e]dit [d]elete [q]uit\n");

		for (;;) {
			char option = promptOption(">> ");
			switch (option) {
				case 'a': addExpense();    break;
				case 'v': viewExpenses();  break;
				case 'e': editExpense();   break;
				case 'd': deleteExpense(); break;
				case 'q':
					connection.close();
					scanner.close();
					return;
				default:
					System.out.println("-- Unknown option --");
					break;
			}
		}
	}

	private static void addExpense() throws SQLException {
		double amount = promptAmount("Add Amount: ");
		String method = promptString("Add Method: ");
		String desc   = promptString("Add Desc. : ");

		String sql = "INSERT INTO expenses" +
		             "(amount, method, description) VALUES (?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, amount);
			statement.setString(2, method);
			statement.setString(3, desc);
			statement.executeUpdate();
		}
		System.out.println("-- Expense added --");
	}

	private static void viewExpenses() throws SQLException {
		loadExpenses();
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

	public static void editExpense() throws SQLException {
		loadExpenses();
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to edit --");
			return;
		}

		int number = promptNumber("Edit No.: ");
		Expense expense = expenses.get(number - 1);

		double amount =
			promptAmount("Edit Amount (Enter to skip): ", expense.getAmount());
		String method =
			promptString("Edit Method (Enter to skip): ", expense.getMethod());
		String desc =
			promptString("Edit Desc.  (Enter to skip): ", expense.getDesc());

		expense.setAmount(amount);
		expense.setMethod(method);
		expense.setDesc(desc);
		
		String sql = "UPDATE expenses " +
		             "SET amount = ?, method = ?, description = ? WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, expense.getAmount());
			statement.setString(2, expense.getMethod());
			statement.setString(3, expense.getDesc());
			statement.setInt(4, expense.getId());
			statement.executeUpdate();
		}
		System.out.println("-- Expense updated --");
	}

	private static void deleteExpense() throws SQLException {
		loadExpenses();
		if (expenses.isEmpty()) {
			System.out.println("-- No expenses to delete --");
			return;
		}

		int number = promptNumber("Delete No.: ");
		int id = expenses.get(number - 1).getId();

		String sql = "DELETE FROM expenses WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		System.out.println("-- Expense deleted --");
	}

	private static void loadExpenses() throws SQLException {
		expenses.clear();
		String sql =
			"SELECT id, amount, method, description FROM expenses ORDER BY id";
		try (Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				expenses.add(new Expense(resultSet.getInt("id"),
				                         resultSet.getDouble("amount"),
				                         resultSet.getString("method"),
				                         resultSet.getString("description")));
			}
		}
	}

	private static char promptOption(String message) {
		for (;;) {
			String input = prompt(message).trim();
			if (input.length() == 1) {
				return input.charAt(0);
			}
			if (input.length() > 1) {
				System.out.println("-- Invalid option --");
			}
		}
	}

	private static int promptNumber(String message) {
		for (;;) {
			Integer number = parseNumber(prompt(message));
			if (number != null) {
				return number;
			}
			System.out.println("-- Invalid No. --");
		}
	}

	private static Integer parseNumber(String s) {
		try {
			int number = Integer.parseInt(s.trim());
			if (number >= 1 && number <= expenses.size()) {
				return number;
			}
		} catch (NumberFormatException e) {
			// Invalid No.
		}
		return null;
	}

	private static double promptAmount(String message, double current) {
		for (;;) {
			String input = prompt(message);
			if (input.isBlank()) {
				return current;
			}
			double amount = parseAmount(input);
			if (!Double.isNaN(amount)) {
				return amount;
			}
			System.out.println("-- Invalid Amount --");
		}
	}

	private static double promptAmount(String message) {
		for (;;) {
			double amount = parseAmount(prompt(message));
			if (!Double.isNaN(amount)) {
				return amount;
			}
			System.out.println("-- Invalid Amount --");
		}
	}

	private static double parseAmount(String s) {
		try {
			double amount = Double.parseDouble(s.trim());
			if (amount >= 0.0 && Double.isFinite(amount)) {
				return amount;
			}
		} catch (NumberFormatException e) {
			// Invalid Amount
		}
		return Double.NaN;
	}

	private static String promptString(String message, String current) {
		String input = prompt(message).trim();
		if (input.isBlank()) {
			return current;
		}
		return input;
	}

	private static String promptString(String message) {
		for (;;) {
			String input = prompt(message).trim();
			if (!input.isBlank()) {
				return input;
			}
			System.out.println("-- Cannot be empty --");
		}
	}

	private static String prompt(String message) {
		System.out.print(message);
		return scanner.nextLine();
	}
}
