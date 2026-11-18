import java.util.Scanner;

public class Prompter {
	public static class CancelException extends RuntimeException {}
	private static final Scanner in = new Scanner(System.in);

	public static String prompt(String msg) {
		System.out.print(msg);
		String input = in.nextLine();
		if (input.equals("!c"))
			throw new CancelException();
		return input;
	}

	public static String prompt(String msg, String value) {
		System.out.print(msg);
		String input = in.nextLine();
		if (input.isBlank())
			return value;
		if (input.equals("!c"))
			throw new CancelException();
		return input;
	}

	public static char promptOption(String msg) {
		for (;;) {
			String input = Prompter.prompt(msg);
			if (!input.isBlank()) {
				if (input.length() == 1)
					return input.charAt(0);
				System.err.println("-- Invalid option --");
			}
		}
	}

	public static double promptAmount(String msg) {
		for (;;) {
			String input = Prompter.prompt(msg);
			if (Validator.validAmount(input))
				return Double.parseDouble(input);
			System.err.println("-- Invalid amount --");
		}
	}

	public static double promptAmount(String msg, double value) {
		for (;;) {
			String input = Prompter.prompt(msg, String.valueOf(value));
			if (Validator.validAmount(input))
				return Double.parseDouble(input);
			System.err.println("-- Invalid amount --");
		}
	}

	public static int promptNumber(String msg, int max) {
		for (;;) {
			String input = Prompter.prompt(msg);
			if (Validator.validNumber(input, max))
				return Integer.parseInt(input);
			System.err.println("-- Invalid no. --");
		}
	}
}
