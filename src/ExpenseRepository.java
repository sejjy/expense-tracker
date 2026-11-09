import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository implements AutoCloseable {
	private final Connection connection;

	public ExpenseRepository(String url) throws SQLException {
		connection = DriverManager.getConnection(url);
		String sql = "CREATE TABLE IF NOT EXISTS expenses(" +
		             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		             "amount REAL NOT NULL, " +
		             "method TEXT NOT NULL, " +
		             "description TEXT NOT NULL)";
		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			connection.close();
			throw e;
		}
	}

	public void add(double amount, String method, String desc) throws SQLException {
		String sql = "INSERT INTO expenses (amount, method, description) VALUES (?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setDouble(1, amount);
			stmt.setString(2, method);
			stmt.setString(3, desc);
			stmt.executeUpdate();
		}
	}

	public List<Expense> getAll() throws SQLException {
		List<Expense> expenses = new ArrayList<>();
		String sql = "SELECT id, amount, method, description FROM expenses ORDER BY id";
		try (Statement stmt = connection.createStatement();
		     ResultSet rset = stmt.executeQuery(sql)) {
			while (rset.next()) {
				expenses.add(
					new Expense(rset.getInt("id"),
				                rset.getDouble("amount"),
				                rset.getString("method"),
				                rset.getString("description")));
			}
		}
		return expenses;
	}

	public void update(Expense expense) throws SQLException {
		String sql = "UPDATE expenses SET amount = ?, method = ?, description = ? WHERE id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setDouble(1, expense.getAmount());
			stmt.setString(2, expense.getMethod());
			stmt.setString(3, expense.getDesc());
			stmt.setInt(4, expense.getId());
			stmt.executeUpdate();
		}
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM expenses WHERE id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	@Override
	public void close() throws SQLException {
		connection.close();
	}
}
