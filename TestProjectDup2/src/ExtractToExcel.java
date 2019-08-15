
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet implementation class ExtractToExcel
 */
@WebServlet("/ExtractToExcel")
public class ExtractToExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Object[]> dataList = getTableData();
		if (dataList != null && dataList.size() > 0) {
			doExport(dataList);
			request.setAttribute("message", "Data Exported Successfully");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");    		
			dispatcher.forward(request, response);
		} else {
			System.out.println("There is no data available in the table to export");
			request.setAttribute("message", "There is no data available in the table to export");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");    		
			dispatcher.forward(request, response);
		}
		

	}

	public ArrayList<Object[]> getTableData() {
		ArrayList<Object[]> tableDataList = null;
		String db_table = null;
		Properties pp = new Properties();
		InputStream input_1 = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
		Connection con = getConnection(input);
		try {
			
	        pp.load(input_1);

	        // get the property value and print it out
	        db_table = pp.getProperty("dbtable");
			
	        
		
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
		if (con != null) {
			try {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM " + db_table.toString());
				ResultSet result = ps.executeQuery();
				tableDataList = new ArrayList<Object[]>();
				while (result.next()) {
					Object[] objArray = new Object[4];
					objArray[0] = (result.getObject(1) == null) ? "NULL" : result.getObject(1);
					objArray[1] = (result.getObject(2) == null) ? "NULL" : result.getObject(2); 
					objArray[2] = (result.getObject(3) == null) ? "NULL" : result.getObject(3);
					objArray[3] = (result.getObject(4) == null) ? "NULL" : result.getObject(4);
					tableDataList.add(objArray);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Unable to create PreparedStatement");
			}
		}
		return tableDataList;
	} // end getTableData()

	private static Connection getConnection(InputStream input) {
		Properties prop = new Properties();
	    //InputStream input = null;
		String db_connect_string = null;
		String db_userid = null;
		String db_password = null;
		Connection con = null;
		
		try {

	        //input = this.getClass().getClassLoader().getResourceAsStream("config.properties");	
	        // load a properties file
	        prop.load(input);

	        // get the property value and print it out
	        db_connect_string = prop.getProperty("database");
			db_userid = prop.getProperty("dbuser");
			db_password = prop.getProperty("dbpassword");
	        
		
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
			con = DriverManager.getConnection(db_connect_string, db_userid, db_password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Driver class not found. Please add MSSQL connector jar in classpath");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Exception occured while getting Database connection");
		}
		return con;
	} // end getConnection

	public void doExport(ArrayList<Object[]> dataList) {
		if (dataList != null && !dataList.isEmpty()) {
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet();
			HSSFRow headingRow = sheet.createRow(0);
			headingRow.createCell((short) 0).setCellValue("Issue Name");
			headingRow.createCell((short) 1).setCellValue("Coupon");
			headingRow.createCell((short) 2).setCellValue("Denomination");
			headingRow.createCell((short) 3).setCellValue("Call Date 1");
			short rowNo = 1;
			for (Object[] objects : dataList) {
				HSSFRow row = sheet.createRow(rowNo);
				row.createCell((short) 0).setCellValue(objects[0].toString());
				row.createCell((short) 1).setCellValue(objects[1].toString());
				row.createCell((short) 2).setCellValue(objects[2].toString());
				row.createCell((short) 3).setCellValue(objects[3].toString());
				rowNo++;
			}
			Properties prop = new Properties();
		    InputStream input = null;
		    String filelocation = null;
		    try {

		        input = this.getClass().getClassLoader().getResourceAsStream("config.properties");	
		        // load a properties file
		        prop.load(input);

	
		        filelocation = prop.getProperty("filelocation");
			
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


			//String file = "C:\\Users\\HP user\\Documents\\Napinder\\Details.xls";
			try {
				FileOutputStream fos = new FileOutputStream(filelocation);
				workBook.write(fos);
				fos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Invalid directory or file not found");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error occurred while writing excel file to directory");
			}
		}
	} // end doExport
}
