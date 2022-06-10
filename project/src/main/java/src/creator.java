package src;

public class creator extends user{
	private String password;
	private String username;
	
	public creator (String username,String password) //constructor Για τον creator
	{
		this.username=username;
		this.password=password;
	}
	public String login(String username,String password)//κανει login ως creator
	{
		String data="select cr_username,cr_password from creator where cr_username='"+username+"'and cr_password='"+password+"'";
		return data;
	}
	public String addcon(String user,String url,int key,String title) //συναρτιση που προσθετει content
	{
		String data="INSERT INTO content VALUES('"+user+"','"+url+"','"+key+"','"+title+"')";
		return data;
	}
	public String delcon(int key,String name) //συναρτιση που διαγραφει περιεχομενο
	{
		String data="DELETE FROM content WHERE id='"+key+"' and cr_username='"+name+"'";
		return data;
	}
	public String viewc() //συναρτιση που κανει προβολη ολων των βιντεο
	{
		String data="SELECT * FROM 	content";
		return data;
	}

}
