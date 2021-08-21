package View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import Controller.Controller;
import Models.Account;
import Models.AccountType;
import Models.User;
import Models.UserRole;

public class BankAppUI {
	private enum response {
		fail,
		succsess,
		exit,
		logout,
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		System.out.printf("Hello welcome Global Banking Solutions!\n");
		Scanner scanner = new Scanner(System.in);
		response status; 
		while(true) {
			if(controller.currUser == null) {
				status = loginRegisterPrompt(scanner, controller);
				System.out.printf("\n");
				if(status == response.exit) {
					break;
				} else if(status == response.fail) {
					continue;
				}
			}
			
			if(controller.currUser.role == UserRole.customer) {
				status = customerPrompt(scanner, controller);
				System.out.printf("\n");
				if(status == response.succsess) {
					continue;
				}else if(status == response.fail) {
					continue;
				}else if(status == response.logout){
					controller.logout();
					continue;
				}
			}else if(controller.currUser.role == UserRole.employee) {
				status = empPrompt(scanner, controller);
				System.out.printf("\n");
				
				if(status == response.succsess) {
					continue;
				}else if(status == response.fail) {
					continue;
				}else if(status == response.logout){
					controller.logout();
					continue;
				}
			}else {
				status = adminPrompt(scanner, controller);
				System.out.printf("\n");
				
				if(status == response.succsess) {
					continue;
				}else if(status == response.fail) {
					continue;
				}else if(status == response.logout){
					controller.logout();
				
				}
			}
		}
		scanner.close();
		System.out.printf("Goodbye!\n");
	}
	
	private static response empPrompt(Scanner scanner, Controller controller) {
		System.out.printf("Please Select 1, 2, 3, or 4 to make selection, type LOGOUT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)View all approved accounts.\n\t2)View all approved customers.\n");
		System.out.printf("\t3)Approve/Deny an Account.\n\t4)Approve/Deny a customer.\n");
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			List<Account> accounts;
			try {
				accounts = controller.getAllClearedAccounts();
				printAccounts(accounts);
			} catch (Exception e) {
				System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
			}
			
		}else if(input.equals("2")) {
			List<User> users;
			try {
				users = controller.getAllClearedUsers().stream().filter((user)->user.role == UserRole.customer).collect(Collectors.toList());
				printUsers(users);
			} catch (Exception e) {
				System.out.printf("Retreval of users Fail!: %s\n",e.getMessage());
			}
		}else if(input.equals("3")) {
			List<Account> userAccounts;
			try {
				userAccounts = controller.getPendingAccounts();
				printAccounts(userAccounts);
				
			} catch (Exception e) {
				System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			int account;
			String approval;
			try {
				
				System.out.printf("Please Select account to make approve/deny(type number):\n\t");
				account = Integer.parseInt(scanner.nextLine().trim());
				account--;
				if(account < 0 || account >= userAccounts.size()) throw new Exception("invalid input");
				System.out.printf("Please enter approve or deny:\n");
				approval = scanner.nextLine().trim();
				if(!(approval.equals("approve") || approval.equals("deny"))) {
					System.out.printf("Invalid input!\n");
					return response.fail;
				}
					
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			if(approval.equals("approve")) {
				
				try {
					controller.verifyAccount(userAccounts.get(account));
					System.out.printf("Approve account Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Approve account Fail!: %s\n",e.getMessage());
					return response.fail;
				}

			}else {
				try {
					controller.removeAccount((userAccounts.get(account)));
					System.out.printf("DenyS account Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Deny account Fail!: %s\n",e.getMessage());
					return response.fail;
				}
				
			}
		}else if(input.equals("4")) {
			List<User> users;
			try {
				users = controller.getAllUsers().stream().filter((user)->!user.verified).collect(Collectors.toList());
				printUsers(users);
				
			} catch (Exception e) {
				System.out.printf("Retreval of users Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			int user;
			String approval;
			try {
				
				System.out.printf("Please select customer to approve/deny(type number):\n\t");
				user = Integer.parseInt(scanner.nextLine().trim());
				user--;
				if(user < 0 || user >= users.size()) throw new Exception("invalid input");
				System.out.printf("Please enter approve or deny:\n");
				approval = scanner.nextLine().trim();
				if(!(approval.equals("approve") || approval.equals("deny"))) {
					System.out.printf("Invalid input!\n");
					return response.fail;
				}
					
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			if(approval.equals("approve")) {
				
				try {
					controller.verifyUser(users.get(user));
					System.out.printf("Approve user Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Approve user Fail!: %s\n",e.getMessage());
					return response.fail;
				}

			}else {
				try {
					controller.removeUser(users.get(user));
					System.out.printf("Deny user Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Deny user Fail!: %s\n",e.getMessage());
					return response.fail;
				}
				
			}
		}else if(input.equals("LOGOUT")) {
			return response.logout;
		}else {
			System.out.printf("Invalid input!\n");
			return response.fail;
		}
		
		return response.succsess;
	}

	private static void printUsers(List<User> users) {
		User user;
		for(int i = 0; i< users.size(); i++) {
			user = users.get(i);
			System.out.printf("%d) Customer name: %s %s\n", i+1, user.firstname, user.lastname);
			System.out.printf("\tUsername: %s\n", user.username);
			System.out.printf("\tEmail: %S\n", user.email);
			System.out.printf("\tPhone: %s\n", user.phonenumber);
			System.out.printf("\tVerified: %s\n", user.verified);
		}
		
	}

	private static response loginRegisterPrompt(Scanner scanner, Controller controller) {
		System.out.printf("Please Select 1 or 2 to make selection, type QUIT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)Login.\n\t2)Register as customer.\n");
		
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			System.out.printf("Enter username:\n\t");
			String username = scanner.nextLine().trim();
			System.out.printf("Enter Password:\n\t");
			String password = scanner.nextLine().trim();
			try {
				controller.login(username, password);
				System.out.printf("Login Succsess!\nWelcome %s %s.\n", controller.currUser.firstname, controller.currUser.lastname);
				return response.succsess;
			}catch(Exception e) {
				System.out.printf("Login Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		} else if(input.equals("2")) {
			System.out.printf("Enter new username:\n\t");
			String username = scanner.nextLine().trim();
			System.out.printf("Enter new Password:\n\t");
			String password = scanner.nextLine().trim();
			System.out.printf("Enter firstname:\n\t");
			String firstname = scanner.nextLine().trim();
			System.out.printf("Enter lastname:\n\t");
			String lastname = scanner.nextLine().trim();
			System.out.printf("Enter email:\n\t");
			String email = scanner.nextLine().trim();
			System.out.printf("Enter phonenumber:\n\t");
			String phonenumber = scanner.nextLine().trim();
			User newUser = new User(username, password, UserRole.customer, firstname, lastname, email, phonenumber, false);
			
			try {
				controller.registerCustomer(newUser);
				System.out.printf("Register Succsess! Please wait for employee or admin to approve account.\n");
				return response.fail;
			}catch(Exception e) {
				System.out.printf("Register Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		}else if( input.equals("QUIT")) {
			return response.exit;
		}else {
			System.out.printf("Invalid input!\n");
			return response.fail;
		}
	}

	private static response customerPrompt(Scanner scanner, Controller controller) {
		System.out.printf("Please Select 1, 2, 3, 4, 5, or 6 to make selection, type LOGOUT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)Create bank account.\n\t2)Create joint bank account.\n");
		System.out.printf("\t3)Withdraw.\n\t4)Deposit.\n");
		System.out.printf("\t5)Transfer.\n\t6)View accounts.\n");
		List<Account> userAccounts;
		try {
			userAccounts = controller.getUserAccounts(controller.currUser);
			
		} catch (Exception e) {
			System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
			return response.fail;
		}
		
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			System.out.printf("Enter account name:\n\t");
			String name = scanner.nextLine().trim();
			AccountType type;
			
			System.out.printf("Enter account type(enter saving or checking):\n\t");
			String typeIn = scanner.nextLine().trim();
			if(typeIn.equals(AccountType.checking.toString())) {
				type = AccountType.checking;
			}else if(typeIn.equals(AccountType.saving.toString())) {
				type = AccountType.saving;
			}else {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			List<String> holders = new ArrayList<String>();
			holders.add(controller.currUser.username);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
			LocalDateTime dateCreated = LocalDateTime.now(); 
			Long balance = (long) 0;
			
			Account newAccount = new Account("some id", holders, name, type, dtf.format(dateCreated) ,balance, false);
			try {
				controller.createAccount(newAccount);
				System.out.printf("Account creation Succsess! Please wait for employee or admin to approve account.\n");
			} catch (Exception e) {
				System.out.printf("Creation of of account Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		}else if(input.equals("2")) {
			System.out.printf("Enter account name:\n\t");
			String name = scanner.nextLine().trim();
			AccountType type;
			
			System.out.printf("Enter account type(enter saving or checking):\n\t");
			String typeIn = scanner.nextLine().trim();
			if(typeIn.equals(AccountType.checking.toString())) {
				type = AccountType.checking;
			}else if(typeIn.equals(AccountType.saving.toString())) {
				type = AccountType.saving;
			}else {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			System.out.printf("Enter other holders (usernames) for the account separated by whitespace:\n\t");
			String[] otherHolders = scanner.nextLine().trim().split("\\s+");
			List<String> holders = new ArrayList<String>();
			holders.add(controller.currUser.username);
			for(String holder:otherHolders) {holders.add(holder);}
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
			LocalDateTime dateCreated = LocalDateTime.now(); 
			Long balance = (long) 0;
			
			Account newAccount = new Account("some id", holders, name, type, dtf.format(dateCreated) ,balance, false);
			
		}else if(input.equals("3")) {
			printAccounts(userAccounts);
			int account;
			long amount;
			try {
				System.out.printf("Please Select account to make withdraw from(type number):\n\t");
				account = Integer.parseInt(scanner.nextLine().trim());
				account--;
				if(account < 0 || account >= userAccounts.size()) throw new Exception("invalid input");
				System.out.printf("Please enter amount to make withdraw from:\n");
				amount = Long.parseLong(scanner.nextLine().trim());
				
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			try {
				controller.makeWithdraw(userAccounts.get(account), amount);
				System.out.printf("Withdraw of %d$ from \"%s\" was a Succsess!\n", amount, userAccounts.get(account).name);
				
			} catch (Exception e) {
				System.out.printf("Withdraw from account Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			
		}else if(input.equals("4")) {
			printAccounts(userAccounts);
			int account;
			long amount;
			try {
				System.out.printf("Please Select account to make deposit to(type number):\n\t");
				account = Integer.parseInt(scanner.nextLine().trim());
				account--;
				if(account < 0 || account >= userAccounts.size()) throw new Exception("invalid input");
				System.out.printf("Please enter amount to make deposit to:\n");
				amount = Long.parseLong(scanner.nextLine().trim());
				
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			try {
				controller.makeDeposit(userAccounts.get(account), amount);
				System.out.printf("Deposit of %d$ to \"%s\" was a Succsess!\n", amount, userAccounts.get(account).name);
				
			} catch (Exception e) {
				System.out.printf("Deposit to account Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		}else if(input.equals("5")) {
			printAccounts(userAccounts);
			int fromAccount;
			int toAccount;
			long amount;
			try {
				System.out.printf("Please Select transfer source account (type number):\n\t");
				fromAccount = Integer.parseInt(scanner.nextLine().trim());
				fromAccount--;
				if(fromAccount < 0 || fromAccount >= userAccounts.size()) throw new Exception("invalid input");
				
				System.out.printf("Please Select transfer destination account (type number):\n\t");
				toAccount = Integer.parseInt(scanner.nextLine().trim());
				toAccount--;
				if(toAccount < 0 || toAccount >= userAccounts.size()) throw new Exception("invalid input");
				
				System.out.printf("Please enter amount to transfer:\n");
				amount = Long.parseLong(scanner.nextLine().trim());
				
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			try {
				controller.makeTransfer(userAccounts.get(fromAccount), userAccounts.get(toAccount), amount);
				System.out.printf("Transfer of %d$ from \"%s\" to \"%s\" was a Succsess!\n", amount, userAccounts.get(fromAccount).name, userAccounts.get(toAccount).name);
				
			} catch (Exception e) {
				System.out.printf("Transfer Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		}else if(input.equals("6")) {
			printAccounts(userAccounts);
		}else if(input.equals("LOGOUT")) {
			return response.logout;
		}else {
			System.out.printf("Invalid input!\n");
			return response.fail;
		}
		
		return response.succsess;
	}
	
	private static void printAccounts(List<Account> userAccounts) {
		Account account;
		for(int i = 0; i< userAccounts.size(); i++) {
			account = userAccounts.get(i);
			System.out.printf("%d) Account name: %s (%s)\n", i+1, account.name, account.ID);
			System.out.printf("\ttype: %s\n", account.type);
			System.out.printf("\tbalance: %02d\n", account.balance);
			System.out.printf("\tholders: %s\n", account.holders);
		}
	}

	private static response adminPrompt(Scanner scanner, Controller controller) {
		System.out.printf("Please Select 1, 2, 3, 4, or 5 to make selection, type LOGOUT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)View all approved accounts.\n\t2)View all approved customers.\n");
		System.out.printf("\t3)Approve/Deny an Account.\n\t4)Approve/Deny a customer.\n");
		System.out.printf("\t5)Modify an Account.\n");
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			List<Account> accounts;
			try {
				accounts = controller.getAllClearedAccounts();
				printAccounts(accounts);
			} catch (Exception e) {
				System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
			}
			
		}else if(input.equals("2")) {
			List<User> users;
			try {
				users = controller.getAllClearedUsers().stream().filter((user)->user.role == UserRole.customer).collect(Collectors.toList());
				printUsers(users);
			} catch (Exception e) {
				System.out.printf("Retreval of users Fail!: %s\n",e.getMessage());
			}
		}else if(input.equals("3")) {
			List<Account> userAccounts;
			try {
				userAccounts = controller.getPendingAccounts();
				printAccounts(userAccounts);
				
			} catch (Exception e) {
				System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			int account;
			String approval;
			try {
				
				System.out.printf("Please Select account to make approve/deny(type number):\n\t");
				account = Integer.parseInt(scanner.nextLine().trim());
				account--;
				if(account < 0 || account >= userAccounts.size()) throw new Exception("invalid input");
				System.out.printf("Please enter approve or deny:\n");
				approval = scanner.nextLine().trim();
				if(!(approval.equals("approve") || approval.equals("deny"))) {
					System.out.printf("Invalid input!\n");
					return response.fail;
				}
					
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			if(approval.equals("approve")) {
				
				try {
					controller.verifyAccount(userAccounts.get(account));
					System.out.printf("Approve account Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Approve account Fail!: %s\n",e.getMessage());
					return response.fail;
				}

			}else {
				try {
					controller.removeAccount((userAccounts.get(account)));
					System.out.printf("Deny account Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Deny account Fail!: %s\n",e.getMessage());
					return response.fail;
				}
				
			}
		}else if(input.equals("4")) {
			List<User> users;
			try {
				users = controller.getAllUsers().stream().filter((user)->!user.verified).collect(Collectors.toList());
				printUsers(users);
				
			} catch (Exception e) {
				System.out.printf("Retreval of users Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			int user;
			String approval;
			try {
				
				System.out.printf("Please select customer to approve/deny(type number):\n\t");
				user = Integer.parseInt(scanner.nextLine().trim());
				user--;
				if(user < 0 || user >= users.size()) throw new Exception("invalid input");
				System.out.printf("Please enter approve or deny:\n");
				approval = scanner.nextLine().trim();
				if(!(approval.equals("approve") || approval.equals("deny"))) {
					System.out.printf("Invalid input!\n");
					return response.fail;
				}
					
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			
			if(approval.equals("approve")) {
				
				try {
					controller.verifyUser(users.get(user));
					System.out.printf("Approve user Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Approve user Fail!: %s\n",e.getMessage());
					return response.fail;
				}

			}else {
				try {
					controller.removeUser(users.get(user));
					System.out.printf("Deny user Succsess!\n");
				} catch (Exception e) {
					System.out.printf("Deny user Fail!: %s\n",e.getMessage());
					return response.fail;
				}
				
			}
			
		}else if(input.equals("5")) {
			List<Account> userAccounts;
			try {
				userAccounts = controller.getAllClearedAccounts();
				printAccounts(userAccounts);
				
			} catch (Exception e) {
				System.out.printf("Retreval of accounts Fail!: %s\n",e.getMessage());
				return response.fail;
			}
			int account;
			try {
				
				System.out.printf("Please Select account to modify(type number):\n\t");
				account = Integer.parseInt(scanner.nextLine().trim());
				account--;
				if(account < 0 || account >= userAccounts.size()) throw new Exception("invalid input");
					
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			modifyPrompt(scanner,controller, userAccounts.get(account));
		}else if(input.equals("LOGOUT")) {
			return response.logout;
		}else {
			System.out.printf("Invalid input!\n");
			return response.fail;
		}
		
		return response.succsess;
	}

	private static response modifyPrompt(Scanner scanner, Controller controller, Account account) {
		System.out.printf("Please Select 1, 2, 3, 4, 5, or 6 to make selection, type LOGOUT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)Change name.\n\t2)Change holders.\n");
		System.out.printf("\t3)Change balance.\n\t4)Change type.\n");
		System.out.printf("\t5)Delete account.\n\t6)Cancel.\n");
		
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			System.out.printf("Enter new name:\n\t");
			String newName = scanner.nextLine().trim();
			account.name = newName;
		}else if(input.equals("2")) {
			System.out.printf("Enter new holders (usernames) for the account separated by whitespace:\n\t");
			String[] otherHolders = scanner.nextLine().trim().split("\\s+");
			List<String> holders = new ArrayList<String>();
			for(String holder:otherHolders) {holders.add(holder);}
			account.holders = holders;
		}else if(input.equals("3")) {
			System.out.printf("Enter new balance:\n\t");
			Long newBal;
			try{
				newBal = Long.parseLong(scanner.nextLine().trim());
				if(newBal<0) throw new Exception("invalid input");
			}catch(Exception e) {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			account.balance = newBal;
		}else if(input.equals("4")) {
			System.out.printf("Enter account new type(enter saving or checking):\n\t");
			String typeIn = scanner.nextLine().trim();
			AccountType type;
			if(typeIn.equals(AccountType.checking.toString())) {
				type = AccountType.checking;
			}else if(typeIn.equals(AccountType.saving.toString())) {
				type = AccountType.saving;
			}else {
				System.out.printf("Invalid input!\n");
				return response.fail;
			}
			account.type = type;
			
		}else if(input.equals("5")) {
			try {
				controller.removeAccount(account);
				System.out.printf("Delete account Succsess!\n");
				return response.succsess;
			} catch (Exception e) {
				System.out.printf("Delete account Fail!: %s\n",e.getMessage());
				return response.fail;
			}
		}else if(input.equals("6")) {
			System.out.printf("Operation canceled");
			return response.succsess;
		}else {
			System.out.printf("Invalid input!\n");
			return response.fail;
		}
		
		try {
			controller.moddifyAccount(account);
			System.out.printf("Moddify account Succsess!\n");
		} catch (Exception e) {
			System.out.printf("Moddify account Fail!: %s\n",e.getMessage());
		}
		return response.succsess;
	}
}
