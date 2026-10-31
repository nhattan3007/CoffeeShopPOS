package vn.edu.ptit.oop.nhom05.demo;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class RevenueByCategoryChartDemo {
	 public static void main(String[] args) {

	        // Khởi chạy giao diện trên Event Dispatch Thread của Swing
	        SwingUtilities.invokeLater(() -> {

	            // 1. Tạo danh sách hóa đơn mẫu
	            List<Order> orders = SampleData.createOrders();

	            // 2. Tạo biểu đồ doanh thu theo danh mục
	            JFreeChart chart =
	                    ChartHelper.createRevenueByCategoryChart(orders);

	            // 3. Tạo khung hiển thị biểu đồ
	            JFrame frame = new JFrame("Revenue By Category");

	            // 4. Đưa biểu đồ vào ChartPanel
	            ChartPanel chartPanel = new ChartPanel(chart);

	            // 5. Cấu hình cửa sổ
	            frame.setContentPane(chartPanel);
	            frame.setSize(900, 600);
	            frame.setLocationRelativeTo(null);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	            // 6. Hiển thị cửa sổ
	            frame.setVisible(true);
	        });
	    }
}
