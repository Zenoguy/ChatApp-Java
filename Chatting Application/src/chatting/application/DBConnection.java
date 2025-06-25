package chatting.application;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/chatapp",
                    "root",   // replace with your MySQL user
                    "" );   // replace with your MySQL password
                System.out.println("Connected to the database.");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    // TEMP main method for testing
}
