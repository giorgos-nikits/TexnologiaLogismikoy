package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/loginServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DataSource datasource=null;//μεταβλητη για να κανουμε το conect Με την βαση
    admin admin1=new admin("1","1");//δημιουργουμε έναν admin
    String user;//ονομα χρηστη
    String pass;//κωδικος χρηστη
    String info;//κατηγορια χρηστη
    String ad;//ονομα του admin
    String category;//metablhth poy pernei timh creator 'η assessment
    int num=1;
    int i=1;
    String p,p1,p2,p3;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init() throws ServletException //κανουμε conection με την βαση
    {
    	try {
    		InitialContext ctx=new InitialContext();
    		datasource=(DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
    	}
    	catch(Exception e) {throw new ServletException(e.toString());}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");	
		PrintWriter out= response.getWriter();
		out.println("<html>");
		out.println("<head><title>ADMIN</title></head>");
		out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
		String requestType=request.getParameter("requestType");
		
		if(requestType.equalsIgnoreCase("addcre"))//συναρτιση που προσθετει ενα νεο request 
		{
			user=request.getParameter("username");//to ονομα του χρηστη
			pass=request.getParameter("password");//ο κωδικοσ του χρηστη
			info=request.getParameter("info");//η ειδικοτητα του
			ad=request.getParameter("admin");//το ονομα του αδμιν
			category=request.getParameter("category");//ή creator ή assessment
			try 
			{
				Connection co=datasource.getConnection();//kanoyem conection me thn bash
				Statement stmt =co.createStatement();
				String d;
				if(category.equals("creator"))d="select cr_username from creator where cr_username='"+user+"'";//ελεγχουμε εαν ειναι Creator ή assessment
				else d="select username from asses where username='"+user+"'";
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs=doc.executeQuery();
				if(rs.next())//ελεγχουμε εαν υπαρχει αλλως χρηστης με το ιδιο ονομα 
				{
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">THIS NAME IS USED PLEASE CHOOSE ANOTHER</h1>");
					out.println("<a href=\"/project/addcre.html\"><strong>TRY AGAIN</strong></a><br>");
					out.println("<a href=\"/project/index.html\"><strong>EXIT</strong></a>");
				}
				else 
				{//αλλιως τον αποθηκευουμε στην αντιστοιχη βαση 
					int ix=0;
					String x="select max(ID) from request";//βρησκουμε το μεγαλυτερο id του πινακα
					PreparedStatement doc2=co.prepareStatement(x);
					ResultSet rs2=doc2.executeQuery();
					if(rs2.next())
					{ ix=rs2.getInt("max(ID)");ix++;}
					String sql=admin1.addreq(ix,user,pass,info,ad,category);
				    stmt.executeUpdate(sql);
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\"> REQUEST HAS SENDT!</h1><br>");
					out.println("<a href=\"/project/index.html\"><strong>EXIT</strong></a>");
				}
				stmt.close();
				co.close();
				rs.close();
			}catch(Exception e) {out.println(e.toString());}
		}
		else if(requestType.equalsIgnoreCase("delcre")) //συναρτιση που διαγραφει εναν Creator
		{
			user=request.getParameter("username");//pernoyme to onoma poy eedose o xrhsths
			try 
			{
				Connection co=datasource.getConnection();
				Statement stmt =co.createStatement();
				String d="select cr_username from creator where cr_username='"+user+"'";
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs2=doc.executeQuery();
				if(rs2.next())//ean to onoma poy edose o xrhsths antistoixei sse xrhsth tote ton kanei delete
				{
					Connection co2=datasource.getConnection();
					stmt=co2.createStatement();
					String sql=admin1.delcr(user);
					stmt.executeUpdate(sql);
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE CREATOR WAS DELETED</h1>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
					co2.close();
				}
				else //allivs emfanizei to antoistixo minima
				{
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">THIS NAME DOES NOT CORRESPOND TO A USER</h1>");
					out.println("<a href=\"/project/delcre.html\"><strong>TRY AGAIN</strong></a><br>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				stmt.close();
				co.close();
				rs2.close();

			}catch(Exception e) {out.println(e.toString());}	
		}
		else if(requestType.equalsIgnoreCase("delca")) //συναρτιση που διαγραφει εναν assessment
		{
			user=request.getParameter("username");//περνουμε το ονομα που εδωσε ο χρηστης
			try 
			{
				Connection co=datasource.getConnection();
				Statement stmt =co.createStatement();
				String d="select username from asses where username='"+user+"'";
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs=doc.executeQuery();
				if(rs.next())//εαν το ονομα αντοιστιχει σε εναν κριτη τοτε κανει διαγραφει απο την βαση
				{
					Connection co2=datasource.getConnection();
					stmt=co2.createStatement();
					String sql=admin1.delas(user);
					stmt.executeUpdate(sql);
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE CREATOR ASSESSMENT WAS DELETED</h1>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
					co2.close();
				}
				else //αλλιως εμφανιζει το αντοιστειχο μνμ
				{
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">THIS NAME DOES NOT CORRESPOND TO A USER</h1>");
					out.println("<a href=\"/project/delca.html\"><strong>TRY AGAIN</strong></a><br>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				stmt.close();
				co.close();
				rs.close();

			}catch(Exception e) {out.println(e.toString());}	
		}
		else if(requestType.equalsIgnoreCase("delco"))//συναρτιση που διαγραφει περιεχομενο
			
		{
			user=request.getParameter("code");//περνουμε τον κωδικο του βιντεο
			try 
			{
				Connection co=datasource.getConnection();
				Statement stmt =co.createStatement();
				Statement stmt2 =co.createStatement();
				String d="select id from content where id='"+user+"'";
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs=doc.executeQuery();
				if(rs.next())//εαν αντοιστειχει σε βιντεο τοτε το διαγραφουμε
				{
					Connection co2=datasource.getConnection();
					stmt=co2.createStatement();
					String sql=admin1.delcont(user);
					stmt.executeUpdate(sql);
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE CONTENT  WAS DELETED</h1>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
					stmt2=co2.createStatement();
					String sql2="delete from eva where co_id='"+user+"'";
					stmt2.executeUpdate(sql2);
					co2.close();
					
				}
				else //αλλιως εμφανιζουμε μνμ λαθους
				{
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">THIS CODE DOES NOT CORRESPOND TO A CONTENT</h1>");
					out.println("<a href=\"/project/delco.html\"><strong>TRY AGAIN</strong></a><br>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				stmt.close();
				co.close();
				rs.close();

			}catch(Exception e) {out.println(e.toString());}	
		}
		else if(requestType.equalsIgnoreCase("viewreq")) //συναρτιση που βλεπει τα request
		{
			HttpSession session=request.getSession();
			try 
			{
				Connection co=datasource.getConnection();
				String d="select * from request where username='"+session.getAttribute("user")+"'";//query που συλεγει τα δεδομενα απο τον πινακα
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs=doc.executeQuery();
				out.println("<h3 style=\"text-align:center;font-family:Rockwell;color:#2b6777;font-size:25px;\"><b>REQUEST FOR ADMIN"+" : "+session.getAttribute("user")+"</b></h3>");
				while(rs.next()) //βαζουμε ενα while για να εμφανισουμε ολα τα στοιχεια του πινακα με το out.println
				{
					 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> USERNAME :</b></h3>");					  
			    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs.getString("user")+" </b></h3>");
			    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>CATEGORY :</b></h3>");					   
			    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs.getString("category")+" </b></h3>");
			    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> CONTENT TYPE :</b></h3>");					 
				     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs.getString("info")+" </b></h3>");
				     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>USER CODE :</b></h3>");					 
				     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs.getString("id")+" </b></h3>");
				     out.println("<a style=\"font-family:Rockwell;color:red;font-size:25px\" href=\"/project/adduser.html\">ACCEPT REQUEST</a><br>");
				     out.println("<hr style=\"width:100%;border-top: 3px solid green;\">");
				}
		
			     out.println("<br>");
				 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				
			}catch(SQLException e) {System.out.println(e.toString());}
		}
		else if(requestType.equalsIgnoreCase("acceptr")) //συναρτιση που κανει accept ενα request
		{
			String sql="";
			String del1="";
			String x=request.getParameter("code");//περνουμε τον κωδικο του request που εδωσε ο χρηστησ
			try 
			{
				Connection co=datasource.getConnection();
				Statement stmt =co.createStatement();
				String d="select * from request where id='"+x+"'";
				PreparedStatement doc=co.prepareStatement(d);
				ResultSet rs=doc.executeQuery();
				if(rs.next())//εαν χρησιμοποιειται τοτε κανουμε accept
				{
					Connection co2=datasource.getConnection();
					stmt=co2.createStatement();
					//αναλογα με την κατηγορια του request το προσθετουμε στον αντοιστειχο πινακα και το κανουμε delete σπο τοn request
					if(rs.getString("category").equals("creator")) {sql=admin1.addcre(rs.getString("user"),rs.getString("pass"),rs.getString("info")); del1="DELETE FROM request WHERE id='"+x+"'";}
					else {sql=admin1.addas(rs.getString("user"),rs.getString("pass"),rs.getString("info")); del1="DELETE FROM request WHERE id='"+x+"'";}
					stmt.executeUpdate(sql);
					stmt.executeUpdate(del1);
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE USER WAS ADDED</h1><br>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				else //αλλιως εμφανιζουμε μνμ λαθους
				{
					out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">WRONG CODE</h1>");
					out.println("<a href=\"/project/adduser.html\"><strong>TRY AGAIN</strong></a><br>");
					out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				stmt.close();
				co.close();
				rs.close();

			}catch(Exception e) {out.println(e.toString());}
			
		}
			else if(requestType.equalsIgnoreCase("viewcon"))//συναρτιση που εμφανιζει ολα τα βιντεο του site
			{
				try 
				{
					 Connection conn=datasource.getConnection();
					 String data="select * from content";//περνουμε ολα τα δεδομενα απο τον πινακα content
				     PreparedStatement admin=conn.prepareStatement(data);
				     ResultSet rs=admin.executeQuery();
				 	 out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#2b6777;font-size:25px\"><b> THE VIDEO CLIPS OF SITE </b></h1>");
				 	 out.println("<hr style=\"width:60%;border-top: 3px solid blue;text-align:center;\">");
				     while(rs.next()) 
				     {//με ενα while εμφανιζουμε ολα τα στοιχει του πινακα content
				    	 String data2="select * from eva";
				    	 PreparedStatement admin2=conn.prepareStatement(data2);
				    	 ResultSet rs2=admin2.executeQuery();
				     	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> CREATOR :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("cr_username")+"</span></b></h3>");					  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>TITLE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("title")+"</span></b></h3>");	
					     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> VIDEO CODE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("id")+"</span></b></h3>");	
					     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> EVALUATIONS :</b></h3>");	
					     while(rs2.next()) 
					     {//με ενα ακομα while εμφανιζουμε ολες τις κριτηκες για το συγκεκριμενο βιντεο παρεα με το ονομα του κριτη
					    	 if(rs2.getInt("co_id")==rs.getInt("id")) 
					    	 {				  
						    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs2.getString("juror")+" : "+rs2.getInt("eva")+"⭐"+" </b></h3>");
						    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b> Evaluation Code "+"  : "+rs2.getInt("id")+"</b></h3>");
					    		 out.println("<a style=\"font-family:Rockwell;color:red;font-size:15px\" href=\"/project/delrate2.html\">DELETE EVALUATION</a>");
					    	 }
					     }
					     out.println("<video style=\"display: block; margin: auto;\" width=\"60%\" controls autoplay muted \"><source src="+ rs.getString("url") +" type=\"video/mp4\"></video><br><br>");
					     rs2.close();
						 admin2.close();
					     out.println("<hr style=\"width:100%;border-top: 3px solid blue;\">");
				     }
				     out.println("<br>");
					 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				     admin.close();
					 conn.close();
					 rs.close();
					
				}catch(SQLException e) {System.out.println(e.toString());}
			}
			else if(requestType.equalsIgnoreCase("viewuser"))//συναρτιση που βλεπει τους χρηστες του site
			{
				try 
				{
					 Connection conn=datasource.getConnection();
					 String data="select * from creator";//πρωτα διαλεγουμε τους Creators
				     PreparedStatement admin=conn.prepareStatement(data);
				     ResultSet rs=admin.executeQuery();
				 	 out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#2b6777;font-size:25px\"><b> SITE USERS</b></h1>");
				 	 out.println("<hr style=\"width:60%;border-top: 3px solid blue;text-align:center;\">");
				 	 out.println("<h2 style=\"text-align:left;font-family:Rockwell;color:green;font-size:25px\"><b>CONTENT CREATORS :</b></h1>");
				 	 while(rs.next()) 
				 	 {//εμφανιζει Creators παρεα με ενα κουμπι delete για να μπορουμε να τον διαγραψουμε
				     	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> USERNAME :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("cr_username")+"</span></b></h3>");					  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>CONTENT TYPE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("content")+"</span></b></h3>");
				    	 out.println("<a style=\"color:blue;font-size:15px;font-family:Rockwell\"href=\"/project/delcre.html\"><strong>DELETE USER</strong></a>");
				 	 }
				 	 rs.close();
				 	 admin.close();
				 	 String data2="select * from asses";//διαλεγουμε τους κριτες
				     PreparedStatement admin2=conn.prepareStatement(data2);
				     ResultSet rs2=admin2.executeQuery();
				     out.println("<hr style=\"width:60%;border-top: 3px solid blue;\">");
				     out.println("<h2 style=\"text-align:left;font-family:Rockwell;color:green;font-size:25px\"><b>CONTENT ASSESSMENT :</b></h1>");
				     while(rs2.next()) 
				     {//εμφανιζει krites παρεα με ενα κουμπι delete για να μπορουμε να τον διαγραψουμε
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> USERNAME :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs2.getString("username")+"</span></b></h3>");					  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>SPECIALTY :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs2.getString("studies")+"</span></b></h3>");
				    	 out.println("<a style=\"color:blue;font-size:15px;font-family:Rockwell\"href=\"/project/delca.html\"><strong>DELETE USER</strong></a><br><br>");
				     }
				     rs2.close();
				     admin2.close();
				     out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
				}
				catch(Exception e) {out.println(e.toString());}
				 
			}
			else if(requestType.equalsIgnoreCase("delrate2")) 
			{
				int code=Integer.parseInt(request.getParameter("code"));//ΠΕΡΝΕΙ ΤΟΝ ΚΩΔΙΚΟ ΤΗΣ ΑΞΙΟΛΟΓΙΣΗς ΠΟΥ ΕΔΩΣΕ Ο ΧΡΗΣΤΗΣ
				try 
				{
					HttpSession session=request.getSession();
					Connection co=datasource.getConnection();
					Statement stmt =co.createStatement();//ΕΛΕΓΧΟΥΜΕ ΕΑΝ Ο ΚΩΔΙΚΟς ΤΗΣ ΑΞΙΟΛΟΓΙΣΗΣ ΑΝΤΟΙΣΤΕΙΧΕΙ ΣΕ ΑΥΤΟΝ ΤΟΝ ΚΡΙΤΗ
					String d="select * from eva where id='"+code+"'";
					PreparedStatement doc=co.prepareStatement(d);
					ResultSet rs2=doc.executeQuery();
					if(rs2.next())
					{//ΕΑΝ ΥΠΑΡΧΕΙ ΤΟ ΟΝΟΜΑ ΤΟΥ ΚΡΙΤΗ ΜΕ ΑΥΤΟΝ ΤΟΝ ΚΩΔΙΚΟ ΤΟΤΕ ΚΑΝΕΙ ΔΙΑΓΡΑΦΕΙ
						Connection co2=datasource.getConnection();
						stmt=co2.createStatement();
						String sql=admin1.delev(code);
						stmt.executeUpdate(sql);
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE EVALUATION WAS DELETED</h1>");
						out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
						co2.close();
					}
					else 
					{//ΑΛΛΙΩς ΕΜΦΑΝΙΖΕΙ ΜΝΜ ΛΑΘΟΥΣ
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">WRONG CODE</h1>");
						out.println("<a href=\"/project/delrate2.html\"><strong>TRY AGAIN</strong></a><br>");
						out.println("<a href=\"/project/adminsetting.html\"><strong>EXIT</strong></a>");
					}
					stmt.close();
					co.close();
					rs2.close();

				}catch(Exception e) {out.println(e.toString());
			}
				
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");	
		PrintWriter out= response.getWriter();
		out.println("<html>");
		out.println("<head><title>ADMIN</title></head>");
		out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
		HttpSession session=request.getSession();
		try
		{//περνουμε τα στοιχεια που εδωσε ο χρηστης για να κανει Login ως admin
		String username = request.getParameter("username");//ονομα		
	    String password = request.getParameter("password");//κωδικος
	    Connection conn=datasource.getConnection();
	    String data=admin1.login(username,password);//καλουμε την συναρτιση Login toy admin
	    PreparedStatement admin=conn.prepareStatement(data);
	    ResultSet rs=admin.executeQuery();
	     if(rs.next())
	     {//εαν το ονομα και ο κωδικος υπαρχουν τοτε κανουμε Login με επιτυχεια
	    	session.setAttribute("user", username);
	    	out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">LOGIN SUCCESSFULY</h1>");
	    	out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98\">WELCOME ADMIN"+": "+session.getAttribute("user")+"</h1>");
	    	out.println("<a href=\"/project/adminsetting.html\"><strong>CONTINUE</strong></a>");//κουμπι για να μπουμε στο μενου του admin
	    	out.println("<br>");
	    	out.println("<a href=\"/project/index.html\"><strong>EXIT</strong></a>");//Κουμπι για να παμε παλι στην αρχικη σελιδα
	    	
	     }
	     else
	     {//αλλιως εμφανιζει μνμ λαθους
	       out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">WRONG PASSWORD or USERNAME</h1>");
		   out.println("<a href=\"/project/logadmin.html\"><strong>TRY AGAIN\r</strong></a>");//koympi gia thn selida Λογιν
		   out.println("<br>");
	     }
	     admin.close();
		 conn.close();
		 rs.close();
	    }
		catch(SQLException e) {System.out.println(e.toString());}
	}

}
