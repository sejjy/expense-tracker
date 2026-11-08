import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
	static final String DB_URL = "jdbc:sqlite:expenses.db";

	static final ArrayList<Expense> expenses = new ArrayList<>();
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
			char option = Input.promptOption(">> ");
			switch (option) {
				case 'a': addExpense();       break;
				case 'v': viewExpenses();     break;
				case 'e': editExpense();      break;
				case 'd': deleteExpense();    break;
				case 'q': connection.close(); return;
				default:
					System.out.println("-- Unknown option --");
					break;
			}
		}
	}

	private static void addExpense() throws SQLException {
		double amount = Input.promptAmount("Add Amount: ");
		String method = Input.promptString("Add Method: ");
		String desc   = Input.promptString("Add Desc. : ");

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

		int number = Input.promptNumber("Edit No.: ", expenses.size());
		Expense expense = expenses.get(number - 1);

		double amount = Input.promptAmount("Edit Amount (Enter to skip): ",
		                                   expense.getAmount());
		String method = Input.promptString("Edit Method (Enter to skip): ",
		                                   expense.getMethod());
		String desc   = Input.promptString("Edit Desc.  (Enter to skip): ",
		                                   expense.getDesc());

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

		int number = Input.promptNumber("Delete No.: ", expenses.size());
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
}
