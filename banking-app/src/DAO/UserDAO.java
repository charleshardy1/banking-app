package DAO;

import Models.User;
import Models.UserRole;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserDAO {
	String srcFile = "./src/DAO/users.json";
	public List<User> users;
	
	public UserDAO(){
		this.users = loadUsers();
	}
	
	public void save() {
		JSONObject jo = new JSONObject();
		
		JSONArray ja = new JSONArray();
		
		for(User user : users) {
			Map userIn = new LinkedHashMap(8);
			userIn.put("username", user.username);
			userIn.put("password", user.password);
			userIn.put("role", user.role.toString());
			userIn.put("firstname", user.firstname);
			userIn.put("lastname", user.lastname);
			userIn.put("email", user.email);
			userIn.put("phonenumber", user.phonenumber);
			userIn.put("verified", user.verified);
			
			ja.add(userIn);
		}
		
		jo.put("users", ja);
		
		// writing JSON to file:"JSONExample.json" in cwd
        PrintWriter pw;
		try {
			pw = new PrintWriter(srcFile);
			pw.write(jo.toJSONString());
	          
	        pw.flush();
	        pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	private List<User> loadUsers() {
		List<User> users = new ArrayList<User>();
		try {
			JSONObject data = (JSONObject)new JSONParser().parse(new FileReader(srcFile));
			//Iterating the contents of the array
			JSONArray jsonArray = (JSONArray) data.get("users");
			Iterator<JSONObject> iterator = jsonArray.iterator();
			JSONObject user;
			UserRole role;
			String userRole;
			boolean verified;
			while(iterator.hasNext()) {
			   
			   user = iterator.next();
			   userRole = (String) user.get("role");
			   if(userRole == "admin") role = UserRole.admin;
			   else if(userRole == "employee") role = UserRole.employee;
			   else role = UserRole.customer;

			   users.add(
				   new User(
					   (String) user.get("username"),
					   (String) user.get("password"),
					   role,
					   (String) user.get("firstname"),
					   (String) user.get("lastname"),
					   (String) user.get("email"),
					   (String) user.get("phonenumber"),
					   (boolean) user.get("verified")
				   )
			   );
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	
}
