package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DBConnection;

public class HighScoreDAO {
    public static void saveGameHistory(String playerName, int score) {
        String sql = "INSERT INTO highscore (player_name, score, play_date) VALUES (?, ?, NOW())";
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, playerName);
                    pstmt.setInt(2, score);
                    
                    pstmt.executeUpdate();
                    System.out.println("Đã tự động lưu lịch sử cho " + playerName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lưu Database: " + e.getMessage());
        }
    }
}