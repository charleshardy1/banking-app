package Models;

import java.util.List;

public class Account {
	public String ID;
	public List<String> holders;
	public String name;
	public AccountType type;
	public String dateCreated;
	public long balance;
	public boolean verified;
	
	
	public Account(
			String ID,
			List<String> holders,
			String name,
			AccountType type,
			String dateCreated,
			long balance,
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
