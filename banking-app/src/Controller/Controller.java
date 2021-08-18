package Controller;

import DAO.AccountDAO;
import DAO.UserDAO;
import Models.Account;
import Models.User;

public class Controller {

	public static void main(String[] args) {
		AccountDAO accountDAO = new AccountDAO();
		for(Account user : accountDAO.accounts) {
			System.out.println(user.name);
		}
		accountDAO.accounts.get(0).name = accountDAO.accounts.get(0).name+" mod";
		accountDAO.save();
	}

}
