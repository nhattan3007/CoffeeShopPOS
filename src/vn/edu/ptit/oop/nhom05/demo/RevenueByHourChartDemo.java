package vn.edu.ptit.oop.nhom05.demo;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class RevenueByHourChartDemo {
	public static void main(String[] args) {

	    // Khởi chạy giao diện trên Event Dispatch Thread của Swing
	    SwingUtilities.invokeLater(() -> {

	        // 1. Lấy danh sách hóa đơn từ dữ liệu mẫu
	        List<Order> orders = SampleData.createOrders();

	        // 2. Tạo biểu đồ doanh thu theo giờ
	        JFreeChart chart =
	                ChartHelper.createRevenueByHourChart(orders);

	        // 3. Tạo khung hiển thị biểu đồ
	        JFrame frame = new JFrame("Revenue By Hour");

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
