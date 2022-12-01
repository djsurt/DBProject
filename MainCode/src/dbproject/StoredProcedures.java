package dbproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StoredProcedures {

    static Scanner sc = DatabaseMenu.sc;
    static Connection conn = DatabaseMenu.conn;

    /**
     * Helper method to print the query result after calling a stored procedure
     *
     * @param funcName name of the function to display (e.g. SelectCompanyFromPrestige)
     * @param colCount number of columns
     * @param rs ResultSet
     * @param rsmd ResultSetMetaData
     */
    public static void printQueryResult(String funcName, int colCount, ResultSet rs, ResultSetMetaData rsmd) {
        System.out.println("----------------------------------");
        System.out.println("FUNCTION: " + funcName);
        System.out.println("RESULT: ");

        ArrayList<ArrayList<String>> output = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();

        // Add header information to output
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
        System.out.println(DatabaseMenu.formatAsTable(output));
    }

    /**
     * Stored Procedure 1
     *
     * Get all the companies from a specific location
     */
    public static void selectCompaniesWithLocation() {

        String funcName = "SelectCompaniesWithLocation";

        // Create the query
        String query = "EXEC " +funcName+ " @Country=?, @City=?, @State=?";

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

        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 2
     *
     * Get all the companies with a certain prestige level
     */
    public static void selectCompanyFromPrestige() {

        String funcName = "SelectCompanyFromPrestige";

        // Create query
        String query = "EXEC " +funcName+ " @Prestige=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a prestige level: ");
        String prestige = sc.nextLine();

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, prestige);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 3
     *
     * Get all the companies from a specific industry (e.g. Technology)
     */
    public static void selectCompanyFromIndustry() {

        String funcName = "SelectCompanyFromIndustry";

        // Create query
        String query = "EXEC " +funcName+ " @Industry=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter an industry: ");
        String industry = sc.nextLine();

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, industry);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 4
     *
     * Get all the companies with a number of employees between a given range
     */
    public static void selectCompanyFromEmployeeRange() {

        String funcName = "SelectCompanyFromEmployeeRange";

        // Create query
        String query = "EXEC " +funcName+ " @Lowerbound=?, @Upperbound=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a minimum number of employees (inclusive): ");
        int lowerbound = Integer.parseInt(sc.nextLine());

        System.out.println("Enter a maximum number of employees (exclusive): ");
        int upperbound = Integer.parseInt(sc.nextLine());

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, lowerbound);
            pstmt.setInt(2, upperbound);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 5
     *
     * Get all jobs from a specific cycle (e.g. Summer)
     */
    public static void selectJobsFromCycle() {

        String funcName = "SelectJobsFromCycle";

        // Create query
        String query = "EXEC " +funcName+ " @Cycle=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a cycle (e.g. Summer): ");
        String cycle = sc.nextLine();

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cycle);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 6
     *
     * Get all jobs from with a total compensation between a given range
     */
    public static void selectJobFromCompensationRange() {

        String funcName = "SelectJobFromCompensationRange";

        // Create query
        String query = "EXEC " +funcName+ " @Lowerbound=?, @Upperbound=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a minimum number for total compensation (inclusive): ");
        int lowerbound = Integer.parseInt(sc.nextLine());

        System.out.println("Enter a maximum number for total compensation (inclusive): ");
        int upperbound = Integer.parseInt(sc.nextLine());

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, lowerbound);
            pstmt.setInt(2, upperbound);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 7
     *
     * Get all the jobs with a specified minimum years of experience
     */
    public static void selectJobFromYearsOfExperience() {

        String funcName = "SelectJobFromYearsOfExperience";

        // Create query
        String query = "EXEC " +funcName+ " @Min=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a minimum number for years of experience: ");
        int min = Integer.parseInt(sc.nextLine());

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, min);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 8
     *
     * Get all the jobs with some paid time off
     */
    public static void selectJobsWithPaidTimeOff() {

        String funcName = "SelectJobsWithPaidTimeOff";

        // Create query
        String query = "EXEC " +funcName;

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 9
     *
     * Get all the jobs from JobsFollowed that have been applied to
     */
    public static void selectJobsAppliedTo() {

        String funcName = "SelectJobsAppliedTo";

        // Create query
        String query = "EXEC " +funcName;

        sc.nextLine();

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 10
     *
     * Get all jobs that were posted within a specific date range
     */
    public static void selectJobsPostedWithinDateRange() {

        String funcName = "selectJobsPostedWithinDateRange";

        // Create query
        String query = "EXEC " +funcName+ " @Lowerbound=?, @Upperbound=?";

        sc.nextLine();

        // Getting user input
        System.out.println("Enter a start date (inclusive): ");
        int lowerbound = Integer.parseInt(sc.nextLine());

        System.out.println("Enter an end date (inclusive): ");
        int upperbound = Integer.parseInt(sc.nextLine());

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, lowerbound);
            pstmt.setInt(2, upperbound);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }

    /**
     * Stored Procedure 11
     *
     * Get all jobs that were posted within a specific date range
     */
    public static void selectUsersFollowingAtLeastOneJob() {

        String funcName = "SelectUsersFollowingAtLeastOneJob";

        // Create query
        String query = "EXEC " +funcName;

        sc.nextLine();

        // Execute query
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCount = 0;

        try {
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Query Call
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the result
        printQueryResult(funcName, colCount, rs, rsmd);
    }
}
