import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static Connection connect() throws SQLException {
		String url = "jdbc:sqlite:expenses.db";
		return DriverManager.getConnection(url);
	}

	public static void create() throws SQLException {
		String sql = """
			CREATE TABLE IF NOT EXISTS expenses (
				id          INTEGER PRIMARY KEY AUTOINCREMENT,
				amount      REAL NOT NULL,
				method      TEXT NOT NULL,
				description TEXT NOT NULL
			)""";
		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		}
	}

	public static void add(double amount, String method, String desc) throws SQLException {
		String sql = "INSERT INTO expenses (amount, method, description) VALUES (?, ?, ?)";
		try (Connection conn = Database.connect();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDouble(1, amount);
			ps.setString(2, method);
			ps.setString(3, desc);
			ps.executeUpdate();
		}
	}

	public static List<Expense> list() throws SQLException {
		List<Expense> list = new ArrayList<>();
		String sql = "SELECT * FROM expenses ORDER BY id";
		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				list.add(
					new Expense(
						rs.getInt("id"),
						rs.getDouble("amount"),
						rs.getString("method"),
						rs.getString("description")));
			}
		}
		return list;
	}

	public static void edit(Expense expense) throws SQLException {
		String sql = "UPDATE expenses SET amount = ?, method = ?, description = ? WHERE id = ?";
		try (Connection conn = Database.connect();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDouble(1, expense.getAmount());
			ps.setString(2, expense.getMethod());
			ps.setString(3, expense.getDesc());
			ps.setInt(4, expense.getId());
			ps.executeUpdate();
		}
	}

	public static void delete(int id) throws SQLException {
		String sql = "DELETE FROM expenses WHERE id = ?";
		try (Connection conn = Database.connect();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}
