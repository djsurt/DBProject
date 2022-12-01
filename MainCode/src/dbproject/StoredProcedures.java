package dbproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StoredProcedures {

    static Scanner sc = DatabaseMenu.sc;
    static Connection conn = DatabaseMenu.conn;

    /**
     * Helper method to print the query result after calling a stored procedure
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
    public void selectCompanyFromPrestige() {

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

}
