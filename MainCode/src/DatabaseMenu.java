import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseMenu {
    public static void main (String args[]) {

        /*
         *  Establish Connection to Database
         */
        
        String connectionUrl = 
        "jdbc:sqlserver://localhost;"
                + "database=university;"
                + "user=dbuser;"
                + "password=scsd431134dscs;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";

        Connection conn;
        Statement statement;
        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(connectionUrl);
            statement = conn.createStatement();
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
            System.out.println("Database Menu - Input number of choice");
            System.out.println("[1] Reset Database");
            System.out.println("[2] Insert Data");
            System.out.println("[3] Update Data");
            System.out.println("[4] Delete Data");
            System.out.println("[5] Select Data");
            System.out.println("[6] Quit");

            // Store text input from CLI
            input = sc.nextInt();

            switch(input) {
                case 1:
                    resetDatabase();
                    break;
                case 2:
                    insertData();
                    break;
                case 3:
                    updateData();
                    break;
                case 4:
                    deleteData();
                    break;
                case 5:
                    selectData();
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

    public static void insertData() {
        System.out.println("Insert Data");
    }

    public static void updateData() {
        System.out.println("Update Data");
    }

    public static void deleteData() {
        System.out.println("Delete Data");
    }

    public static void selectData() {
        System.out.println("Select Data");
    }
}
