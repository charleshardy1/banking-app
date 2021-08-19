package View;

import java.util.List;
import java.util.Scanner;

import Controller.Controller;
import Models.Account;
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
				
			}else {
				
			}
			
		}
		scanner.close();
		System.out.printf("Goodbye!\n");
	}
	
	private static response loginRegisterPrompt(Scanner scanner, Controller controller) {
		System.out.printf("Please Select 1 or 2 to make selection, type QUIT to exit.(hit enter to submit)\n");
		System.out.printf("\t1)Login.\n\t2)Register as customer.\n");
		
		String input = scanner.nextLine();
		input = input.trim();
		
		if(input.equals("1")) {
			System.out.printf("Enter username:\n\t");
			String username = scanner.nextLine();
			System.out.printf("Enter Password:\n\t");
			String password = scanner.nextLine();
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
			String username = scanner.nextLine();
			System.out.printf("Enter new Password:\n\t");
			String password = scanner.nextLine();
			System.out.printf("Enter firstname:\n\t");
			String firstname = scanner.nextLine();
			System.out.printf("Enter lastname:\n\t");
			String lastname = scanner.nextLine();
			System.out.printf("Enter email:\n\t");
			String email = scanner.nextLine();
			System.out.printf("Enter phonenumber:\n\t");
			String phonenumber = scanner.nextLine();
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
		
		}else if(input.equals("2")) {
			
		}else if(input.equals("3")) {
			
		}else if(input.equals("4")) {
			
		}else if(input.equals("5")) {
			
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
}
