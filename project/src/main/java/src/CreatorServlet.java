package src;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
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
import javax.swing.JFileChooser;




/**
 * Servlet implementation class CreatorServlet
 */
@WebServlet("/creatorServlet")
public class CreatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource=null;//μεταβλητη για να κανουμε συνδεση με την βαση    
	creator c1=new creator("1","1");//δημιουργουμε εναν νεο creator
	int id=1;//PRIMARY KEY ΓΙΑ ΝΑ ΠΡΟΣΘΕΣΕΙ ΚΟΝΤΕΝΤ
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init() throws ServletException //kanoyme connection Με την βαση
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
			out.println("<head><title>CREATOR</title></head>");
			out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
			String requestType=request.getParameter("requestType");
			HttpSession session=request.getSession();
			if(requestType.equals("addco")) //ΠΡΟΣΘΕΤΕΙ content
			{
				try
				{
					String title = request.getParameter("title");//ΠΕΡΝΕΙ ΤΟΝ ΤΙΤΛΟ ΤΟΥ ΒΙΝΤΕΟ ΠΟΥ ΕΔΩΣΕ Ο ΧΡΗΣΤΗΣ
					Connection conn=datasource.getConnection();
					String x="select max(ID) from content";
					PreparedStatement doc2=conn.prepareStatement(x);
					ResultSet rs2=doc2.executeQuery();
					if(rs2.next())
					{ //ΕΑΝ ΒΡΕΙ MAX ID ΤΟΤΕ ΘΕΤΕΙ ΤΟ ID ΙΣΟ ΜΕ ΤΟ MAX ID +1 ΓΙΑ ΤΟ PRIMARY KEY
						if(rs2.getString("max(ID)")!=null) {id=rs2.getInt("max(ID)");id++;}
					}
					Statement statement = conn.createStatement();
					statement.executeUpdate(c1.addcon(session.getAttribute("user").toString(), request.getParameter("img"), id,title));
				    out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE VIDEO WAS UPLOADED ON THE SITE</h1>");
				    out.println("<a href=\"/project/contsetting.html\"><strong>CONTINUE</strong></a>");
				    out.println("<br>");				    
				    conn.close();
				    rs2.close();

				}
				catch(Exception e) {out.println(e.toString());}
			
			}
			else if(requestType.equals("delco")) //ΣΥΝΑΡΤΙΣΗ ΠΟΥ ΔΙΑΓΡΑΦΕΙ CONTENT
			{
				id=Integer.parseInt(request.getParameter("code"));//ΠΕΡΝΕΙ ΤΟΝ ΚΩΔΙΚΟ ΤΟΥ ΒΙΝΤΕΟ ΠΟΥ ΕΔΩΣΕ Ο ΧΡΗΣΤΗΣ
				try 
				{
					Connection co=datasource.getConnection();
					Statement stmt =co.createStatement();
					Statement stmt2 =co.createStatement();
					String d="select id from content where id='"+id+"' and cr_username='"+session.getAttribute("user").toString()+"'";
					PreparedStatement doc=co.prepareStatement(d);
					ResultSet rs=doc.executeQuery();
					if(rs.next())
					{//ΕΑΝ Ο ΚΩΔΙΚΟς ΥΠΑΡΧΕΙ ΚΑΝΕΙ ΔΙΑΓΡΑΦΕΙ
						Connection co2=datasource.getConnection();
						stmt=co2.createStatement();
						String sql=c1.delcon(id,session.getAttribute("user").toString());
						stmt.executeUpdate(sql);
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE CONTENT  WAS DELETED</h1>");
						out.println("<a href=\"/project/contsetting.html\"><strong>EXIT</strong></a>");
						stmt.close();
						stmt2=co2.createStatement();
						String sql2="delete from eva where co_id='"+id+"'";
						stmt2.executeUpdate(sql2);
						co2.close();
					}
					else 
					{//ΑΛΛΙΩς ΕΜΦΑΝΙΖΕΙ ΜΝΜ ΛΑΘΟΥΣ
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">THIS CODE DOES NOT CORRESPOND TO ANY OF YOUR VIDEOS</h1>");
						out.println("<a href=\"/project/delco.html\"><strong>TRY AGAIN</strong></a><br>");
						out.println("<a href=\"/project/contsetting.html\"><strong>EXIT</strong></a>");
					}
					stmt.close();
					co.close();
					rs.close();

				}catch(Exception e) {out.println(e.toString());}
			}
			else if(requestType.equalsIgnoreCase("viewcon"))//ΣΥΝΑΡΤΙΣΗ ΠΟΥ ΒΛΕΠΕΙ ΤΟ CONTENT
			{
				try 
				{
					 Connection conn=datasource.getConnection();
					 String data="select * from content";
				     PreparedStatement admin=conn.prepareStatement(data);
				     ResultSet rs=admin.executeQuery();
				     out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#2b6777;font-size:25px\"><b> THE VIDEO CLIPS OF SITE </b></h1>");
				 	 out.println("<hr style=\"width:60%;border-top: 3px solid blue;text-align:center;\">");
				     while(rs.next()) 
				     {//WHILE ΓΙΑ ΝΑ ΕΜΦΑΝΙΣΕΙ ΟΛΑ ΤΑ ΣΤΟΙΧΕΙΑ ΤΟΥ ΠΙΝΑΚΑ CONTENT
				    	 String data2="select * from eva";
				    	 PreparedStatement admin2=conn.prepareStatement(data2);
				    	 ResultSet rs2=admin2.executeQuery();
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> CREATOR :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("cr_username")+"</span></b></h3>");					  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>TITLE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("title")+"</span></b></h3>");	
					     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> VIDEO CODE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("id")+"</span></b></h3>");					 
					     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> EVALUATIONS :</b></h3>");	
					     while(rs2.next()) 
					     {//WHILE ΓΙΑ ΝΑ ΕΜΦΑΝΙΣΕΙ ΟΛΕς ΤΙΣ ΚΡΙΤΗΚΕΣ ΠΟΥ ΑΝΤΟΙΣΤΕΙΧΟΥΝ ΣΤΟ ΣΥΓΚΕΚΡΙΜΕΝΟ ΒΙΝΤΕΟ
					    	 if(rs2.getInt("co_id")==rs.getInt("id")) 
					    	 {				  
						    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs2.getString("juror")+" : "+rs2.getInt("eva")+"⭐"+" </b></h3>");	
					    	 }
					     }//ΕΜΦΑΝΙΖΕΙ ΒΙΝΤΕΟ 
					     out.println("<video style=\"display: block; margin: auto;\" width=\"60%\" controls autoplay muted \"><source src="+ rs.getString("url") +" type=\"video/mp4\"></video><br><br>");
						 rs2.close();
						 admin2.close();						
					     out.println("<hr style=\"width:100%;border-top: 3px solid blue;\">");
				     }
				     out.println("<br>");
					 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/contsetting.html\"><strong>EXIT</strong></a>");
				     admin.close();
					 conn.close();
					 rs.close();
				}catch(SQLException e) {System.out.println(e.toString());}
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
			out.println("<head><title>CREATOR</title></head>");
			out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
			HttpSession session=request.getSession();
			try
			{//ΚΑΝEI LOGIN ΩΣ CREATOR
			String username = request.getParameter("username");//ΠΕΡΝΕΙ ΤΟ ΟΝΟΜΑ
		    String password = request.getParameter("password");//ΠΕΡΝΕΙ ΤΟΝ ΚΩΔΙΚΟ
		    Connection conn=datasource.getConnection();
		    String data=c1.login(username,password);//ΕΛΕΓΧΕΙ ΤΑ ΣΤΟΙΧΕΙΑ ΠΟΥ ΕΔΩΣΕ Ο ΧΡΗΣΤΗΣ ΜΕ ΤΗΝΣΥΝΑΡΤΙΣΗ ΤΟΥ CREATOR
		    PreparedStatement admin=conn.prepareStatement(data);
		    ResultSet rs=admin.executeQuery();
		     if(rs.next())
		     {//EAN YPARXEI CREATOR ΜΕ ΑΥΤΑ ΤΑ ΣΤΟΙΧΕΙΑ ΤΟΤΕ ΚΑΝΕ lOGIN
		    	session.setAttribute("user", username);
		    	out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">LOGIN SUCCESSFULY</h1>");
		    	out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98\">WELCOME CREATOR"+": "+session.getAttribute("user")+"</h1>");
		    	out.println("<a href=\"/project/contsetting.html\"><strong>CONTINUE</strong></a>");//ΚΟΥΜΠΙ ΓΙΑ ΝΑ ΜΠΕΙ ΣΤΗΝ ΣΕΛΙΔΑ ΤΟΥ CREATOR
		    	out.println("<br>");
		    	out.println("<a href=\"/project/index.html\"><strong>EXIT</strong></a>");//ΚΟΥΜΠΙ ΓΙΑ ΝΑ ΠΑΣ ΣΤΗΝ ΑΡΧΙΚΗ ΣΕΛΙΔΑ
		    	
		     }
		     else
		     {//AΛΛΙΟΣ ΕΜΦΑΝΙΣΕ ΜΝΜ ΛΑΘΟΥΣ
		       out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">WRONG PASSWORD or USERNAME</h1>");
			   out.println("<a href=\"/project/logcontent.html\"><strong>TRY AGAIN\r</strong></a>");
			   out.println("<br>");
		     }
		     admin.close();
			 conn.close();
			 rs.close();
		    }
			catch(SQLException e) {System.out.println(e.toString());}
	}

}
