import java.sql.*;
import java.lang.*;
/**
 *
 * @author yutang
 */

public class Test {
    
    public static Connection LoadDriver() throws SQLException 
    {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db6"; 
        String dbUsername = "Group6";
        String dbPassword = "a37213721"; 
        Connection con  = null; 
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        } 
        catch(ClassNotFoundException e)
        {
            System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
            System.exit(0); 
        }
        catch(SQLException e)
        {
            System.out.println(e); 
        }
        return con;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		/*String sql = "DROP TABLE IF EXISTS S1;"
		String sql0= "CREATE TABLE S1( sid integer, age integer);";
		String sql1= "INSERT INTO S1 VALUES(1155070292, 21);";

        String psql = "SELECT * FROM S1 WHERE age = ?;";
        String aUserInputAge = "21";
		*/
        try
        {
            Connection conn=  LoadDriver();
			/*Statement statement = conn.createStatement();
			statement.executeUpdate(sql);
			statement.executeUpdate(sql0);
			statement.executeUpdate(sql1);
            PreparedStatement pstmt = conn.prepareStatement(psql);
            pstmt.setInt(1, Integer.parseInt(aUserInputAge));
            ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			String columnValue = resultSet.getString(1);
			System.out.println(columnValue + " ");
			*/
        }
        catch(Exception e){System.out.println(e);}

    }
    
}

