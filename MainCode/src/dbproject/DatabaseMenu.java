package dbproject;

import java.util.List;
import java.util.Scanner;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseMenu {

    static Connection conn = null;
    static Scanner sc = null;

    static final String connectionUrl =
            "jdbc:sqlserver://localhost;"
                    + "database=CSJobsInterface;"
                    + "user=dbuser;"
                    + "password=scsd431134dscs;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "loginTimeout=30;";

    /**
     * Establish a connection to a database
     * @param connectionUrl url to connect to the database
     */
    private static void connectToDatabase(String connectionUrl) {
        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection established...");

        }
        catch (SQLException e) {
            System.out.println("Connection Failed:");
            e.printStackTrace();
        }
    }

    public static void main (String args[]) {
        connectToDatabase(connectionUrl);

        /*
         *  Loop Menu of Database Options
         */
        sc = new Scanner(System.in);
        boolean loop = true;
        int input;

        List<String> menuOptions = List.of(
                "Reset Database", "Insert Data", "Update data", "Delete data", "Select Data", "Special Select Statements Menu", "QUIT"
        );

        while(loop) {
            // Print menu options
            printMenu("Database Menu", menuOptions);

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
                    selectDataInterface();
                    break;
                case 6:
                    specialSelectInterface();
                    break;
                case 7:
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

    /*
     * Empties all tables and inserts the initial records back into the database
     */
    public static void resetDatabase() {
        String callStoredProc = "{call dbo.FullDatabaseReset()}";

        try (CallableStatement prepsStoredProc = conn.prepareCall(callStoredProc);) {
            prepsStoredProc.execute();
            System.out.println("Database successfully reset.");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
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
            sc.nextLine();
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                String type = types.get(i);

                System.out.println("Please insert a value for " + col + " of type " + type + "...");
                String strInput = sc.nextLine();

                values.add(strInput);
            }
            
            insertData(values, columns, tableName);
        }
    }

    public static void updateDataInterface() {
        /*
        * Access database and launch menu of table options, receiving user input
        */
        ResultSet rs = getTableResultSet();
        ArrayList<String> options = getTablesFromRS(rs);
        int input = launchMenu( "Table Menu - Choose a table to UPDATE", options);

        /*
        * Let user choose columns to select 
        */
        if (input != options.size()) {
            /*
            * Access database and retrieve columns and types
            */
            String tableName = options.get(input - 1);
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> columns = getColumnsFromRSMD(rsmd);
            ArrayList<String> col_types = mapSQLTypeArrayToString(getColumnTypesFromRSMD(rsmd));

            /*
             * Access daatabase and retrieve primary keys and types
             */
            rs = getPrimaryKeyResultSet(tableName);
            ArrayList<String> pk = getPrimaryKeysFromRS(rs);
            rs = getPrimaryKeyResultSet(tableName);
            ArrayList<String> pk_types = mapSQLTypeArrayToString(getPrimaryKeyTypesFromRS(rs));

            /*
             * Retrieve user input on which primary key values to update at
             */
            ArrayList<ArrayList<String>> columnInfo = new ArrayList<>();
            columnInfo.add(pk);
            columnInfo.add(pk_types);

            System.out.println("The table " + tableName + " requires the following primary key values:");
            System.out.println(formatAsTable(columnInfo));

            ArrayList<String> pk_values = new ArrayList<String>();
            sc.nextLine();
            for (int i = 0; i < pk.size(); i++) {
                String col = pk.get(i);
                String type = pk_types.get(i);

                System.out.println("Please insert a value for primary key " + col + " of type " + type + " to update at...");
                String strInput = sc.nextLine();

                pk_values.add(strInput);
            }
            
            System.out.println("You will be updating values WHERE " + parseWithDelimiterEquals(pk_values, pk, ", "));

            // Remove primary keys from columns
            for (String key : pk) {
                columns.remove(0);
            }

            /*
             * Receive input from user on which columns to update
             */
            // CASE 1: STOP ADDING COLUMNS is not a menu option
            int colInput = launchMenu(tableName + " Column Menu - Choose a column to UPDATE (you will be given the opportunity to choose more)", columns);
            
            ArrayList<String> output = new ArrayList<String>();
            ArrayList<String> output_types = new ArrayList<String>();
            String curr = columns.get(colInput - 1);
            String curr_type = col_types.get(colInput - 1);
            columns.add("STOP ADDING COLUMNS");

            // CASE 2: Loop through options while not STOP ADDING COLUMNS
            while (colInput != columns.size()) {
                output.add(curr);
                output_types.add(curr_type);

                columns.remove(colInput - 1);
                col_types.remove(colInput - 1);

                colInput = launchMenu(tableName + " Column Menu - Select another column to UPDATE ", columns);
                curr = columns.get(colInput - 1);
                curr_type = col_types.get(colInput - 1);
            }
            
            System.out.println("The following columns (" + parseWithDelimiter(output, ", ") + ") will be updated WHERE " + parseWithDelimiterEquals(pk_values, pk, ", "));

            /*
            * Receive user input for each column
            */
            ArrayList<String> values = new ArrayList<String>();
            sc.nextLine();
            for (int i = 0; i < output.size(); i++) {
                String col = output.get(i);
                String type = output_types.get(i);

                System.out.println("Please insert a value for " + col + " of type " + type + "...");
                String strInput = sc.nextLine();

                values.add(strInput);
            }

            updateData(values, output, output_types, pk_values, pk, pk_types, tableName);
        }
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
            * Access database and print primary key information
            */
            String tableName = options.get(input - 1);
            rs = getPrimaryKeyResultSet(tableName);
            ArrayList<String> columns = getPrimaryKeysFromRS(rs);
            rs = getPrimaryKeyResultSet(tableName);
            ArrayList<String> types = mapSQLTypeArrayToString(getPrimaryKeyTypesFromRS(rs));

            ArrayList<ArrayList<String>> columnInfo = new ArrayList<>();
            columnInfo.add(columns);
            columnInfo.add(types);

            System.out.println("The table " + tableName + " requires the following primary key values:");
            System.out.println(formatAsTable(columnInfo));

            /*
             * Receive user input for each primary key
             */
            ArrayList<String> values = new ArrayList<String>();
            sc.nextLine();
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                String type = types.get(i);

                System.out.println("Please insert a value for primary key " + col + " of type " + type + "...");
                String strInput = sc.nextLine();

                values.add(strInput);
            }
            
            deleteData(values, columns, tableName);
        }

    }

    public static void selectDataInterface() {
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
    * specialSelectInterface - runs the special select menu
    */
    public static void specialSelectInterface() {
        List<String> menuOptions = List.of(
            "Select Companies With Location", 
            "Select Companies From Prestige", 
            "Select Company From Industry", 
            "Select Company From Employee Range", 
            "Select Jobs From Cycle", 
            "Select Job From Years of Experience",
            "Select Jobs With Paid Time Off",
            "Select Jobs Applied To",
            "Select Jobs Posted Within Date Range",
            "Select Users Following At Least One Job"
        );

        // Print menu options
        printMenu("Database Menu", menuOptions);

        /*
         * Receive text input from user
         */
        int input = sc.nextInt();

//        switch(input) {
//            case 1:
//                StoredProcedures.selectCompaniesWithLocation();
//                break;
//            case 2:
//                StoredProcedures.selectCompanyFromPrestige();
//                break;
//            case 3:
//                StoredProcedures.selectCompanyFromIndustry();
//                break;
//            case 4:
//                StoredProcedures.selectCompanyFromEmployeeRange();
//                break;
//            case 5:
//                StoredProcedures.selectJobsFromCycle();
//                break;
//            case 6:
//                StoredProcedures.selectJobFromYearsOfExperience();
//                break;
//            case 7:
//                StoredProcedures.selectJobsWithPaidTimeOff();
//                break;
//            case 8:
//                StoredProcedures.selectJobsAppliedTo();
//                break;
//            case 9:
//                StoredProcedures.selectJobsPostedWithinDateRange();
//                break;
//            case 10:
//                StoredProcedures.selectUsersFollowingAtLeastOneJob();
//                break;
//            default:
//                System.out.println("You did not input a valid option. Please try again.");
//                break;
//        }
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

        String query = "INSERT INTO " + tableName + "(" + parseWithDelimiter(columns, ", ") +") values (" + parseWithDelimiter(placeholders, ", ") + ")";
        
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
                System.out.println("Successfully Inserted Values: " + parseWithDelimiter(values, ", "));
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
    public static void updateData(ArrayList<String> values, ArrayList<String> columns, ArrayList<String> col_types, ArrayList<String> pk_values, ArrayList<String> primarykeys, ArrayList<String> pk_types, String tableName) {
        /*
        * Create Query
        */
        ArrayList<String> placeholders_col = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            placeholders_col.add("?");
        }

        ArrayList<String> placeholders_pk = new ArrayList<>();
        for (int i = 0; i < primarykeys.size(); i++) {
            placeholders_pk.add("?");
        }

        String query = "UPDATE " + tableName + " SET " + parseWithDelimiterEquals(placeholders_col, columns, ", ") + " WHERE " + parseWithDelimiterEquals(placeholders_pk, primarykeys, " AND ");
        System.out.println(query);

        /* 
         * Execute Query
         */
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Fill column values
            System.out.println(values);
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                String type = col_types.get(i);
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

            // Fill primary key values
            System.out.println(pk_values);
            for (int i = 0; i < pk_values.size(); i++) {
                String value = pk_values.get(i);
                String type = pk_types.get(i);

                int sql_parameter = values.size() + i + 1;
                // If varchar<50>
                if (type.equals("12")) {
                    pstmt.setString(sql_parameter, value);
                }
                // If Integer
                else if (type.equals("4")) {
                    pstmt.setInt(sql_parameter, Integer.parseInt(value));
                }
                // If DATE
                else if (type.equals("91")) {
                    pstmt.setDate(sql_parameter, java.sql.Date.valueOf(value));
                }
                else { // Covers 4, which is date
                    pstmt.setString(sql_parameter, value);
                }
            }

            int res = pstmt.executeUpdate();
            if (res == 1) {
                System.out.println("Successfully Updated Values: " + parseWithDelimiterEquals(values, columns, ", "));
                System.out.println("Completed Where: " + parseWithDelimiterEquals(pk_values, primarykeys, ", "));
            }
            else {
                System.out.println("Update failed.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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

        String query = "DELETE FROM " + tableName + " WHERE " + parseWithDelimiterEquals(placeholders, primarykey, " AND ");
        System.out.println(query);
        
        /*
         * Execute Query
         */
        try{
            ResultSet rs = getPrimaryKeyResultSet(tableName);
            ResultSetMetaData rsmd = getColumnRSMDFromTable(rs, tableName);
            ArrayList<String> types = getColumnTypesFromRSMD(rsmd);
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                String type = types.get(i);

                // If varchar<50>
                if (type.equals("12") || type.equals("1003")) {
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
                System.out.println("Successfully Deleted Tuple: " + parseWithDelimiter(values, ", "));
            }
            else {
                System.out.println("Deletion failed.");
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
        String query = "select " + parseWithDelimiter(columns, ", ") + " from " + tableName;
        
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
     * Get all the companies from a specific location (e.g. New York, NY)
     */
    public static void selectCompaniesWithLocation() {
        
        /*
         * Create Query
         */
        String query = "EXEC SelectCompaniesWithLocation @Country=?, @City=?, @State=?";

        sc.nextLine();
        System.out.println("Please enter a value for Country of type Varchar...");
        String country = sc.nextLine();
        System.out.println("Please enter a value for State of type Varchar...");
        String state = sc.nextLine();
        System.out.println("Please enter a value for City of type Varchar...");
        String city = sc.nextLine();

        /*
         * Execute Query
         */
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;
        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, country);
            pstmt.setString(2, city);
            pstmt.setString(3, state);
    
            // Query Call
            rs = pstmt.executeQuery();
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
        System.out.println("FUNCTION: Select Companies With Location");
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
        if (sqlType.equals("12") || sqlType.equals("1003")) {
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
     * parseWithDelimiterEquals - parses the values and columns to suffice a WHERE statement
     */
    public static String parseWithDelimiterEquals(ArrayList<String> values, ArrayList<String> columns, String delimiter) {
        ArrayList<String> output = new ArrayList<String>();

        for (int i = 0; i < columns.size(); i++) {
            output.add(columns.get(i) + "=" + values.get(i));
        }

        return parseWithDelimiter(output, delimiter);
    }

    /*
     * Returns a String of the values of an ArrayList with a delimeter 
     */
    public static String parseWithDelimiter(ArrayList<String> array, String delimiter){
        String output = "";
        for (int i = 0; i < array.size(); i++) {
            output += array.get(i);

            if (i != array.size() - 1) {
                output +=  delimiter;
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

    /*
     * Returns the ResultSet of the primary key
     */
    public static ResultSet getPrimaryKeyResultSet(String tableName) {
        ResultSet rs = null;
        try {
            DatabaseMetaData data = conn.getMetaData();
            rs = data.getPrimaryKeys(null, null, tableName);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /*
     * Returns the ResultSetMetaData from a specific table in the ResultSet rs
     */
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
     * Returns an ArrayList<String> of tables in the ResultSet for Menu
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

    public static ArrayList<String> getPrimaryKeysFromRS(ResultSet rs) {
        ArrayList<String> output = new ArrayList<>();

        // Add all valid options
        try {
            while(rs.next())
            {
                output.add(rs.getString("COLUMN_NAME"));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static ArrayList<String> getPrimaryKeyTypesFromRS(ResultSet rs) {
        ArrayList<String> output = new ArrayList<>();
        // Add all valid options
        try {
            while(rs.next())
            {
                output.add(String.valueOf(rs.getType()));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     * Returns an ArrayList<String> of columns in the ResultSetMetaData
     *
     * @param rsmd
     * @return
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

    /**
     * Prints a menu based off menuName and menu options and collects user input
     *
     * @param menuName Title of menu to be displayed
     * @param options a list of options to display
     * @return the input
     */
    public static int launchMenu(String menuName, List<String> options) {
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

    /**
     * Prints a formatted menu based off the menu options and the menuName
     *
     * @param menuName Title of menu to be displayed
     * @param options a list of options to display
     */
    public static void printMenu(String menuName, List<String> options) {
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

    /**
     * Formats and prints a nested ArrayList as a table
     *
     * @param rows
     * @return string version of what is printed
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
