package src;

public class juror extends user {
	
		private String password;
		private String username;
		
		public juror (String username,String password) //CUNSTRACTOR циа том йяитг
		{
			this.username=username;
			this.password=password;
		}
		public String login(String username,String password) //kanei login os kriths
		{
			String data="select username,password from asses where username='"+username+"'and password='"+password+"'";
			return data;
		}
		public String rate(int id,int co_id,int eva,String name) //aksiologei to periexomeno
		{
			String data="INSERT INTO eva VALUES('"+id+"','"+co_id+"','"+eva+"','"+name+"')";
			return data;
		}
		public String delev(int key) //diagrafei mia aksiologish toy
		{
			String data="DELETE FROM eva WHERE id='"+key+"'";
			return data;
		}
		public String viewc() //blepei to content
		{
			String data="SELECT * FROM 	content";
			return data;
		}

}
