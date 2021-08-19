package View;

import java.util.Scanner;

import Controller.Controller;
import Models.User;
import Models.UserRole;

public class BankAppUI {
	private enum response {
		fail,
		succsess,
		exit
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
				System.out.printf("Login Succsess!\n");
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
			System.out.printf("Invalid input!%s!\n",input);
			return response.fail;
		}
	}

}
