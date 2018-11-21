import java.sql.*;
import java.lang.*;
import java.io.*; 
import java.text.SimpleDateFormat;
import java.text.*;
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
    public static void main(String[] args) {
	/*
    String psql = "SELECT * FROM S1 WHERE age = ?;";
    String aUserInputAge = "21";
    */	
        //String sql = "DROP TABLE IF EXISTS S1;";
        // String sql = "set foreign_key_checks = 0;";
        // String sql0= "DROP TABLE IF EXISTS Drivers_Vehicles;";
        //String sql0= "CREATE TABLE IF NOT EXISTS S1( sid integer, age integer);";
        // String sql1= "INSERT INTO S1 VALUES(1155070292, 21);";

        // String DVsql = "CREATE TABLE IF NOT EXISTS Drivers_Vehicles("+
        //                 "did integer,"+
        //                 "vid char(6), "+
        //                 "name char(30) not null,"+
        //                 "model char(30) not null, "+
        //                 "modelyear integer, "+
        //                 "seats integer,"+
        //                 "UNIQUE(vid),"+
        //                 "PRIMARY KEY (did)"+
        //                 ");";
        // String Pssql = "CREATE TABLE IF NOT EXISTS Passenger("+
        //                 "pid integer, "+
        //                 "name char(30) not null,"+
        //                 "PRIMARY KEY(pid)"+
        //                 ");";
        // String MRsql = "CREATE TABLE IF NOT EXISTS Makes_Request("+
        //                 "rid integer, "+
        //                 "pid integer, "+
        //                 "modelyear integer, "+
        //                 "model char(30), "+
        //                 "passengers integer, "+
        //                 "taken boolean,"+
        //                 "PRIMARY KEY(rid),"+
        //                 "FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE"+
        //                 ");";
        // String TTsql = "CREATE TABLE IF NOT EXISTS Takes_Trip("+
        //                 "tid integer, "+
        //                 "did integer,"+
        //                 "rid integer,"+
        //                 "`start` datetime, "+
        //                 "`end` datetime, "+
        //                 "fee integer, "+
        //                 "rating integer default 0,"+
        //                 "PRIMARY KEY(tid, rid),"+
        //                 "FOREIGN KEY(did) REFERENCES Drivers_Vehicles(did) ON UPDATE CASCADE ON DELETE CASCADE,"+
        //                 "FOREIGN KEY(rid) REFERENCES Makes_Request(rid) ON UPDATE CASCADE ON DELETE CASCADE "+
        //                 ");";
        // String Rtsql = "CREATE TABLE IF NOT EXISTS Rates("+
        //                 "tid integer,"+
        //                 "rid integer,"+
        //                 "pid integer,"+
        //                 "FOREIGN KEY(rid) REFERENCES Makes_Request(rid) ON UPDATE CASCADE ON DELETE CASCADE,"+
        //                 "FOREIGN KEY(tid) REFERENCES Takes_Trip(tid) ON UPDATE CASCADE ON DELETE CASCADE,"+
        //                 "FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE,"+
        //                 "PRIMARY KEY(tid, rid)"+
        //                 ");";             
        // try
        // {
        //     Connection conn=  LoadDriver();

        //     Statement statement = conn.createStatement();
        // statement.executeUpdate(sql0);
        // statement.executeUpdate(sql1);
        // statement.executeUpdate(sql);
        //          statement.executeUpdate(sql0);
        //          PreparedStatement pstmt = conn.prepareStatement(psql);
        //          pstmt.setInt(1, Integer.parseInt(aUserInputAge));
        //          ResultSet rs = pstmt.executeQuery();
        // rs.next();
        // String columnValue = rs.getString(1);
        // System.out.println(columnValue + " ");

        //          Statement statement = conn.createStatement();
        //          statement.executeUpdate(DVsql);
        //          statement.executeUpdate(Pssql);
        //          statement.executeUpdate(MRsql);
        //          statement.executeUpdate(TTsql);
        //          statement.executeUpdate(Rtsql);

        // }
        // catch(Exception e){System.out.println(e);}

        // SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
        // String input = "2012-12-10 12:02:10";

        // try { 
        //   Date t = new Date(ft.parse(input).getTime()); 
        //   long l = t.getTime();
        //   System.out.println(l); 
        // } catch (Exception e) { 
        //   System.out.println("Unparseable using " + ft); 
        // }

        String checkDV = "SELECT COUNT(*) FROM Drivers_Vehicles;";
       
        try{
            System.out.print("Processing...");
            Connection conn=  LoadDriver();
            Statement stmt = conn.createStatement();
            ResultSet rsDV = stmt.executeQuery(checkDV);
            rsDV.next();
            System.out.println("Drivers_Vehicles: " + rsDV.getString(1));
        }catch(Exception e){System.out.println(e);}
        
    }
}

