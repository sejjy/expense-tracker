import java.util.Scanner;

public class Input {
	public static class CancelException extends RuntimeException {}

	static final Scanner scanner = new Scanner(System.in);
	
	public static char promptOption(String message) {
		for (;;) {
			String input = read(message).trim();
			if (input.length() == 1) {
				return input.charAt(0);
			}
			if (input.length() > 1) {
				System.out.println("-- Invalid option --");
			}
		}
	}

	public static int promptNumber(String message, int max) {
		for (;;) {
			Integer number = parseNumber(prompt(message), max);
			if (number != null) {
				return number;
			}
			System.out.println("-- Invalid No. --");
		}
	}

	private static Integer parseNumber(String s, int max) {
		try {
			int number = Integer.parseInt(s.trim());
			if (number >= 1 && number <= max) {
				return number;
			}
		} catch (NumberFormatException e) {
			// Invalid No.
		}
		return null;
	}

	public static double promptAmount(String message, double current) {
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

	public static double promptAmount(String message) {
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

	public static String promptString(String message, String current) {
		String input = prompt(message).trim();
		if (input.isBlank()) {
			return current;
		}
		return input;
	}

	public static String promptString(String message) {
		for (;;) {
			String input = prompt(message).trim();
			if (!input.isBlank()) {
				return input;
			}
			System.out.println("-- Cannot be empty --");
		}
	}

	private static String prompt(String message) {
		String input = read(message).trim();
		if (input.equals("c!")) {
			throw new CancelException();
		}
		return input;
	}

	private static String read(String message) {
		System.out.print(message);
		return scanner.nextLine();
	}
}
