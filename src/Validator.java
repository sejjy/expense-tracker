public class Validator {

	public static boolean amountValid(String s) {
		try {
			double d = Double.parseDouble(s);
			return d > 0.0 && Double.isFinite(d);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean numberValid(String s, int max) {
		try {
			int n = Integer.parseInt(s);
			return n >= 1 && n <= max;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
