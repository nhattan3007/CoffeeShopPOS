package vn.edu.ptit.oop.nhom05.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderItem;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class RevenueByHourChartDemo {
	public static void main(String[] args) {
		ChartHelper.applyVietnameseFont();// Thiết lập font trước khi tạo biểu đồ
        List<Order> orders = SampleData.createOrders();// Tạo dữ liệu mẫu
        for (Order order : orders) {

            // Lấy thông tin hóa đơn
            String orderId = order.getId();
            LocalDateTime orderDate = order.getCreatedAt();

            // In mỗi hóa đơn đúng một lần
            System.out.println(
                orderId + " | " +
                orderDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")) +
                " | Tổng tiền: " + order.getTotal()
            );
		}
	    // Khởi chạy giao diện trên Event Dispatch Thread của Swing
	    SwingUtilities.invokeLater(() -> {

	        // 2. Tạo biểu đồ doanh thu theo giờ
	        JFreeChart chart =
	                ChartHelper.createRevenueByHourChart(orders);

	        // 3. Tạo khung hiển thị biểu đồ
	        JFrame frame = new JFrame("CoffeeShopPOS - Doanh thu theo giờ");

	        // 4. Đưa biểu đồ vào ChartPanel để hiển thị
	        ChartPanel chartPanel = new ChartPanel(chart);

	        // 5. Cấu hình cửa sổ
	        frame.setContentPane(chartPanel);               // Đưa biểu đồ vào cửa sổ
	        frame.setSize(900, 600);                         // Thiết lập kích thước cửa sổ
	        frame.setLocationRelativeTo(null);              // Căn giữa màn hình
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng chương trình khi thoát

	        // 6. Hiển thị cửa sổ
	        frame.setVisible(true);
	    });
	}
}
