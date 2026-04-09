import view.GameFrame;
import utils.DBConnection; // Thêm dòng này
import java.sql.Connection; // Thêm dòng này

public class Main {
    public static void main(String[] args) {
        // Thêm đoạn này để kiểm tra kết nối khi vừa bật game
        Connection testConn = DBConnection.getConnection();
        if (testConn != null) {
            System.out.println("Sẵn sàng lưu điểm rồi ");
        } else {
            System.out.println("Chưa bật XAMPP hoặc lỗi rồi");
        }

        new GameFrame();
    }
}
