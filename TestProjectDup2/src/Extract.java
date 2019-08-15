

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Extract
 */
@WebServlet("/Extract")
public class Extract extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Properties prop = new Properties();
	    InputStream input = null;
		String db_connect_string = null;
		String db_userid = null;
		String db_password = null;
		String db_table = null; 
		try {

	        input = this.getClass().getClassLoader().getResourceAsStream("config.properties");	
	        // load a properties file
	        prop.load(input);

	        // get the property value and print it out
	        db_connect_string = prop.getProperty("database");
			db_userid = prop.getProperty("dbuser");
			db_password = prop.getProperty("dbpassword");
	        db_table = prop.getProperty("dbtable");
		
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (input != null) {
	            try {
	                input.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

		try {
			response.setContentType("text/html");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			PreparedStatement ps = conn.prepareStatement("select * from "+ db_table.toString()); 
			ResultSet rs = ps.executeQuery();
			PrintWriter out = response.getWriter();
			out.println("<html><style>table {\r\n" + 
					"  width:100%;\r\n" + 
					"}\r\n" + 
					"table, th, td {\r\n" + 
					"  border: 1px solid black;\r\n" + 
					"  border-collapse: collapse;\r\n" + 
					"}\r\n" + 
					"th, td {\r\n" + 
					"  padding: 15px;\r\n" + 
					"  text-align: left;\r\n" + 
					"}\r\n" + 
					"table#t01 tr:nth-child(even) {\r\n" + 
					"  background-color: #eee;\r\n" + 
					"}\r\n" + 
					"table#t01 tr:nth-child(odd) {\r\n" + 
					" background-color: #fff;\r\n" + 
					"}\r\n" + 
					"table#t01 th {\r\n" + 
					"  background-color: black;\r\n" + 
					"  color: white;\r\n" + 
					"}div {\r\n" + 
					"	border-radius: 5px;\r\n" + 
					"	background-color: #b6f7ee;\r\n" + 
					"	padding: 10px;\r\n" + 
					"}\r\n" + 
					"</style><body><center><h3>Date in Table Form:</h3><div><table id = 't01'><tr><th><b>Issue Name</b></th><th><b>Coupon</b></th><th><b>Denomination</b></th><th><b>Call Date 1</b></th></tr>");
            while(rs.next()) {
            	
            	out.println("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td></tr>");
            }
            
            out.println("</table></div></center></body></html>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
