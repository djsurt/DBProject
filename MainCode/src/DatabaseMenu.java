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
        /*
        * Access database and launch menu of table options, receiving user input
        */
        ResultSet rs = getTableResultSet();
        ArrayList<String> options = getTablesFromRS(rs);
        int input = launchMenu( "Table Menu - Choose a table to INSERT into", options);

        /*
        * Let user choose columns to select 
        */
        if (input != options.size()) {
            /*
             * Access database and get columns in selected table
             */
            String tableName = options.get(input - 1);
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);

            // Display list of required insert information
            ArrayList<String> columns = getColumnsFromRSMD(rsmd);
            ArrayList<String> types = mapSQLTypeArrayToString(getColumnTypesFromRSMD(rsmd));

            ArrayList<ArrayList<String>> columnInfo = new ArrayList<>();
            columnInfo.add(columns);
            columnInfo.add(types);

            System.out.println("The table " + tableName + " requires the following values:");
            System.out.println(formatAsTable(columnInfo));

            ArrayList<String> values = new ArrayList<String>();
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                String type = types.get(i);

                System.out.println("Please insert a value for " + col + " of type " + type + "...");
                String strInput = sc.next();

                values.add(strInput);
            }
            
            insertData(values, columns, tableName);
        }
    }

    public static void updateDataInterface() {
        System.out.println("Update Data");
    }

    public static void deleteDataInterface() {
        /*
        * Access database and launch menu of table options, receiving user input
        */
        ResultSet rs = getTableResultSet();
        ArrayList<String> options = getTablesFromRS(rs);
        int input = launchMenu( "Table Menu - Choose a table to DELETE from", options);

        /*
        * Let user choose columns to select 
        */
        if (input != options.size()) {
            /*
            * Access database and launch menu of column options, receiving user input
            */
            String tableName = options.get(input - 1);
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> columns = getColumnsFromRSMD(rsmd);
            columns.add("*");
            int colInput = launchMenu(tableName + " Column Menu - Choose a column to DELETE from", columns);
        }


    }

    public static void selectDataInterface(Scanner sc) {
        /*
        * Access database and launch menu of table options, receiving user input
        */
        ResultSet rs = getTableResultSet();
        ArrayList<String> options = getTablesFromRS(rs);
        int input = launchMenu( "Table Menu - Choose a table to SELECT from", options);

        /*
        * Let user choose columns to select 
        */
        if (input != options.size()) {
            /*
            * Access database and launch menu of column options, receiving user input
            */
            String tableName = options.get(input - 1);
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> columns = getColumnsFromRSMD(rsmd);
            columns.add("*");
            int colInput = launchMenu(tableName + " Column Menu - Choose a column to SELECT from (you will be given the opportunity to choose more)", columns);
            
            ArrayList<String> output = new ArrayList<String>();
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
    public static void insertData(ArrayList<String> values, ArrayList<String> columns, String tableName) {
        /*
         * Create Query
         */
        ArrayList<String> placeholders = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            placeholders.add("?");
        }

        String query = "INSERT INTO " + tableName + "(" + parseWithDelimiter(columns) +") values (" + parseWithDelimiter(placeholders) + ")";
        System.out.println(query);
        
        /*
         * Execute Query
         */
        try{
            ResultSet rs = getTableResultSet();
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> types = getColumnTypesFromRSMD(rsmd);
            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                String type = types.get(i);

                // If varchar<50>
                if (type.equals("12")) {
                    pstmt.setString(i + 1, value);
                }
                // If Integer
                else if (type.equals("4")) {
                    pstmt.setInt(i + 1, Integer.parseInt(value));
                }
                // If DATE
                else if (type.equals("91")) {
                    pstmt.setDate(i + 1, java.sql.Date.valueOf(value));
                }
                else { // Covers 4, which is date
                    pstmt.setString(i + 1, value);
                }
            }

            int res = pstmt.executeUpdate();
            if (res == 1) {
                System.out.println("Successfully Inserted Values: " + parseWithDelimiter(values));
            }
            else {
                System.out.println("Insertion failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
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
    * ArrayList<String> columns - represents the 
    * String tableName - table to delete from
    */
    public static void deleteData(ArrayList<String> values, ArrayList<String> primarykey, String tableName) {
        /*
         * Create Query
         */
        ArrayList<String> placeholders = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            placeholders.add("?");
        }

        String query = "DELETE FROM " + tableName + " WHERE " + parseWhereFormat(values, primarykey);
        System.out.println(query);
        
        /*
         * Execute Query
         */
        try{
            ResultSet rs = getTableResultSet();
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> types = getColumnTypesFromRSMD(rsmd);
            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                String type = types.get(i);

                // If varchar<50>
                if (type.equals("12")) {
                    pstmt.setString(i + 1, value);
                }
                // If Integer
                else if (type.equals("4")) {
                    pstmt.setInt(i + 1, Integer.parseInt(value));
                }
                // If DATE
                else if (type.equals("91")) {
                    pstmt.setDate(i + 1, java.sql.Date.valueOf(value));
                }
                else { // Covers 4, which is date
                    pstmt.setString(i + 1, value);
                }
            }

            int res = pstmt.executeUpdate();
            if (res == 1) {
                System.out.println("Successfully Inserted Values: " + parseWithDelimiter(values));
            }
            else {
                System.out.println("Insertion failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
            e.printStackTrace();     
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
            e.printStackTrace();    
        }

        // Print output
        System.out.println(formatAsTable(output));
    }

    /*
     * Maps an ArrayList of String representing SQL Type codes to the String values of the type
     */
    public static ArrayList<String> mapSQLTypeArrayToString(ArrayList<String> types) {
        ArrayList<String> output = new ArrayList<>();
        for (String type : types) {
            output.add(mapSQLTypeToString(type));
        }

        return output;
    }

    /*
     * Maps an String SQL Type Code representing SQL Type to the String name of the type
     */
    public static String mapSQLTypeToString(String sqlType) {
        if (sqlType.equals("12")) {
            return "Varchar";
        }
        else if (sqlType.equals("4")) {
            return "Integer";
        }
        else if (sqlType.equals("91")) {
            return "Date (yyyy-MM-dd)";
        }
        else {
            return "err";
        }
    }

    /*
     * parseWher
     * 
     */
    public static String parseWhereFormat(ArrayList<String> values, ArrayList<String> columns) {

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
     * Returns the ResultSet with all table names
     */
    public static ResultSet getTableResultSet() {
        ResultSet rs = null;
        try {
            DatabaseMetaData data = conn.getMetaData();
            rs = data.getTables(null, "dbo", null, new String[]{"TABLE"});
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static ResultSetMetaData getColumnRSMDFromTable(ResultSet rs, String tableName) {
        ResultSetMetaData rsmd = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from " + tableName);
            rsmd = rs.getMetaData();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return rsmd;
    }

    /*
     * Returns an ArrayList<String> of tables in the ResultSet
     */
    public static ArrayList<String> getTablesFromRS(ResultSet rs) {
        ArrayList<String> options = new ArrayList<>();

        // Add all valid options
        try {
            while(rs.next())
            {
                options.add(rs.getString("TABLE_NAME"));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        options.add("GO BACK");

        return options;

    }

    /*
     * Returns an ArrayList<String> of columns in the ResultSetMetaData
     */
    public static ArrayList<String> getColumnsFromRSMD(ResultSetMetaData rsmd) {
        ArrayList<String> columns = new ArrayList<String>();

        try {
            int count = rsmd.getColumnCount();
            for(int i = 1; i <= count; i++) {
               columns.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }

    public static ArrayList<String> getColumnTypesFromRSMD(ResultSetMetaData rsmd) {
        ArrayList<String> types = new ArrayList<String>();

        try {
            int count = rsmd.getColumnCount();
            for(int i = 1; i <= count; i++) {
               types.add(String.valueOf(rsmd.getColumnType(i)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
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
