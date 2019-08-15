

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Insert
 */
@WebServlet("/Insert")
public class Insert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println(prop.getProperty("database"));
	        System.out.println(prop.getProperty("dbuser"));
	        System.out.println(prop.getProperty("dbpassword"));
			Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			PreparedStatement pst = null;
            
			String sql = "INSERT INTO "+db_table.toString()+" VALUES (?,?,?,?)";
			pst = conn.prepareStatement(sql);
			
            String issue_name;
			String coupon;
			String denomination;
			String call_date_1;
			
				issue_name = request.getParameter("IssueName").toString();
				coupon = request.getParameter("Coupon").toString();
				denomination = request.getParameter("Denomination").toString();
				call_date_1 = request.getParameter("CallDate1");

    		
    		if(issue_name.isEmpty())issue_name = null; pst.setString(1, issue_name);									//1
    		if(coupon.isEmpty()) pst.setString(2, null); else pst.setInt(2, Integer.parseInt(coupon));;					//2
    		if(denomination.isEmpty()) pst.setString(3, null); else pst.setDouble(3, Double.parseDouble(denomination)); //3
    		if(call_date_1.isEmpty())call_date_1 = null; pst.setString(4, call_date_1);									//4
    		
    		
				pst.executeUpdate();
		
    		
    		pst.close();
    		conn.close();
    		
    		request.setAttribute("message", "Data Entered Successfully");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");    		
    		dispatcher.forward(request, response);
    		
            
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not connect to database");
		}

	}
		
}
	
