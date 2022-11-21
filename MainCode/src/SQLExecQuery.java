import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecQuery {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        String connectionUrl = 
        "jdbc:sqlserver://localhost;"
                + "database=university;"
                + "user=dbuser;"
                + "password=scsd431134dscs;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT top 100 ID, Name, Dept_Name from Instructor";
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}