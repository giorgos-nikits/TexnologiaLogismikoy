package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  private DataSource datasource=null; //μεταβλιτη ΓΙΑ ΝΑ ΚΑΝΟΥΜΕ CONECT ME THN ΒΑΣΗ
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init() throws ServletException 
    {//ΓΙΝΕΤΕ ΤΟ CONNECTION ΜΕ ΤΗΝ ΒΑΣΗ
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
		
     if(requestType.equalsIgnoreCase("viewcon"))//ΣΥΝΑΡΤΙΣΗ ΠΟΥ ΒΛΕΠΕΙ ΤΟ ΠΕΡΙΕΧΟΜΕΝΟ
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
		     {//ΕΜΦΑΝΙΖΕΙ ΟΛΑ ΤΑ ΒΙΝΤΕΟ		
		    	 String data2="select * from eva";
		    	 PreparedStatement admin2=conn.prepareStatement(data2);
		    	 ResultSet rs2=admin2.executeQuery();
		    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> CREATOR :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("cr_username")+"</span></b></h3>");					  
		    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b>TITLE :<span style=\"color:#52ab98;font-weight:bold;font-family:Rockwell;\">"+rs.getString("title")+"</span></b></h3>");					   
			     out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:red \"><b> EVALUATIONS :</b></h3>");	
			     while(rs2.next()) 
			     {//ΓΙΑ ΚΑΘΕ ΒΙΝΤΕΟ ΕΜΦΑΝΙΖΕΙ ΚΑΙ ΤΙΣ ΑΞΙΟΛΟΓΙΣΕΙΣ ΤΟΥ 
			    	 if(rs2.getInt("co_id")==rs.getInt("id")) 
			    	 {				  
				    	 out.println("<h3 style=\"text-align:left;font-family:Rockwell;color:#52ab98 \"><b>"+rs2.getString("juror")+" : "+rs2.getInt("eva")+"⭐"+" </b></h3>");	
			    	 }
			     }
			     out.println("<video style=\"display: block; margin: auto;\" width=\"60%\" controls autoplay muted \"><source src="+ rs.getString("url") +" type=\"video/mp4\"></video><br><br>");
			     rs2.close();
				 admin2.close();
			     out.println("<hr style=\"width:100%;border-top: 3px solid blue;\">");
		     }
		     out.println("<br>");
			 out.println("<a style=\"color:red;font-size:30px;font-family:Rockwell\"href=\"/project/index.html\"><strong>EXIT</strong></a>");
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
			out.println("<head><title>USER</title></head>");
			out.println("<body style=\"background-color:#f2f2f2;\">");
			//ΔΕΝ ΧΡΕΙΑΖΕΤΕ ΝΑ ΚΑΝΟΥΜΕ LOGIN ΚΑΘΩ ΕΙΝΑΙ ΜΟΝΟ ΕΠΙΣΚΕΠΤΗΣ ΚΑΙ ΔΕΝ ΕΧΕΙ ΔΙΚΑΙΩΜΑΤΑ
	}

}
