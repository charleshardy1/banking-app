package Controller;

import DAO.UserDAO;
import Models.User;

public class Controller {

	public static void main(String[] args) {
		UserDAO userDAO = new UserDAO();
		for(User user : userDAO.users) {
			System.out.println(user.username);
		}
		
		userDAO.users.remove(0);
		
		userDAO.save();
	}

}
