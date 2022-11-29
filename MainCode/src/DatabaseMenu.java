import java.util.Scanner;
import java.sql.*;  
import java.util.ArrayList;

public class DatabaseMenu {

    static Connection conn = null;
    static Scanner sc = null;
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
        sc = new Scanner(System.in);
        boolean loop = true;

        int input;
        while(loop) {
            /*
             * Print menu options
             */
            ArrayList<String> options = new ArrayList<>();
            options.add("Reset Database");
            options.add("Insert Data");
            options.add("Update Data");
            options.add("Delete Data");
            options.add("Select Data");
            options.add("QUIT");
            printMenu("Database Menu", options);

            /*
             * Receive text input from user
             */
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
                    selectDataInterface(sc);
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

    public static void selectDataInterface(Scanner sc) {
        /*
        * Access data to query
        */
        ResultSet rs = null;
        try {
            DatabaseMetaData data = conn.getMetaData();
            rs = data.getTables(null, "dbo", null, new String[]{"TABLE"});
        }
        catch(SQLException e) {
            System.out.println("SQL Exception Error: Occured at selectDataInterface during creation of resultSet.");
        }

        /*
         * Print menu
         */
        ArrayList<String> options = new ArrayList<>();

        // Add all valid options
        try {
            while(rs.next())
            {
                options.add(rs.getString("TABLE_NAME"));
            }
        } 
        catch (SQLException e) {
            System.out.println("SQL Exception Error: Occured at selectDataInterface while printing table menu.");
        }

        options.add("GO BACK");

        /*
         * Initializes a menu and collects user input
         */
        int input = launchMenu( "Table Menu - Select Data", options);

        /*
        * Let user choose columns to select 
        */
        if (input != options.size()) {
            String tableName = options.get(input - 1);

            /*
             * Access database for column names
             */
            ResultSetMetaData rsmd = null;
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from " + tableName);
                rsmd = rs.getMetaData();
            }
            catch (SQLException e) {
                System.out.println("SQL Exception Error: Occured at selectDataInterface while accessing column names.");
            }
            
            /*
             * Populate list of column options to select from
             */
            ArrayList<String> columns = new ArrayList<String>();

            try {
                int count = rsmd.getColumnCount();
                for(int i = 1; i <= count; i++) {
                   columns.add(rsmd.getColumnName(i));
                }
            }
            catch (SQLException e) {
                System.out.println("SQL Exception Error: Occured at selectDataInterface while populating column options.");
            }

            // Add options * 
            columns.add("*");

            /*
             * Receive input from user 
             */
            ArrayList<String> output = new ArrayList<String>();
            int colInput = launchMenu("Column Menu - Select A Column from " + tableName, columns);

            // CASE 1: if star is selected, add it alone to output
            if (colInput == columns.size()) {
                output.add(columns.get(colInput - 1));
            }
            // CASE 2: if star is not selected, add selection to output, remove from columns, and replace * with STOP ADDING COLUMNS
            else {
                String curr = columns.get(colInput - 1);
                columns.remove(columns.size() - 1);
                columns.add("STOP ADDING COLUMNS");

                // Loop through options while not STOP ADDING COLUMNS
                while (colInput != columns.size()) {
                    output.add(curr);
                    columns.remove(colInput - 1);

                    colInput = launchMenu("Column Menu - Select A Column from " + tableName, columns);
                    curr = columns.get(colInput - 1);
                }
            }
            
            selectData(output, tableName);
        }
    }

    /*
    * insertData - inserts data into database
    * ArrayList<String> values - represents each value in the inserted tuple
    * String tableName - table to insert into
    */
    public static void insertData(ArrayList<String> values, String tableName) {
        /*
         * Create Query
         */
        String query = "INSERT INTO " + tableName + "values (" + parseWithDelimiter(values) + ")";

        /*
         * Execute Query
         */
        try{
            Statement stmt = conn.createStatement();
            stmt.executeQuery(query);
            System.out.println("Successfully Inserted Values: " + parseWithDelimiter(values));
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in insertData()");
        }
    }

    /*
    * updateData - updates data from the database
    * ArrayList<String> values - represents each value in the inserted tuple
    * String tableName - table to update
    */
    public static void updateData(ArrayList<String> values, String tableName) {

    }

    /* 
    * deleteData - deletes data from the database
    * ArrayList<String> primarykey - list values representing the primary key to delete
    * String tableName - table to delete from
    */
    public static void deleteData(ArrayList<String> primarykey, String tableName) {

    }

    /* 
    * selectData - selects data from the database
    * ArrayList<String> columns - list of column names to select
    * String tableName - table to select from
    */
    public static void selectData(ArrayList<String> columns, String tableName) {
        /*
         * Create Query
         */
        String query = "select " + parseWithDelimiter(columns) + " from " + tableName;
        
        /*
         * Access database
         */
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in selectData() with initial access.");
        }

        /*
         * Print query result
         */
        System.out.println("----------------------------------");
        System.out.println("QUERY: " + query);
        System.out.println("RESULT: ");

        ArrayList<ArrayList<String>> output = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();

        // Add header information to ouput
        try {
            for (int i = 1; i <= colCount; i++) {
                headers.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in selectData() with headers.");       
        }
        output.add(headers);

        // Add data to output
        try {
            while (rs.next()) {
                // Add current row of data to output
                ArrayList<String> row = new ArrayList<>();

                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i));
                }
                output.add(row);
            }   
        }
        catch (SQLException e) {
            System.out.println("SQL Statement Error in selectData() with data.");       
        }

        // Print output
        System.out.println(formatAsTable(output));
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
     * Prints a menu based off menuName and menu options and collects user input
     */
    public static int launchMenu(String menuName, ArrayList<String> options) {
        printMenu(menuName, options);

        /*
         * Receive valid user input
         */
        int input = sc.nextInt();
        while (input < 1 || input > options.size()) {
            System.out.println("Please input a valid option.");
            input = sc.nextInt();
        }

        return input;
    }

    /*
     * Prints a formatted menu based off the menu options and the menuName
     */
    public static void printMenu(String menuName, ArrayList<String> options) {
        System.out.println("----------------------------------");
        System.out.println(menuName);

        // Create output
        ArrayList<ArrayList<String>> output = new ArrayList<>();

        // Add headers to output
        ArrayList<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("Option");
        output.add(headers);

        // Add options to output
        for (int i = 0; i < options.size(); i++) {
            ArrayList<String> row = new ArrayList<>();
            row.add("[" + (i + 1) + "]");
            row.add(options.get(i));
            output.add(row);        
        }

        // Print output
        System.out.println(formatAsTable(output));
        System.out.println("Select a numeric option and hit Enter...");
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
