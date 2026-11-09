public class Expense {
	private final int id;
	private double amount;
	private String method;
	private String desc;

	public Expense(int id, double amount, String method, String desc) {
		this.id     = id;
		this.amount = amount;
		this.method = method;
		this.desc   = desc;
	}

	public int    getId()     { return id; }
	public double getAmount() { return amount; }
	public String getMethod() { return method; }
	public String getDesc()   { return desc; }

	public void setAmount(double amount) { this.amount = amount; }
	public void setMethod(String method) { this.method = method; }
	public void setDesc(String desc)     { this.desc   = desc; }
}
