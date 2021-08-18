package Models;

public class Account {
	public String ID;
	public User [] holders;
	public String name;
	public AccountType type;
	public String dateCreated;
	public double balance;
	public boolean verified;
	
	
	Account(
			String ID,
			User [] holders,
			String name,
			AccountType type,
			String dateCreated,
			int balance,
			boolean verified
	){
		this.ID = ID;
		this.holders = holders;
		this.name = name;
		this.type = type;
		this.dateCreated = dateCreated;
		this.balance = balance;
		this.verified = verified;
	}
}
