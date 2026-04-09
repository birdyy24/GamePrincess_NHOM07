package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            // Nạp Driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            

            String url = "jdbc:mysql://localhost:3307/gameprincess";
            String user = "root";
            String password = ""; 
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối Database thành công rồi !");
            return conn;
        } catch (Exception e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }
}