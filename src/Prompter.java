import java.util.Scanner;

public class Prompter {
	public static class CancelException extends RuntimeException {}
	private static final Scanner stdin = new Scanner(System.in);

	public static String prompt(String message) {
		System.out.print(message);
		String input = stdin.nextLine();
		if (input.equals("!c"))
			throw new CancelException();
		return input;
	}

	public static String prompt(String message, String defaultValue) {
		String input = Prompter.prompt(message);
		if (!input.isBlank())
			return input;
		return defaultValue;
	}

	public static String promptRequired(String message) {
		for (;;) {
			String input = Prompter.prompt(message);
			if (!input.isBlank())
				return input;
			System.err.println("-- Input required --");
		}
	}

	public static char promptOption(String message) {
		for (;;) {
			String input = Prompter.prompt(message);
			if (input.length() == 1)
				return input.charAt(0);
			if (input.length() > 1)
				System.err.println("-- Invalid option --");
		}
	}

	public static double promptAmount(String message) {
		for (;;) {
			String amtStr = Prompter.promptRequired(message);
			if (Validator.amountValid(amtStr))
				return Double.parseDouble(amtStr);
			System.err.println("-- Invalid amount --");
		}
	}

	public static double promptAmount(String message, double defaultValue) {
		for (;;) {
			String amtStr = Prompter.prompt(message, String.valueOf(defaultValue));
			if (Validator.amountValid(amtStr))
				return Double.parseDouble(amtStr);
			System.err.println("-- Invalid amount --");
		}
	}

	public static int promptNumber(String message, int max) {
		for (;;) {
			String numStr = Prompter.promptRequired(message);
			if (Validator.numberValid(numStr, max))
				return Integer.parseInt(numStr);
			System.err.println("-- Invalid no. --");
		}
	}
}
