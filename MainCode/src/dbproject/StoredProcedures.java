/**
 * This file contains the code for all 10 of our stored procedures
 */

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
     * Use case: Get the number of users who have applied to given company
     */
    public static void GetNumUsersAppliedToCompany() {

        String funcName = "GetNumUsersAppliedToCompany";
        String query = "EXEC " +funcName+ " @Company=?"; // @Company refers to Company.name

        // Get user input
        sc.nextLine();

        System.out.println("Enter a company (e.g. Google): ");
        String company = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, company);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 2
     *
     * Use case: Select all jobs from a specific company
     */
    public static void SelectJobsFromCompany() {

        String funcName = "SelectJobsFromCompany";
        String query = "EXEC " +funcName+ " @Company=?"; // @Company refers to Company.name

        // Get user input
        sc.nextLine();

        System.out.println("Enter a company (e.g. Google): ");
        String company = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, company);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 3
     *
     * Use case: Select all jobs from a specific industry
     */
    public static void SelectJobsFromIndustry() {

        String funcName = "SelectJobsFromIndustry";
        String query = "EXEC " +funcName+ " @Industry=?"; // @Industry refers to Company.industry

        // Get user input
        sc.nextLine();

        System.out.println("Enter an industry (e.g. Defense): ");
        String industry = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, industry);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 4
     *
     * Use case: Get all jobs, with company name and benefits, during a specific cycle, at a specific state, and for a specific role
     */
    public static void SelectJobInfoWithParameters() {

        String funcName = "SelectJobInfoWithParameters";
        String query = "EXEC " +funcName+ " @Cycle=?, @Country=?, @State=?, @Role=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a cycle (e.g. Summer): ");
        String cycle = sc.nextLine();
        System.out.println("Enter a country (e.g. USA): ");
        String country = sc.nextLine();
        System.out.println("Enter a state (e.g. Ohio): ");
        String state = sc.nextLine();
        System.out.println("Enter a role (e.g. Software Engineering): ");
        String role = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cycle);
            pstmt.setString(2, country);
            pstmt.setString(3, state);
            pstmt.setString(4, role);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 5
     *
     * Use case: Get mean, min, and max insurance and paid time off benefits by city for a specific state
     */
    public static void GetBenefitInfoByCityFromState() {

        String funcName = "GetBenefitInfoByCityFromState";
        String query = "EXEC " +funcName+ " @Country=?, @State=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a country (e.g. USA): ");
        String country = sc.nextLine();
        System.out.println("Enter a state (e.g. Ohio): ");
        String state = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, country);
            pstmt.setString(2, state);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 6
     *
     * Use case: Get ordered list of most applied to jobs within a specific prestige
     */
    public static void GetPopularJobsByApplicationsFromPrestige() {

        String funcName = "GetPopularJobsByApplicationsFromPrestige";
        String query = "EXEC " +funcName+ " @Prestige=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a prestige (e.g. FAANG): ");
        String prestige = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, prestige);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 7
     *
     * Use case: Get all the jobs with some paid time off
     */
    public static void SelectJobsWithPaidTimeOffGreaterThan() {

        String funcName = "SelectJobsWithPaidTimeOffGreaterThan";
        String query = "EXEC " +funcName+ " @PaidTime=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a minimum amount of paid time off (e.g. 5): ");
        int paidTime = Integer.parseInt(sc.nextLine());

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paidTime);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 8
     *
     * Use case: Get an ordered list of the most applied to locations for a specific role
     */
    public static void GetPopularLocationsByApplicationFromRole() {

        String funcName = "GetPopularLocationsByApplicationFromRole";
        String query = "EXEC " +funcName+ " @Role=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a role (e.g. Software Engineering): ");
        String role = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, role);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 9
     *
     * Use case: Get all jobs that were posted within a specific date range (inclusive) from a specific company
     */
    public static void SelectJobsFromCompanyPostedWithinDateRange() {

        String funcName = "SelectJobsFromCompanyPostedWithinDateRange";
        String query = "EXEC " +funcName+ " @Company=?, @Lowerbound=?, @Upperbound=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a company (e.g. Google): ");
        String company = sc.nextLine();
        System.out.println("Enter a lower bound for the date (e.g. 2022-01-01): ");
        String lowerbound = sc.nextLine();
        System.out.println("Enter an upper bound (e.g. 2022-02-01): ");
        String upperbound = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, company);
            pstmt.setString(2, lowerbound);
            pstmt.setString(3, upperbound);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }

    /**
     * Stored Procedure 10
     *
     * Use case: Get all users who applied to jobs within a specific cycle
     */
    public static void SelectUsersWhoAppliedForCycle() {

        String funcName = "SelectUsersWhoAppliedForCycle";
        String query = "EXEC " +funcName+ " @Cycle=?";

        // Get user input
        sc.nextLine();

        System.out.println("Enter a cycle (e.g. Summer): ");
        String cycle = sc.nextLine();

        // Execute the query
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        int colCount = 0;

        try{
            // Access Database
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cycle);

            // Query Call
            resultSet = pstmt.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            colCount = resultSetMetaData.getColumnCount();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        printQueryResult(funcName, colCount, resultSet, resultSetMetaData);
    }
}
