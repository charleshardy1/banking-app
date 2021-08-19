package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.AccountDAO;
import DAO.UserDAO;
import Models.Account;
import Models.User;
import Models.UserRole;

public class Controller {
	private AccountDAO accountDAO;
	private UserDAO userDAO;
	private User currUser;
	public Controller() {
		this.accountDAO = new AccountDAO();
		this.userDAO = new UserDAO();
		this.currUser = null;
	}
	
	private void checkLoggedIn() throws Exception {
		if(currUser == null) throw new Exception("must be logged in to use this functionallity");
	}
	
	public void login(String username, String password) throws Exception {
		for(User user : userDAO.users) {
			if(user.username == username && user.password == password) {
				this.currUser = user;
				return;
			}
		}
		
		throw new Exception("login credentials invalid");
	}
	
	public void logout() {
		this.currUser = null;
	}
	
	public void createAccount(Account account) throws Exception {
		checkLoggedIn();
		for(String holder : account.holders) {
			boolean userexists = false;
			for(User user: userDAO.users) {
				if(user.username == holder) {
					userexists = true;
				}
			}
			
			if(!userexists) throw new Exception("can't create account with user that does not exist");
		}
		
		account.verified = false;
		
		accountDAO.accounts.add(account);
		accountDAO.save();
	}

	public void verifyAccount(Account account) throws Exception{
		checkLoggedIn();
		if(currUser.role == UserRole.customer) throw new Exception("must be an employee or admin to approve");
		
		for(Account accountToChange : accountDAO.accounts) {
			if(accountToChange.ID == account.ID) {
				accountToChange.verified = true;
				accountDAO.save();
				return;
			}
		}
	}

	public void moddifyAccount(Account account) throws Exception{
		checkLoggedIn();
		if(currUser.role != UserRole.admin) throw new Exception("must be an admin to moddify account");
		
		for(Account accountToChange : accountDAO.accounts) {
			if(accountToChange.ID == account.ID) {
				accountToChange.ID = account.ID;
				accountToChange.holders = account.holders;
				accountToChange.name = account.name;
				accountToChange.type = account.type;
				accountToChange.dateCreated = account.dateCreated;
				accountToChange.balance = account.balance;
				accountToChange.verified = account.verified;
				accountDAO.save();
				return;
			}
		}
	}
	
	public void removeAccount(Account account) throws Exception{
		checkLoggedIn();
		if(currUser.role == UserRole.customer) throw new Exception("must be an employee or admin to approve");
		
		for(Account accountToRemove : accountDAO.accounts) {
			if(accountToRemove.ID == account.ID) {
				accountDAO.accounts.remove(accountToRemove);
				accountDAO.save();
				return;
			}
		}
	}

	public void makeDeposit(Account account, long amount) throws Exception {
		checkLoggedIn();
		for(Account accountToDeposit : accountDAO.accounts) {
			if(accountToDeposit.ID == account.ID) {
				if(!accountToDeposit.holders.contains(currUser.username)) throw new Exception("you do not own this account");
				accountToDeposit.balance += amount;
				accountDAO.save();
			}
		}
		
	}
	
	public void makeWithdraw(Account account, long amount) throws Exception {
		checkLoggedIn();
		for(Account accountToWithdraw : accountDAO.accounts) {
			if(accountToWithdraw.ID == account.ID) {
				if(!accountToWithdraw.holders.contains(currUser.username)) throw new Exception("you do not own this account");
				if(accountToWithdraw.balance < amount) throw new Exception("insufficient funds");
				accountToWithdraw.balance -= amount;
				accountDAO.save();
			}
		}
		
	}

	public void makeTransfer(Account fromAccount, Account toAccount,long amount) throws Exception {
		checkLoggedIn();
		this.makeWithdraw(fromAccount, amount);
		this.makeDeposit(toAccount, amount);
	}

	public void registerCustomer(User newUser) throws Exception {
		for(User user : userDAO.users) {
			if(user.username == newUser.username) {
				throw new Exception("username is taken");
			}
		}
		newUser.verified = false;
		userDAO.users.add(newUser);
		userDAO.save();
		
	}

	public void verifyUser(User user) throws Exception{
		checkLoggedIn();
		if(currUser.role == UserRole.customer) throw new Exception("must be an employee or admin to approve");
		
		for(User userToChange : userDAO.users) {
			if(userToChange.username == user.username) {
				userToChange.verified = true;
				accountDAO.save();
				return;
			}
		}
	}

	public List<Account> getUserAccounts(User user) throws Exception{
		checkLoggedIn();
		List<Account> userAccounts = new ArrayList<Account>();
		
		for(Account account : accountDAO.accounts) {
			if(account.holders.contains(user.username)) {
				userAccounts.add(account);
			}
		}
		return userAccounts;
	}
	
	public List<Account> getPendingAccounts() throws Exception{
		checkLoggedIn();
		List<Account> pendingAccounts = new ArrayList<Account>();
		
		for(Account account : accountDAO.accounts) {
			if(account.verified == false) {
				pendingAccounts.add(account);
			}
		}
		return pendingAccounts;
	}

	public List<Account> getAllClearedAccounts() throws Exception{
		checkLoggedIn();
		List<Account> allClearedAccounts = new ArrayList<Account>();
		
		for(Account account : accountDAO.accounts) {
			if(account.verified) {
				allClearedAccounts.add(account);
			}
		}
		return allClearedAccounts;
	}

	public List<User> getAllUsers() throws Exception{
		checkLoggedIn();
		List<User> users = new ArrayList<User>();
		for(User user: userDAO.users) {
			users.add(user);
		}
		return users;
	}
}