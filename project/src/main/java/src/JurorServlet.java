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
 * Servlet implementation class JurorServlet
 */
@WebServlet("/jurorServlet")
public class JurorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource=null; //ΜΕΤΑΒΛΙΤΗ ΓΙΑ ΝΑ ΚΑΝΕΙ CONECT ΜΕ ΤΗΝ ΒΑΣΗ
	juror j1=new juror("1","1");//ΔΗΜΙΟΥΡΓΟΥΜΕ ΕΝΑΝ ΚΡΙΤΗ ΓΙΑ ΝΑ ΜΠΟΡΟΥΜΕ ΝΑ ΧΡΗΣΙΜΟΠΟΙΟΥΜΕ ΤΙΣ ΣΥΝΑΡΤΙΣΕΙΣ ΤΟΥ;		
	int id=1;//PRIMARY KEY ΓΙΑ ΤΟΝ ΠΙΝΑΚΑ EVA 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JurorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init() throws ServletException //ΚΑΝΕΙ ΤΟ CONNECTION ΜΕ ΤΗΝ ΒΑΣΗ
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
			out.println("<head><title>JUROR</title></head>");
			out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
			String requestType=request.getParameter("requestType");
			if(requestType.equalsIgnoreCase("delrate")) //ΣΥΝΑΡΤΙΣΗ ΠΟΥ ΔΙΑΓΡΑΦΕΙ ΜΙΑ ΑΞΙΟΛΟΓΙΣΗ ΤΟΥ ΚΡΙΤΗ
			{
				int code=Integer.parseInt(request.getParameter("code"));//ΠΕΡΝΕΙ ΤΟΝ ΚΩΔΙΚΟ ΤΗΣ ΑΞΙΟΛΟΓΙΣΗς ΠΟΥ ΕΔΩΣΕ Ο ΧΡΗΣΤΗΣ
				try 
				{
					HttpSession session=request.getSession();
					Connection co=datasource.getConnection();
					Statement stmt =co.createStatement();//ΕΛΕΓΧΟΥΜΕ ΕΑΝ Ο ΚΩΔΙΚΟς ΤΗΣ ΑΞΙΟΛΟΓΙΣΗΣ ΑΝΤΟΙΣΤΕΙΧΕΙ ΣΕ ΑΥΤΟΝ ΤΟΝ ΚΡΙΤΗ
					String d="select * from eva where id='"+code+"' and juror='"+session.getAttribute("user")+"'";
					PreparedStatement doc=co.prepareStatement(d);
					ResultSet rs2=doc.executeQuery();
					if(rs2.next())
					{//ΕΑΝ ΥΠΑΡΧΕΙ ΤΟ ΟΝΟΜΑ ΤΟΥ ΚΡΙΤΗ ΜΕ ΑΥΤΟΝ ΤΟΝ ΚΩΔΙΚΟ ΤΟΤΕ ΚΑΝΕΙ ΔΙΑΓΡΑΦΕΙ
						Connection co2=datasource.getConnection();
						stmt=co2.createStatement();
						String sql=j1.delev(code);
						stmt.executeUpdate(sql);
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">THE EVALUATION WAS DELETED</h1>");
						out.println("<a href=\"/project/jursetting.html\"><strong>EXIT</strong></a>");
						co2.close();
					}
					else 
					{//ΑΛΛΙΩς ΕΜΦΑΝΙΖΕΙ ΜΝΜ ΛΑΘΟΥΣ
						out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:red\">WRONG CODE</h1>");
						out.println("<a href=\"/project/delrate.html\"><strong>TRY AGAIN</strong></a><br>");
						out.println("<a href=\"/project/jursetting.html\"><strong>EXIT</strong></a>");
					}
					stmt.close();
					co.close();
					rs2.close();

				}catch(Exception e) {out.println(e.toString());}	
			} 
			else if(requestType.equalsIgnoreCase("viewcon")) //ΒΛΕΠΕΙ ΤΟ ΠΕΡΙΕΧΟΜΕΝΟ ΤΟΥ SITE
			{
				try 
				{
					 boolean bool=false;//ΜΕΤΑΒΛΗΤΗ Η ΟΠΟΙΑ ΕΛΕΓΧΕΙ ΕΑΝ ΕΧΕΙ ΚΑΝΕΙ ΑΞΙΟΛΟΓΙΣΗ Ο ΚΡΙΤΗΣ ΣΕ ΚΑΠΟΙΟ ΒΙΝΤΕΟ
					 HttpSession session=request.getSession();
					 Connection conn=datasource.getConnection();
					 String data="select * from content";
				     PreparedStatement admin=conn.prepareStatement(data);
				     ResultSet rs=admin.executeQuery();
				     out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#2b6777;font-size:25px\"><b> THE VIDEO CLIPS OF SITE </b></h1>");
				 	 out.println("<hr style=\"width:60%;border-top: 3px solid blue;text-align:center;\">");
				     while(rs.next()) 
				     {//ME ENA ςΗΙΛΕ EMFANIZEI OLA TA ΣΤΟΙΧΕΙΑ ΤΟΥ ΠΙΝΑΚΑ CONTENT
				    	 String data2="select * from eva";
				    	 PreparedStatement admin2=conn.prepareStatement(data2);
				    	 ResultSet rs2=admin2.executeQuery();
				    	 bool=false;
				     	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> CREATOR :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("cr_username")+"</span></b></h3>");					  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>TITLE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("title")+"</span></b></h3>");	
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> VIDEO CODE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("id")+"</span></b></h3>");	
					     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> EVALUATIONS :</b></h3>");	
					     while(rs2.next()) 
					     {//ME ENA WHILE ΕΜΦΑΝΙΖΕΙ ΤΙΣ ΑΞΙΟΛΟΓΙΣΕΙΣ ΓΙΑ ΤΟ ΣΥΓΚΕΚΡΙΜΕΝΟ ΒΙΝΤΕΟ
					    	 if(rs2.getInt("co_id")==rs.getInt("id")) 
					    	 {				  
						    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs2.getString("juror")+" : "+rs2.getInt("eva")+"⭐"+" </b></h3>");	
						    	 if(rs2.getString("juror").toString().equals(session.getAttribute("user"))) 
						    	 {//ΕΑΝ Ο ΚΡΙΤΗΣ ΕΧΕΙ ΑΞΙΟΛΟΓΙΣΕΙ ΑΥΤΟ ΤΟ ΒΙΝΤΕΟ ΤΟΥ EMFANIZEI ΤΟΝ ΚΩΔΙΚΟ ΑΞΙΟΛΟΓΙΣΗS ΑΛΛΑ KAI ΚΟΥΜΠΙ DELETE
						    		 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b> Evaluation Code "+"  : "+rs2.getInt("id")+"</b></h3>");
						    		 out.println("<a style=\"font-family:Rockwell;color:red;font-size:15px\" href=\"/project/delrate.html\">DELETE EVALUATION</a>");
						    		 bool=true;//EΑΝ ΕΧΕΙ ΑΞΙΟΛΟΓΙΣΕΙ ΑΥΤΟ ΤΟ ΒΙΝΤΕΟ ΓΙΝΕΤΕ tRUE
						    	 }
					    	 }
					     }
					     out.println("<video style=\"display: block; margin: auto;\" width=\"60%\" controls autoplay muted \"><source src="+ rs.getString("url") +" type=\"video/mp4\"></video><br><br>");
					     admin2.close();
					     rs2.close();
					     //ΕΑΝ ΔΕΝ ΕΧΕΙ ΑΞΙΟΛΟΓΙΣΕΙ ΤΟ ΒΙΝΤΕΟ ΤΟΥ ΕΜΦΑΝΙΖΕΙ ΚΟΥΜΠΙ ΓΙΑ ΝΑ ΓΙΝΕΙ ΑΞΙΟΛΟΓΙΣΗ
					    if(bool!=true) { out.println("<a style=\"font-family:Rockwell;color:green;font-size:20px\" href=\"/project/rate.html\">EVALUATION</a><br>");}
					     out.println("<hr style=\" border: 2px solid blue;witdh:50%>\"<br><br>");
				     }
				     out.println("<br>");
					 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/jursetting.html\"><strong>EXIT</strong></a>");
				     admin.close();
					 conn.close();
					 rs.close();
					
				}catch(SQLException e) {System.out.println(e.toString());}
			}
			else if(requestType.equalsIgnoreCase("rate"))//ΣΥΝΑΡΤΙΣΗ ΠΟΥ ΑΞΙΟΛΟΓΕΙ ΕΝΑ ΒΙΝΤΕΟ
			{
				String data;//ΜΕΤΑΒΛΗΤΗ ΓΙΑ ΝΑ ΑΠΟΘΗΚΕΥΟΥΜΕ ΤΟ QUERY
				HttpSession session=request.getSession();
				int eva=Integer.parseInt(request.getParameter("rate"));//ΠΕΡΝΕΙ ΤΟΒ ΒΑΘΜΟ ΤΗΣ ΑΞΙΟΛΟΓΙΣΕΙΣ ΠΟΥ ΕΔΩΣΕ Ο ΚΡΙΤΗΣ
				int co_id=Integer.parseInt(request.getParameter("code"));//ΜΕΤΑΒΛΗΤΗ ΠΟΥ ΠΕΡΝΕΙ ΤΟΝ ΚΩΔΙΚΟ ΤΟΥ ΒΙΝΤΕΟ ΠΟΥ ΕΙΝΑΙ ΓΙΑ ΑΞΙΟΛΟΓΙΣΗ
				String name=session.getAttribute("user").toString();//ΠΕΡΝΕΙ ΤΟ ΟΝΟΜΑ ΤΟΥ ΚΡΙΤΗ ΑΠΟ ΤΟ SESSION 
				try 
				{
					 Connection conn=datasource.getConnection();
					 data="select max(id) from eva";
					 PreparedStatement ps2=conn.prepareStatement(data);
				     ResultSet rs2=ps2.executeQuery();
				     if(rs2.next()) {id=id+rs2.getInt("max(id)");}
				     else id=1;//PRIMARY KEY ΓΙΑ ΤΟΝ ΠΙΝΑΚΑ EVA 
				     ps2.close();
				     rs2.close();
					 data="select * from content where id='"+co_id+"'";
					 PreparedStatement ps=conn.prepareStatement(data);
				     ResultSet rs=ps.executeQuery();
				     if(rs.next())
				     {//ΕΑΝ Ο ΚΩΔΙΚΟΣ ΑΝΤΟΙΣΤΕΙΧΕΙ ΣΕ ΚΑΠΟΙΟ ΒΙΝΤΕΟ ΤΟΤΕ ΚΑΝΕΙ RATE
				    	 Statement stmt =conn.createStatement();
				    	 stmt=conn.createStatement();
						 String sql=j1.rate(id, co_id, eva, name);
						 stmt.executeUpdate(sql);
						 stmt.close();	
						 out.println("<h1 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \">THE EVALUATION HAS JUST BEEN COMPLETED</h1>");
						 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/jursetting.html\"><strong>EXIT</strong></a>");
				     }				    
				     else 
				     {//ALLIVW EMFANIZEI MNM LAΘΟΥΣ
				    	 out.println("<h1 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \">WRONG CODE</h1>"); 
				    	 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/jursetting.html\"><strong>EXIT</strong></a>");
				    	 
				     }
				     ps.close();
				     rs.close();
				     ps.close();
				     conn.close();
					 
				}
				catch(Exception e) {out.println(e.toString());}

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
			out.println("<head><title>ASSESSMENT</title></head>");
			out.println("<body style=\"background-color:#f2f2f2; font-family:Rockwell;\">");
			HttpSession session=request.getSession();
			try
			{
			String username = request.getParameter("username");//ΟΝΟΜΑ ΓΙΑ ΤΟ LOGIN
		    String password = request.getParameter("password");//KΩΔΙΚΟΣ ΓΙΑ ΤΟ LOGIN
		    Connection conn=datasource.getConnection();
		    String data=j1.login(username,password);
		    PreparedStatement admin=conn.prepareStatement(data);
		    ResultSet rs=admin.executeQuery();
		     if(rs.next())
		     {//ΕΑΝ ΑΝΤΟΙΣΤΕΙΧΕΙ ΣΕ ΚΑΠΟΙΟΝ ΚΡΙΤΗ ΤΟΤΕ ΚΑΝΕΙ LOGIN
		    	session.setAttribute("user", username);
		    	out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">LOGIN SUCCESSFULY</h1>");
		    	out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98\">WELCOME ASSESSMENT"+": "+session.getAttribute("user")+"</h1>");
		    	out.println("<a href=\"/project/jursetting.html\"><strong>CONTINUE</strong></a>");
		    	out.println("<br>");
		    	out.println("<a href=\"/project/index.html\"><strong>EXIT</strong></a>");
		    	
		     }
		     else
		     {//ΑΛΛΙΩΣ ΕΜΦΑΝΙΖΕΙ ΜΝΜ ΛΑΘΟΥΣ
		       out.println("<h1 style=\"text-align:center;font-family:Rockwell;color:#52ab98\">WRONG PASSWORD or USERNAME</h1>");
			   out.println("<a href=\"/project/loginjur.html\"><strong>TRY AGAIN\r</strong></a>");
			   out.println("<br>");
		     }
		     admin.close();
			 conn.close();
		   	 rs.close();
		    }
			catch(SQLException e) {System.out.println(e.toString());}
	}

}
