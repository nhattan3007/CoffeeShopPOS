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

public class ChartDemo {
	public static void main(String[] args) {
        ChartHelper.applyVietnameseFont();// Thiết lập font trước khi tạo biểu đồ
        List<Order> orders = SampleData.createOrders();// Tạo dữ liệu mẫu
        for (Order order : orders) {
        	for (OrderItem it : order.getItems()) {
		    // Gọi các phương thức lấy dữ liệu (getter) từ đối tượng Order và OrderItem
		    String orderId = order.getId();
		    String itemName = it.getMenuItem().getName();
		    LocalDateTime orderDate = order.getCreatedAt(); 
		    int quantity = it.getQuantity();
	        
		    System.out.println(// In số lượng đơn hàng để kiểm tra
		            orderId + " | " +
		            itemName + " | " +
		            quantity + " | " +
		            orderDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
		            )
		        );
        	}

		}

        // Hiển thị biểu đồ
        SwingUtilities.invokeLater(() -> {
            JFreeChart chart =
                    ChartHelper.createTopItemsChart(orders);

            ChartPanel panel = new ChartPanel(chart);
            JFrame frame = new JFrame("CoffeeShopPOS - Top 5 món bán chạy");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(panel);

            frame.setVisible(true);
        });
    }
}
