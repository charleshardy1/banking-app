package Models;

public class User {
	String username;
	String password;
	UserRole role;
	String firstname;
	String lastname;
	String email;
	String phonenumber;
	boolean verified;
	
	User(
		String username,
		String password,
		UserRole role,
		String firstname,
		String lastname,
		String email,
		String phonenumber,
		boolean verified
	){
		this.username = username;
		this.password = password;
		this.role = role;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phonenumber = phonenumber;
		this.verified = verified;
	}
}
