package src;

public class admin extends user {
	private String password;
	private String username;
	
	public admin(String username,String password)//constructor για τον admin
	{
		this.password=password;
		this.username=username;
	}
	
	public String login(String username,String password) //κανει login στον admin
	{
		String data="select username,password from admin where username='"+username+"'and password='"+password+"'";
		return data;
	}
	public String addreq(int x,String user,String pass,String info,String username,String category)//προσθετει νεο αιτημα στον πηνακα request
	{
		String data="INSERT INTO request VALUES('"+x+"','"+user+"','"+pass+"','"+info+"','"+username+"','"+category+"')";
		return data;
	}
	public String delcr(String user) //διαγραφει creator
	{
		String data="DELETE FROM creator WHERE cr_username='"+user+"'";
		return data;
	}
	public String addas(String user,String pass,String st)//προσθετει εάν νεο κριτή
	{
		String data="INSERT INTO asses VALUES('"+user+"','"+pass+"','"+st+"')";
		return data;
	}
	public String delas(String user) //διαγραφει εναν κριτη
	{
		String data="DELETE FROM asses WHERE username='"+user+"'";
		return data;
	}
	public String delcont(String key)//διαγραφει content
	{
		String data="DELETE FROM content WHERE id='"+key+"'";
		return data;
	}
	public String viewc() //βλεπει το περιεχομενο
	{
		String data="SELECT * FROM 	content";
		return data;
	}
	public String addcre(String user,String pass,String st)//προσθετει εναν creator
	{
		String data="INSERT INTO creator VALUES('"+user+"','"+pass+"','"+st+"')";
		return data;
	}
	public String delev(int key) //diagrafei mia aksiologish toy
	{
		String data="DELETE FROM eva WHERE id='"+key+"'";
		return data;
	}
	

}
