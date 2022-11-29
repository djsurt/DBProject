import java.util.Scanner;
import java.sql.*;  
import java.util.ArrayList;


public class DatabaseMenu {
    public static void main (String args[]) {

        /*
         *  Establish Connection to Database
         */
        
        String connectionUrl = 
        "jdbc:sqlserver://localhost;"
                + "database=CSJobsInterface;"
                + "user=dbuser;"
                + "password=scsd431134dscs;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";

        Connection conn = null;
        Statement statement = null;
        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection established...");

        }
        catch (SQLException e) {
            System.out.println("Connection Failed:");
            e.printStackTrace();
        }    
        
        /*
         *  Loop Menu of Database Options
         */
        Scanner sc = new Scanner(System.in);
        boolean loop = true;

        int input;
        while(loop) {
            System.out.println("--------------------------------------");
            System.out.println("Database Menu");
            System.out.println("[1] Reset Database");
            System.out.println("[2] Insert Data");
            System.out.println("[3] Update Data");
            System.out.println("[4] Delete Data");
            System.out.println("[5] Select Data");
            System.out.println("[6] QUIT");
            System.out.println("Select a numeric option and hit Enter...");
            // Store text input from CLI
            input = sc.nextInt();

            switch(input) {
                case 1:
                    resetDatabase();
                    break;
                case 2:
                    insertDataInterface();
                    break;
                case 3:
                    updateDataInterface();
                    break;
                case 4:
                    deleteDataInterface();
                    break;
                case 5:
                    selectDataInterface(conn, sc);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("You did not input a valid option. Please try again.");
                    break;
            }
        }
        sc.close();
    }

    public static void resetDatabase() {
        System.out.println("Reset Database");
    }

    public static void insertDataInterface() {
        System.out.println("Insert Data");
    }

    public static void updateDataInterface() {
        System.out.println("Update Data");
    }

    public static void deleteDataInterface() {
        System.out.println("Delete Data");
    }

    public static void selectDataInterface(Connection conn, Scanner sc) {
        System.out.println("--------------------------------------");
        System.out.println("Select Data");

        try {
            /*
             * Let user choose table to select data from
             */
            DatabaseMetaData data = conn.getMetaData();
            ResultSet resultSet = data.getTables(null, "dbo", null, new String[]{"TABLE"});
            
            // Print Table Menu
            System.out.println("Table Menu");
            System.out.println("----------------------------------");

            int count = 1;
            ArrayList<String> tableNames = new ArrayList<String>();
            while(resultSet.next())
            {
                String tableName = resultSet.getString("TABLE_NAME");

                tableNames.add(tableName);
                System.out.println("[" + count + "] " + tableName);
                count += 1;
            }
            System.out.println("[" + count + "] GO BACK");
            System.out.println("Select a numeric option and hit Enter...");

            // Receive table selection input
            int input = sc.nextInt();
            while (input < 1 || input > count) {
                System.out.println("Please input a valid option.");
                input = sc.nextInt();
            }

            /*
             * Let user choose columns to select 
             */
            if (input != count) {
                String tableName = tableNames.get(input - 1);
                

            }

        }   
        catch (SQLException e) {
            System.out.println("SQL Exception in selectData");
        }
    }

    // ArrayList<String> values - represents each value in the inserted tuple
    // String tableName - table to insert into
    public static void insertData(ArrayList<String> values, String tableName) {

    }

    // ArrayList<String> values - represents each value in the inserted tuple
    // String tableName - table to update
    public static void updateData() {

    }

    // ArrayList<String> primarykey - list values representing the primary key to delete
    // String tableName - table to delete from
    public static void deleteData() {

    }

    // ArrayList<String> columns - list of column names to select
    // String tableName - table to select from
    public static void selectData(Connection conn, ArrayList<String> columns, String tableName) {
        // Create Query
        String columns_parsed = "";
        for (int i = 0; i < columns.size(); i++) {
            columns_parsed += columns.get(i);

            if (i != columns.size() - 1) {
                columns_parsed +=  ", ";
            }
        }
        String query = "select " + columns_parsed + " from " + tableName;
        
        // Execute Query
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Iterate over each row
            while (rs.next()) {
                // Iterate over each value and print
                for (int i = 0; i < columns.size(); i++) {
                    if (i < columns.size() - 1) {
                        System.out.print(rs.getString(columns.get(i)) + ", ");
                    }
                    else {
                        System.out.println(rs.getString(columns.get(i)));
                    }
                }
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in selectData()");
        }
    }
}
