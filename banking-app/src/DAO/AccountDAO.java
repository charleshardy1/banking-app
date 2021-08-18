package DAO;

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

import Models.Account;
import Models.AccountType;

public class AccountDAO {
	String srcFile = "./src/DAO/accounts.json";
	public List<Account> accounts;
	
	public AccountDAO(){
		this.accounts = loadAccounts();
	}
	
	public void save() {
		JSONObject jo = new JSONObject();
		
		JSONArray ja = new JSONArray();
		
		for(Account account : accounts) {
			Map accountIn = new LinkedHashMap(8);
			accountIn.put("id", account.ID);
			accountIn.put("holders", account.holders);
			accountIn.put("name", account.name);
			accountIn.put("type", account.type.toString());
			accountIn.put("datecreated", account.dateCreated);
			accountIn.put("balance", account.balance);
			accountIn.put("verified", account.verified);
			ja.add(accountIn);
		}
		
		jo.put("accounts", ja);
		
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
	
	private List<Account> loadAccounts() {
		List<Account> Accounts = new ArrayList<Account>();
		try {
			JSONObject data = (JSONObject)new JSONParser().parse(new FileReader(srcFile));
			//Iterating the contents of the array
			JSONArray jsonArray = (JSONArray) data.get("accounts");
			Iterator<JSONObject> iterator = jsonArray.iterator();
			JSONObject account;
			AccountType type;
			String accountType;
			while(iterator.hasNext()) {
			   
			   account = iterator.next();
			   accountType = (String) account.get("type");
			   if(accountType == "checking") type = AccountType.checking;
			   else type = AccountType.saving;
			   
			   List<String> holders = new ArrayList<String>();
			   Object[] accHolders = ((JSONArray)account.get("holders")).toArray();
			   for(Object holder : accHolders) {
				   holders.add(holder.toString());
			   }
			   
			   
			   Accounts.add(
				   new Account(
					   (String) account.get("id"),
					   holders,
					   (String) account.get("name"),
					   type,
					   (String) account.get("datecreated"),
					   (long) account.get("balance"),
					   (boolean) account.get("verified")
				   )
			   );
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return Accounts;
	}
	
	
}
