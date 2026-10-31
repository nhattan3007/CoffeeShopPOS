package vn.edu.ptit.oop.nhom05.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import vn.edu.ptit.oop.nhom05.model.Category;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderItem;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class RevenueByCategoryChartDemo {
	 public static void main(String[] args) {
		 ChartHelper.applyVietnameseFont();// Thiết lập font trước khi tạo biểu đồ
	        List<Order> orders = SampleData.createOrders();// Tạo dữ liệu mẫu
	        double drinkTotal = 0;
            double foodTotal = 0;
            double comboTotal = 0;
	        for (Order order : orders) {

	            // In thông tin hóa đơn (chỉ in một lần)
	            String orderId = order.getId();
	            LocalDateTime orderDate = order.getCreatedAt();

	            System.out.println(
	                orderId + " | " +
	                orderDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
	            );
	            
		         // Duyệt danh sách sản phẩm trong hóa đơn
	            for (OrderItem it : order.getItems()) {

	                // Lấy thông tin loại sản phẩm
	                Category itemCategory = it.getMenuItem().getCategory();

	                // Tính thành tiền tùy theo loại sản phẩm
	                double totalValue;
	                switch (itemCategory) {

	                case DRINK:
	                    drinkTotal += it.getLineDrinkTotal();
	                    break;

	                case FOOD:
	                    foodTotal += it.getLineTotal();
	                    break;

	                case COMBO:
	                    comboTotal += it.getLineTotal();
	                    break;
	            }

	                if (itemCategory == Category.DRINK) {

	                    // Nếu là đồ uống: lấy giá đồ uống nhân số lượng
	                    totalValue = it.getLineDrinkTotal();

	                } else {

	                    // Các loại sản phẩm khác: lấy giá thông thường nhân số lượng
	                    totalValue = it.getLineTotal();
	                }

	                // In thông tin từng sản phẩm
	                System.out.println(
	                    "  Loại sản phẩm: " + itemCategory +
	                    " | Thành tiền: " + String.format("%,.0f VNĐ", totalValue)
	                );
	            }
	            
	            
	            System.out.println("--------------------------------");
	       
			}
	        System.out.println("  Drink: " + String.format("%,.0f VNĐ", drinkTotal));
	        System.out.println("  Food: " + String.format("%,.0f VNĐ", foodTotal));
	        System.out.println("  Combo: " + String.format("%,.0f VNĐ", comboTotal));
	        // Khởi chạy giao diện trên Event Dispatch Thread của Swing
	        SwingUtilities.invokeLater(() -> {

	            // 2. Tạo biểu đồ doanh thu theo danh mục
	            JFreeChart chart =
	                    ChartHelper.createRevenueByCategoryChart(orders);

	            // 3. Tạo khung hiển thị biểu đồ
	            JFrame frame = new JFrame("CoffeeShopPOS - Doanh thu theo loài thực phẩm");

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
