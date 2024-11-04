package allusers;

import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  


public class AllUsers {
	
	public User[] users = new User[1000];
	
	public AllUsers() {
		initialiseUsers();
	}
	
	public void initialiseUsers() {
		//parsing a CSV file into Scanner class constructor  
		String line = "";  
		String splitBy = ",";  
		int position = 0;

		try   
		{  
		//parsing a CSV file into BufferedReader class constructor  
		BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));  
		while ((line = br.readLine()) != null)   //returns a Boolean value  
		{  
		String[] newuser = line.split(splitBy);    // use comma as separator  
		User userobj=new User(newuser[0], newuser[2]);
		users[position] = userobj;
		position++;
		}
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		}  
		
		
		line = "";  
		splitBy = ",";  
		try   
		{  
		//parsing a CSV file into BufferedReader class constructor  
		BufferedReader br = new BufferedReader(new FileReader("Paient_List.csv"));  
		while ((line = br.readLine()) != null)   //returns a Boolean value  
		{  
		String[] newuser = line.split(splitBy);    // use comma as separator  
		User userobj=new User(newuser[0], "Patient");
		users[position] = userobj;
		position++;
		}
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		}
		
	}
	
}
