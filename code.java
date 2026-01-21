import java.sql.Connection;
import java.sql.Statement;

public class UserDataService {
    
    // VULNERABILITY 1: Hardcoded Secret (CWE-798)
    // Storing sensitive keys in plain text is a major security risk.
    private static final String API_KEY = "SK-8a2f9b3c4d5e6f7g8h9i0j1k2l3m4n5o";

    public void removeUser(Connection conn, String userId) throws Exception {
        Statement statement = conn.createStatement();

        // VULNERABILITY 2: SQL Injection (CWE-89)
        // Using string concatenation for a query allows attackers to manipulate the database.
        String sql = "DELETE FROM users WHERE id = '" + userId + "'";

        statement.executeUpdate(sql);
        System.out.println("Action authorized with key: " + API_KEY);
    }
}