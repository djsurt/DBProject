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
            System.out.println("----------------------------------");
            System.out.println("Table Menu");

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
                ArrayList<String> columns = new ArrayList<String>();
                columns.add("*");
                selectData(conn, columns, tableName);

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
    public static void updateData(ArrayList<String> values, String tableName) {

    }

    // ArrayList<String> primarykey - list values representing the primary key to delete
    // String tableName - table to delete from
    public static void deleteData(ArrayList<String> primarykey, String tableName) {

    }

    // ArrayList<String> columns - list of column names to select
    // String tableName - table to select from
    public static void selectData(Connection conn, ArrayList<String> columns, String tableName) {
        // Create Query
        String query = "select " + parseWithDelimiter(columns) + " from " + tableName;
        
        // Execute Query
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            // Print Query Info
            System.out.println("----------------------------------");
            System.out.println("QUERY: " + query);
            System.out.println("RESULT: ");

            // Create output variable
            ArrayList<ArrayList<String>> output = new ArrayList<>();

            // Add column names to output
            ArrayList<String> headers = new ArrayList<>();
            for (int i = 1; i <= colCount; i++) {
                headers.add(rsmd.getColumnName(i));
            }
            output.add(headers);

            // Add data to output
            while (rs.next()) {
                // Add current row of data to output
                ArrayList<String> row = new ArrayList<>();

                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i));
                }
                output.add(row);
            }   

            // Print output
            System.out.println(formatAsTable(output));
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in selectData()");
        }
    }

    /*
     * Returns a String of the values of an ArrayList with a ", " delimeter 
     */
    public static String parseWithDelimiter(ArrayList<String> array){
        String output = "";
        for (int i = 0; i < array.size(); i++) {
            output += array.get(i);

            if (i != array.size() - 1) {
                output +=  ", ";
            }
        }
        return output;
    }

    /*
     *  Formats and prints a nested ArrayList as a table
     */
    public static String formatAsTable(ArrayList<ArrayList<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (ArrayList<String> row : rows) {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (ArrayList<String> row : rows) {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }
}
