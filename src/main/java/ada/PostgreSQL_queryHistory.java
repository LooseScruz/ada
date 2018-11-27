package ada;

import org.json.JSONObject;

import java.sql.Connection;
import static java.sql.DriverManager.getConnection;

import java.sql.ResultSet;
import java.sql.Statement;


import static java.sql.DriverManager.getConnection;

public class PostgreSQL_queryHistory {
    public static void main(String username) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = getConnection("jdbc:postgresql://localhost:5432",
                    "postgres", "postgres");
            c.setAutoCommit(false);

            System.out.println(username);

            stmt = c.createStatement();
            String sql = "SELECT * FROM adaChat";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString(4));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
}