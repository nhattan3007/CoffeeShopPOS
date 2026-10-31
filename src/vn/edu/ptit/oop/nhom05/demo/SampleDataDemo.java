package vn.edu.ptit.oop.nhom05.demo;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.ptit.oop.nhom05.util.*;
import vn.edu.ptit.oop.nhom05.model.Order;

public class SampleDataDemo {
	private static int count = 0 ;
	public static void main(String[] args) {
		List<Order> orders = SampleData.createOrders();// goi hàm tạo ngẫu nhiên orders
		double totalRevenue = 0; // Biến cộng dồn tổng doanh thu tất cả các đơn

		for (Order order : orders) {
		    // Gọi các phương thức lấy dữ liệu (getter) từ đối tượng Order
		    String orderId = order.getId();
		    LocalDateTime orderDate = order.getCreatedAt(); 
		    double orderTotal = order.getTotal();
		    
		    totalRevenue += orderTotal;// Cộng dồn tiền của đơn hiện tại vào tổng doanh thu

		    
		    System.out.println("Số đơn: " + orderId + " | Ngày giờ: " + orderDate + 
		    		" | Tổng tiền đơn: " + String.format("%,.0f VNĐ", orderTotal));// In thông tin chi tiết của từng đơn hàng
		    count +=1;
		}

		System.out.println("---------------------------------------------------------------");
		System.out.println("TỔNG DOANH " + count +" THU  ĐƠN HÀNG: " + String.format("%,.0f VNĐ", totalRevenue));
	}
	
}
