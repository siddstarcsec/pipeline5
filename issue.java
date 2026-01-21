import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class VulnerableController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // --- INPUTS FROM USER ---
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        String reportName = request.getParameter("reportName");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        try {
            // 1. VULNERABILITY: SQL Injection (SQLi)
            // The input is concatenated directly into the query string.
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/appdb", "admin", "password123");
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE id = '" + userId + "'"; 
            ResultSet rs = stmt.executeQuery(sql);

            // 2. VULNERABILITY: Cross-Site Scripting (XSS)
            // The 'username' is printed directly to the HTML without encoding.
            out.println("<html><body>");
            out.println("<h1>User Profile for: " + username + "</h1>"); 

            if (rs.next()) {
                out.println("<p>Account Balance: " + rs.getString("balance") + "</p>");
            }

            // 3. VULNERABILITY: Path Traversal
            // The 'reportName' is used to open a file without checking for '../' sequences.
            out.println("<h3>Your Requested Report:</h3>");
            File file = new File("/var/app/data/reports/" + reportName); 
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println("<p>" + line + "</p>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            // POOR PRACTICE: Printing full stack traces to the user (Information Leakage)
            e.printStackTrace(out); 
        }
    }
}
