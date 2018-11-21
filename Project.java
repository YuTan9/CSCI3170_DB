import java.sql.*;
import java.lang.*;
import java.util.Scanner;
import java.io.*; 
import java.text.SimpleDateFormat;
/**
 * @author yutang
 */

public class Project 
{
    public static Connection LoadDriver() throws SQLException{
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
    public static void Pause(int miliseconds){
        try
        {
            Thread.sleep(miliseconds);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static int Welcome(boolean err){
        String WelcomeMsg = "Welcome! Who are you?\n"+
                        "1. An administrator\n"+
                        "2. A passenger\n"+
                        "3. A driver\n"+
                        "4. None of the above\n"+
                        "Please enter [1-4].\n";
        
        if(!err)
            System.out.print(WelcomeMsg);
        else
            System.out.print("Please enter [1-4].\n");
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();
        switch(mode){
            case 1://Admin mode
                Admin(false);
                break;
            case 2://Passenger mode
                Passenger();
                break;
            case 3://Driver mode
                Driver();
                break;
            case 4://None of above
                Welcome(false);
            default:
                System.out.println("[ERROR] Invalid input.");
                mode = Welcome(true);
                break;
        }
        return mode;
    }
    public static void Admin(boolean err)
    {
        String WelcomeAdmin = "Administrator, what would you like to do?\n"+
                        " 1. Create tables\n"+
                        " 2. Delete tables\n"+
                        " 3. Load data\n"+
                        " 4. Check data\n"+
                        " 5. Go back\n";
        String EnterAdmin = "Please enter [1-5].\n";
        if(!err)
            System.out.print(WelcomeAdmin+EnterAdmin);
        else
            System.out.print(EnterAdmin);
        Scanner adminReader = new Scanner(System.in);
        int adminMode = adminReader.nextInt();
        switch(adminMode)
        {
            case 1://create table
                String DVsql = "CREATE TABLE IF NOT EXISTS Drivers_Vehicles("+
                        "did integer,"+
                        "vid char(6), "+
                        "name char(30) not null,"+
                        "model char(30) not null, "+
                        "modelyear integer, "+
                        "seats integer,"+
                        "UNIQUE(vid),"+
                        "PRIMARY KEY (did)"+
                        ");";
                String Pssql = "CREATE TABLE IF NOT EXISTS Passenger("+
                                "pid integer, "+
                                "name char(30) not null,"+
                                "PRIMARY KEY(pid)"+
                                ");";
                String MRsql = "CREATE TABLE IF NOT EXISTS Makes_Request("+
                                "rid integer, "+
                                "pid integer, "+
                                "modelyear integer, "+
                                "model char(30), "+
                                "passengers integer, "+
                                "taken boolean,"+
                                "PRIMARY KEY(rid),"+
                                "FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE"+
                                ");";
                String TTsql = "CREATE TABLE IF NOT EXISTS Takes_Trip("+
                                "tid integer, "+
                                "did integer,"+
                                "pid integer,"+
                                "`start` datetime, "+
                                "`end` datetime, "+
                                "fee integer, "+
                                "rating integer default 0,"+
                                "PRIMARY KEY(tid, pid),"+
                                "FOREIGN KEY(did) REFERENCES Drivers_Vehicles(did) ON UPDATE CASCADE ON DELETE CASCADE"+
                                ");";
                String Rtsql = "CREATE TABLE IF NOT EXISTS Rates("+
                                "tid integer,"+
                                "rid integer,"+
                                "pid integer,"+
                                "FOREIGN KEY(rid) REFERENCES Makes_Request(rid) ON UPDATE CASCADE ON DELETE CASCADE,"+
                                "FOREIGN KEY(tid) REFERENCES Takes_Trip(tid) ON UPDATE CASCADE ON DELETE CASCADE,"+
                                "FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE,"+
                                "PRIMARY KEY(tid, rid)"+
                                ");";             
                try{
                    System.out.print("Processing...");
                    Connection conn=  LoadDriver();
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(DVsql);
                    statement.executeUpdate(Pssql);
                    statement.executeUpdate(MRsql);
                    statement.executeUpdate(TTsql);
                    statement.executeUpdate(Rtsql);
                    System.out.print("Done! Tables are created!\n");
                }
                catch(Exception e){System.out.println(e);}
                Pause(1000);
                break;
            case 2://delete table
                String DisableFK = "SET FOREIGN_KEY_CHECKS = 0;";
                String DropDV = "DROP TABLE IF EXISTS Drivers_Vehicles;";
                String DropPs = "DROP TABLE IF EXISTS Passenger;";
                String DropMR = "DROP TABLE IF EXISTS Makes_Request;";
                String DropTT = "DROP TABLE IF EXISTS Takes_Trip;";
                String DropRt = "DROP TABLE IF EXISTS Rates;";
                String EnableFK = "SET FOREIGN_KEY_CHECKS = 1;";
                try{
                    System.out.print("Processing...");
                    Connection conn=  LoadDriver();
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(DisableFK);
                    statement.executeUpdate(DropDV);
                    statement.executeUpdate(DropPs);
                    statement.executeUpdate(DropMR);
                    statement.executeUpdate(DropTT);
                    statement.executeUpdate(DropRt);
                    statement.executeUpdate(EnableFK);
                    System.out.print("Done! Tables are deleted!\n");
                }
                catch(Exception e){System.out.println(e);}
                Pause(1000);
                break;
            case 3://Load data
                String[][] driverInfo   = new String[10000][3]; //extra cells will be null
                String[][] passengerInfo= new String[10000][2];
                String[][] vehicleInfo  = new String[10000][4];
                String[][] tripInfo     = new String[10000][7];
                System.out.println("Please enter the folder path.");
                Scanner scan = new Scanner(System.in);
                String path = scan.nextLine();
                System.out.print("Processing...");
                //read drivers.csv
                    try{
                        File file = new File(path + "/drivers.csv"); 
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st; 
                        int count = 0;
                        while ((st = br.readLine()) != null) 
                        {
                            driverInfo[count] = st.split(",");
                            count++;
                        }
                        br.close();
                    }catch(Exception e){System.out.println(e);}
                //read passengers.csv
                    try{
                        File file = new File(path + "/passengers.csv"); 
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st; 
                        int count = 0;
                        while ((st = br.readLine()) != null) 
                        {
                            passengerInfo[count] = st.split(",");
                            count++;
                        }
                        br.close();
                    }catch(Exception e){System.out.println(e);}
                //read vehicles.csv
                    try{
                        File file = new File(path + "/vehicles.csv"); 
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st; 
                        int count = 0;
                        while ((st = br.readLine()) != null) 
                        {
                            vehicleInfo[count] = st.split(",");
                            count++;
                        }
                        br.close();
                    }catch(Exception e){System.out.println(e);}
                //read trips.csv
                    try{
                        File file = new File(path + "/trips.csv"); 
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st; 
                        int count = 0;
                        while ((st = br.readLine()) != null) 
                        {
                            tripInfo[count] = st.split(",");
                            count++;
                        }
                        br.close();
                    }catch(Exception e){System.out.println(e);}
                //Insert to DB
                    String DVinsert = "INSERT INTO Drivers_Vehicles Values(?, ?, ?, ?, ?, ?);";
                    String Psinsert = "INSERT INTO Passenger Values(?, ?);";
                    String TTinsert = "INSERT INTO Takes_Trip Values(?, ?, ?, ?, ?, ?, ?);";
                    String Rtinsert = "INSERT INTO Drivers_Vehicles Values(?, ?, ?);";
                    try{
                        Connection conn=  LoadDriver();
                        PreparedStatement DVpstmt = conn.prepareStatement(DVinsert);
                        PreparedStatement Pspstmt = conn.prepareStatement(Psinsert);
                        PreparedStatement TTpstmt = conn.prepareStatement(TTinsert);
                        PreparedStatement Rtpstmt = conn.prepareStatement(Rtinsert);
                        //Insert into 
                        for(int i = 0 ; driverInfo[i][0] != null ; i++){
                            DVpstmt.setInt(1, Integer.parseInt(driverInfo[i][0]));
                            if(driverInfo[i][2].equals(vehicleInfo[i][0]))
                                DVpstmt.setString(2, vehicleInfo[i][0]);
                            else{
                                System.out.println("VID on drivers.csv and vehicles.csv doesn't match on the same line");
                                continue;
                            }
                            DVpstmt.setString(3, driverInfo[i][1]);
                            DVpstmt.setString(4, vehicleInfo[i][1]);
                            DVpstmt.setInt(5, Integer.parseInt(vehicleInfo[i][2]));
                            DVpstmt.setInt(6, Integer.parseInt(vehicleInfo[i][3]));
                            DVpstmt.executeUpdate();
                        }
                        for(int i = 0 ; passengerInfo[i][0] != null ; i++){
                            Pspstmt.setInt(1, Integer.parseInt(passengerInfo[i][0]));
                            Pspstmt.setString(2, passengerInfo[i][1]);
                            Pspstmt.executeUpdate();
                        }
                        for(int i = 0 ; tripInfo[i][0] != null ; i++){
                            TTpstmt.setInt(1, Integer.parseInt(tripInfo[i][0]));
                            TTpstmt.setInt(2, Integer.parseInt(tripInfo[i][1]));
                            TTpstmt.setInt(3, Integer.parseInt(tripInfo[i][2]));
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
                            String start = tripInfo[i][3];
                            String end   = tripInfo[i][4];
                            long startSecond, endSecond;
                            Date startTime = new Date(ft.parse(start).getTime()); 
                            Date endTime  = new Date(ft.parse(end).getTime());
                            startSecond  = startTime.getTime();
                            endSecond    = endTime.getTime();
                            TTpstmt.setTimestamp(4, new Timestamp(startSecond));
                            TTpstmt.setTimestamp(5, new Timestamp(endSecond));
                            TTpstmt.setInt(6, Integer.parseInt(tripInfo[i][5]));
                            TTpstmt.setInt(7, Integer.parseInt(tripInfo[i][6]));
                            TTpstmt.executeUpdate();
                        }
                        System.out.print("Done! Data is loaded!\n");
                    }
                    catch(Exception e){System.out.println(e);}
                    Pause(1000);
                    break;
            case 4://check data
                String checkDV = "SELECT COUNT(*) FROM Drivers_Vehicles;";
                String checkPs = "SELECT COUNT(*) FROM Passenger;";
                String checkMR = "SELECT COUNT(*) FROM Makes_Request;";
                String checkTT = "SELECT COUNT(*) FROM Takes_Trip;";
                String checkRt = "SELECT COUNT(*) FROM Rates;";    
                try{
                    System.out.print("Processing...\n");
                    Connection conn=  LoadDriver();
                    Statement stmt = conn.createStatement();
                //Driver_Vehicles
                    ResultSet rsDV = stmt.executeQuery(checkDV);
                    rsDV.next();
                    System.out.println("Drivers_Vehicles: " + rsDV.getString(1));
                //Passenger
                    ResultSet rsPs = stmt.executeQuery(checkPs);
                    rsPs.next();
                    System.out.println("Passenger: " + rsPs.getString(1));
                //Makes_Request    
                    ResultSet rsMR = stmt.executeQuery(checkMR);
                    rsMR.next();
                    System.out.println("Makes_Request: " + rsMR.getString(1));
                //Takes_Trip
                    ResultSet rsTT = stmt.executeQuery(checkTT);
                    rsTT.next();
                    System.out.println("Takes_Trip: " + rsTT.getString(1));
                //Rates
                    ResultSet rsRt = stmt.executeQuery(checkRt);
                    rsRt.next();
                    System.out.println("Rates: " + rsRt.getString(1));
                }
                catch(Exception e){System.out.println(e);}
                Pause(1000);
                break;
            case 5://go back
                Welcome(false);
                break;
            default:
                System.out.println("[ERROR] Invalid input.\n");
                Admin(true);
                break;
        }
        Admin(false);       
    }

    public static void Passenger(){
        String WelcomePassenger = "Passenger, what would you like to do?\n"+
                        "1. Request a ride\n"+
                        "2. Check trip records\n"+
                        "3. Rate a trip\n"+
                        "4. Go back\n"+
                        "Please enter [1-4].\n";
        System.out.print(WelcomePassenger);     
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();
    }

    public static void Driver(){
        String WelcomeDriver = "Driver, what would you like to do?\n"+
                        "1. Take a request\n"+
                        "2. Finish a trip\n"+
                        "3. Check driver rating\n"+
                        "4. Go back\n"+
                        "Please enter [1-4].\n";
        System.out.print(WelcomeDriver);  
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();       
    }
    
    public static void main(String[] args){
        int mode = Welcome(false);

    }
    
}

