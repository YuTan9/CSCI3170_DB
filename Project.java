import java.sql.*;
import java.lang.*;
import java.util.Scanner;
import java.util.Arrays;
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
            System.out.print(">");
            Thread.sleep(miliseconds);
            System.out.print(">");
            Thread.sleep(miliseconds);
            System.out.print(">");
            Thread.sleep(miliseconds);
            System.out.print("\n");
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static void Welcome(boolean err){
        String WelcomeMsg = "Welcome! Who are you?\n"+
                        "1. An administrator\n"+
                        "2. A passenger\n"+
                        "3. A driver\n"+
                        "4. None of the above(exit)\n"+
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
                Passenger(false);
                break;
            case 3://Driver mode
                Driver(false);
                break;
            case 4://None of above
                System.out.println("Do you want to leave the ride sharing system?[y/n]");
                String isEnd = reader.next();
                if(!isEnd.equals("y"))
                    Welcome(false);
                else{
                    System.out.println("+---------+");
                    System.out.println("| Bye Bye |");
                    System.out.println("+---------+");
                    System.exit(0);
                }
                    
                break;
            default:
                System.out.println("[ERROR] Invalid input.");
                Welcome(true);
                break;
        }
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
                                "model char(30) default null, "+
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
                try{
                    System.out.print("Processing...");
                    Connection conn=  LoadDriver();
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(DVsql);
                    statement.executeUpdate(Pssql);
                    statement.executeUpdate(MRsql);
                    statement.executeUpdate(TTsql);
                    // statement.executeUpdate(Rtsql);
                    System.out.print("Done! Tables are created!\n");
                }
                catch(Exception e){System.out.println(e);}
                Pause(300);
                break;
            case 2://delete table
                String DisableFK = "SET FOREIGN_KEY_CHECKS = 0;";
                String DropDV = "DROP TABLE IF EXISTS Drivers_Vehicles;";
                String DropPs = "DROP TABLE IF EXISTS Passenger;";
                String DropMR = "DROP TABLE IF EXISTS Makes_Request;";
                String DropTT = "DROP TABLE IF EXISTS Takes_Trip;";
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
                    // statement.executeUpdate(DropRt);
                    statement.executeUpdate(EnableFK);
                    System.out.print("Done! Tables are deleted!\n");
                }
                catch(Exception e){System.out.println(e);}
                Pause(300);
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
                    Pause(300);
                    break;
            case 4://check data
                String checkDV = "SELECT COUNT(*) FROM Drivers_Vehicles;";
                String checkPs = "SELECT COUNT(*) FROM Passenger;";
                String checkMR = "SELECT COUNT(*) FROM Makes_Request;";
                String checkTT = "SELECT COUNT(*) FROM Takes_Trip;";
                // String checkRt = "SELECT COUNT(*) FROM Rates;";    
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
                }
                catch(Exception e){System.out.println(e);}
                Pause(300);
                break;
            case 5://go back
                Welcome(false);
                break;
            default:
                System.out.println("[ERROR] Invalid input.");
                Admin(true);
                break;
        }
        Admin(false);       
    }

    public static void Passenger(boolean err){
        String WelcomePassenger = "Passenger, what would you like to do?\n"+
                        "1. Request a ride\n"+
                        "2. Check trip records\n"+
                        "3. Rate a trip\n"+
                        "4. Go back\n";
        String EnterPassenger = "Please enter [1-4].\n";
        if(!err)
            System.out.print(WelcomePassenger+EnterPassenger);  
        else
            System.out.print(EnterPassenger);       
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();
        switch(mode){
            case 1://request a ride
                String MRinsert = "INSERT INTO Makes_Request Values(?, ?, ?, ?, ?, ?);";
                String checkMaxRid = "SELECT MAX(rid) FROM Makes_Request;";
                Scanner scan1 = new Scanner(System.in);

                System.out.println("Please enter your ID.");
                int pid1 = Integer.parseInt(scan1.nextLine());

                System.out.println("Please enter the number of passengers.");
                int passengerno = Integer.parseInt(scan1.nextLine());

                System.out.println("Please enter the earlist model year. (Press enter to skip)");
                String input= scan1.nextLine();
                int modelyear = 0;
                if(!input.equals(""))
                    modelyear = Integer.parseInt(input);

                System.out.println("Please enter the model. (Press enter to skip)");
                String model = scan1.nextLine();
                if(model.equals(""))
                    model = "NULL";
                try{
                    Connection conn=  LoadDriver();
                    PreparedStatement MRpstmt = conn.prepareStatement(MRinsert);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(checkMaxRid);
                    rs.next();
                    String maxRid = rs.getString(1);

                    int rid;
                    if(maxRid==null)
                        rid = 1;
                    else
                        rid = Integer.parseInt(maxRid) + 1; 

                    //Insert into 
                    MRpstmt.setInt(1, rid);
                    MRpstmt.setInt(2, pid1);
                    MRpstmt.setInt(3, modelyear);
                    if(model.equals("NULL"))
                        MRpstmt.setNull(4, java.sql.Types.VARCHAR);
                    else
                        MRpstmt.setString(4, model);
                    MRpstmt.setInt(5, passengerno);
                    MRpstmt.setBoolean(6, false);
                    MRpstmt.executeUpdate();

                }catch(SQLException e){
                    if(e.getErrorCode() == 1452){
                        System.out.println("[ERROR] Passenger ID not found.");
                    }else{
                        System.out.println("[SQL Error]: " + e);
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                try{
                    Connection conn=  LoadDriver();
                    Statement stmt = conn.createStatement();
                    String availableDriver = new String();
                    if(!model.equals("NULL"))
                        availableDriver = "SELECT COUNT(*) FROM Drivers_Vehicles"+
                                                " WHERE seats >= " + passengerno +
                                                " AND modelyear <= " + modelyear +
                                                " AND model = " + model + ";";
                    else
                        availableDriver = "SELECT COUNT(*) FROM Drivers_Vehicles"+
                                                " WHERE seats >= " + passengerno +
                                                " AND modelyear >= " + modelyear + ";";

                    ResultSet rs = stmt.executeQuery(availableDriver);
                    rs.next();
                    System.out.println("Your request is placed. " + rs.getInt(1) + " drivers are able to take the request.");
                }catch(Exception e){System.out.println(e);}
                Pause(300);
                break;
            case 2://check trip record
                Scanner scan2 = new Scanner(System.in);

                System.out.println("Please enter your ID.");
                int pid2 = Integer.parseInt(scan2.nextLine());
                
                System.out.println("Please enter the start date.");
                String startDate = scan2.nextLine() + " 00:00:00";

                System.out.println("Please enter the end date.");
                String endDate   = scan2.nextLine() + " 23:59:59";

                String tripQuery = "SELECT * FROM Takes_Trip WHERE pid = " +
                                    pid2 +
                                    " AND start > \""+
                                    startDate+
                                    "\" AND end < \""+
                                    endDate+
                                    "\"";
                String title = "+-----+------+-----+---------------------+---------------------+------+--------+\n"+
                               "| tid | did  | pid | start               | end                 | fee  | rating |\n"+
                               "+-----+------+-----+---------------------+---------------------+------+--------+\n";
                try{
                    Connection conn=  LoadDriver();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(tripQuery);
                    System.out.print(title);
                    while(rs.next()){
                        System.out.format("| %3d |", rs.getInt(1));
                        System.out.format(" %4d |", rs.getInt(2));
                        System.out.format(" %3d |", rs.getInt(3));
                        System.out.print(" " + rs.getString(4).substring(0, 19) + " |");
                        System.out.print(" " + rs.getString(5).substring(0, 19) + " |");
                        System.out.format(" %4d |", rs.getInt(6));
                        System.out.format(" %6d |\n", rs.getInt(7));
                    }
                    System.out.println("+-----+------+-----+---------------------+---------------------+------+--------+");
                }catch(Exception e){System.out.println(e);} 
                Pause(300);                   
                break;
            case 3://rate a trip
                Scanner scan3 = new Scanner(System.in);

                System.out.println("Please enter your ID.");
                int pid3 = Integer.parseInt(scan3.nextLine());

                System.out.println("Please enter the trip ID.");
                int tid = Integer.parseInt(scan3.nextLine());

                System.out.println("Please enter the rating.");
                int rate = Integer.parseInt(scan3.nextLine());

                String updateRate = "UPDATE Takes_Trip SET rating = "+
                                    rate + 
                                    " WHERE tid = " +
                                    tid + 
                                    " AND pid = "+
                                    pid3 + 
                                    ";";

                try{
                    Connection conn=  LoadDriver();
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(updateRate);
                    ResultSet rsTT = stmt.executeQuery("SELECT * from Takes_Trip WHERE tid ="+tid);           
                    rsTT.next();
                    int did = rsTT.getInt(2);
                    String start = rsTT.getString(4).substring(0,19);
                    String end = rsTT.getString(5).substring(0,19);
                    int fee = rsTT.getInt(6);
                    int rating = rsTT.getInt(7);
                    ResultSet rsDV = stmt.executeQuery("SELECT * from Drivers_Vehicles WHERE did = "+did);
                    rsDV.next();
                    String name = rsDV.getString(3);
                    String vid = rsDV.getString(2);
                    String vmodel = rsDV.getString(4);

                    //Trip ID, Driver name, Vehicle ID, Vehicle model, Start, End, Fee, Rating
                    System.out.println("Trip ID: "+ tid);
                    System.out.println("Driver name: "+ name);
                    System.out.println("Vehicle ID: "+ vid);
                    System.out.println("Vehicle model: "+ vmodel);
                    System.out.println("Start: "+start);
                    System.out.println("End: "+end); 
                    System.out.println("Fee: "+fee);
                    System.out.println("Rating: "+rating);
                }catch(Exception e){System.out.println(e);} 
                Pause(300);
                break;
            case 4://Go back
                Welcome(false);
                break;
            default:
                System.out.println("[ERROR] Invalid input.");
                Passenger(true);
                break;
        }
        Passenger(false);
    }

    public static void Driver(boolean err){
        String WelcomeDriver = "Driver, what would you like to do?\n"+
                        "1. Take a request\n"+
                        "2. Finish a trip\n"+
                        "3. Check driver rating\n"+
                        "4. Go back\n"+
                        "Please enter [1-4].\n";
        System.out.print(WelcomeDriver);  
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();  
        switch (mode) {
                case 1://Take a request
                    System.out.println("Please enter your ID.");
                    int did = reader.nextInt();
                    String driverAvailable = "SELECT tid FROM Takes_Trip WHERE did = " + did + " AND end is null;";
                    String getDriverInfo = "SELECT * FROM Drivers_Vehicles WHERE did = "+did+";";
                    String getRequests = "SELECT * FROM Makes_Request WHERE ? >= passengers AND ? >= modelyear AND taken = 0 AND(model = ? OR model IS NULL);";
                    try{
                        Connection conn = LoadDriver();
                        Statement stmt = conn.createStatement();
                        PreparedStatement pstmt = conn.prepareStatement(getRequests);


                        ResultSet rsGDI = stmt.executeQuery(getDriverInfo);
                        rsGDI.next();
                        String model = rsGDI.getString(4);
                        int modelyear = rsGDI.getInt(5);
                        int seats = rsGDI.getInt(6);
                        int[][] ridPassenger = new int[1000][3]; 

                        ResultSet rsDA = stmt.executeQuery(driverAvailable);
                        boolean busy = rsDA.isBeforeFirst();
                        if(busy){
                            rsDA.next();
                            int unfinishedTrip = rsDA.getInt(1);
                            System.out.println("You have a unfinshed trip. Trip ID: "+unfinishedTrip);
                        }
                        else{//No un-finished trip
                            pstmt.setInt(1, seats);
                            pstmt.setInt(2, modelyear);
                            pstmt.setString(3, model);
                            ResultSet rsGR = pstmt.executeQuery();
                            String title = "+-----+----------------+------------+\n"+
                                            "| rid | Passenger Name | Passengers |\n"+
                                            "+-----+----------------+------------+";
                            if(rsGR.isBeforeFirst()){    
                               int i = 0, length = 0;
                               System.out.println(title);
                               while(rsGR.next()){
                                   ridPassenger[i][0] = rsGR.getInt(1);//rid
                                   ridPassenger[i][1] = rsGR.getInt(5);//passenger
                                   ridPassenger[i][2] = rsGR.getInt(2);//pid
                                   i++;
                                   length++;
                               }
                               i=0;
                               while(i<length){
                                   ResultSet rs = stmt.executeQuery("SELECT name FROM Passenger WHERE pid = " + ridPassenger[i][2] + " ;");
                                   rs.next();
                                   System.out.format("| %3d |", ridPassenger[i][0]);
                                   System.out.format(" %14s |", rs.getString(1));
                                   System.out.format(" %10d |\n", ridPassenger[i][1]);
                                   i++;
                                }
                                System.out.println("+-----+----------------+------------+");
                                System.out.println("Please enter the request ID.");
                                int acceptRequest = reader.nextInt();
                                for(int x = 0 ; x < 1000 ; x++){
                                    if(ridPassenger[x][0]==acceptRequest){
                                        acceptRequest = ridPassenger[x][2];
                                        break;
                                    }
                                }
                                String maxTripSQL = "SELECT MAX(tid) FROM Takes_Trip;";
                                ResultSet rsMT = stmt.executeQuery(maxTripSQL);
                                rsMT.next();
                                int maxTrip = 1;
                                if(!rsMT.getString(1).equals("NULL")){
                                    maxTrip = rsMT.getInt(1)+1;
                                }
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                String time = timestamp.toString().substring(0,19);
                                String startTrip = "INSERT INTO Takes_Trip VALUES(?,?,?,?,?,?,?);";
                                PreparedStatement p = conn.prepareStatement(startTrip);
                                p.setInt(1,maxTrip);
                                p.setInt(2,did);
                                p.setInt(3, acceptRequest);
                                p.setString(4, time);
                                p.setNull(5, java.sql.Types.VARCHAR);
                                p.setInt(6, 0);
                                p.setInt(7, 0);
                                p.executeUpdate();
                                ResultSet rs = stmt.executeQuery("SELECT name FROM Passenger WHERE pid = " + acceptRequest + " ;");
                                rs.next();
                                System.out.println("+---------+----------------+---------------------+");
                                System.out.println("| Trip ID | Passenger Name | Start               |");
                                System.out.println("+---------+----------------+---------------------+");
                                System.out.format("| %7d | %14s | %19s |\n", maxTrip, rs.getString(1), time);
                                System.out.println("+---------+----------------+---------------------+");
                                String updateMR = "UPDATE Makes_Request SET taken = 1 WHERE pid = " + acceptRequest;
                                stmt.executeUpdate(updateMR);
                            }
                            else{//No suitable request.
                                System.out.println("There's currently no suitable request for you.");
                            }   
                        }
                    }catch(Exception e){System.out.println(e);}
                    Pause(300);
                    break;
                case 2://Finish a trip
                    System.out.println("Please enter your ID.");
                    int did2 = reader.nextInt();
                    String driverAvailable2 = "SELECT * FROM Takes_Trip WHERE did = " + did2 + " AND end is null;";
                    try{
                        Connection conn = LoadDriver();
                        Statement stmt = conn.createStatement();

                        ResultSet rsDA = stmt.executeQuery(driverAvailable2);
                        boolean busy = rsDA.isBeforeFirst();
                        if(busy){
                            rsDA.next();
                            int tid = rsDA.getInt(1);
                            int pid = rsDA.getInt(3);
                            String start = rsDA.getString(4).substring(0,19);
                            System.out.println("+---------+--------------+---------------------+");
                            System.out.println("| Trip ID | Passenger ID | Start               |");
                            System.out.println("+---------+--------------+---------------------+");
                            System.out.format("| %7d |", tid);
                            System.out.format(" %12d |", pid);
                            System.out.format(" %19s |\n", start);
                            System.out.println("+---------+--------------+---------------------+");
                            System.out.println("Do you want to finish the trip?[y/n]");
                            String toEnd = reader.next();
                            if(toEnd.equals("y")){
                                Timestamp endstmp = new Timestamp(System.currentTimeMillis());
                                String end = endstmp.toString().substring(0,19);
                                Timestamp startstmp = Timestamp.valueOf(start);
                                int fee = (int)((endstmp.getTime()- startstmp.getTime())/60000);
                                String updateTrip = "UPDATE Takes_Trip SET end = \""+
                                                    end + "\" ,fee = "+
                                                    fee + " WHERE tid = "+
                                                    tid + ";";
                                stmt.executeUpdate(updateTrip);
                                ResultSet rsGP = stmt.executeQuery("SELECT name FROM Passenger WHERE pid = " + pid + ";");
                                rsGP.next();
                                String passengerName = rsGP.getString(1);
                                System.out.println("+---------+----------------+---------------------+---------------------+-----+");
                                System.out.println("| Trip ID | Passenger Name | Start               | End                 | Fee |");
                                System.out.println("+---------+----------------+---------------------+---------------------+-----+");
                                System.out.format("| %7d | %14s | %19s | %19s | %3d |\n", tid, passengerName, start, end, fee);
                                System.out.println("+---------+----------------+---------------------+---------------------+-----+");
                            }else{
                                System.out.println("Finish trip failed.");
                            }
                        }else{
                            System.out.println("All the trips are finished.");
                        }
                    }catch(Exception e){System.out.println(e);}
                    Pause(300);
                    break;
                case 3://Check rate
                    System.out.println("Please enter your ID.");
                    int did3 = reader.nextInt();
                    String checkRate = "SELECT rating FROM Takes_Trip WHERE did = " + did3 + ";";
                    double sumRate = 0, rowNo = 0;
                    try{
                        Connection conn = LoadDriver();
                        Statement stmt = conn.createStatement();                        
                        ResultSet rsDA = stmt.executeQuery(checkRate);

                        while(rsDA.next()){
                            if(rsDA.getInt(1) != 0){
                                sumRate+=rsDA.getInt(1);
                                rowNo++;
                            }
                        }
                        if(rowNo == 0){
                            System.out.println("No rating record for you yet.");
                        }
                        else{
                            double avgRate = sumRate/rowNo;
                            System.out.format("Your driver rating is %.1f.\n", avgRate);
                        }
                    }catch(Exception e){System.out.println(e);}
                    Pause(300);
                    break;
                case 4:
                    Welcome(false);
                    break;
        }
        Driver(false);     
    }

    public static void main(String[] args){
        Welcome(false);
    }
    
}

