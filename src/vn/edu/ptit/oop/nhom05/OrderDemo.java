package vn.edu.ptit.oop.nhom05;

import java.time.LocalTime;

import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;
import vn.edu.ptit.oop.nhom05.model.Category;
import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.HappyHourDiscount;
import vn.edu.ptit.oop.nhom05.model.MemberDiscount;
import vn.edu.ptit.oop.nhom05.model.MenuData;
import vn.edu.ptit.oop.nhom05.model.MenuItem;
import vn.edu.ptit.oop.nhom05.model.NoDiscount;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.PercentDiscount;

public class OrderDemo {
	public static void main(String[] args) throws Exception {
		// ----- Menu -----
		System.out.println("Số món trong menu: " + MenuData.getAll().size());
		System.out.println("Số đồ uống: " + MenuData.getByCategory(Category.DRINK).size());
		MenuItem comboSang = MenuData.getByCategory(Category.COMBO).get(0);
		System.out.println(comboSang.getDisplayName() + " = " + Math.round(comboSang.getPrice()));

		// ----- KỊCH BẢN ĐỀ: 1 đồ uống size L + 1 Combo + Happy Hour -----
		Order order = new Order("HD001", 3);
		order.addItem(new Drink("D02", "Cà phê sữa", 25000, Drink.Size.L), 1, "");
		order.addItem(comboSang, 1, "");
		HappyHourDiscount hh = new HappyHourDiscount();
		hh.setSimulatedTime(LocalTime.of(14, 30));
		order.setDiscount(hh);
		System.out.println("Tạm tính: " + Math.round(order.getSubtotal()));
		System.out.println("Giảm giá: " + Math.round(order.getDiscountAmount()));
		System.out.println("Phải trả: " + Math.round(order.getTotal()));

		// ----- ĐA HÌNH Strategy: đổi cách giảm giá, Order không đổi -----
		DiscountStrategy[] list = { new NoDiscount(), new PercentDiscount(15), new MemberDiscount(), hh };
		for (DiscountStrategy d : list) {
			order.setDiscount(d);
			System.out.println(d.getName() + ": giảm " + Math.round(order.getDiscountAmount()) + " -> phải trả "
					+ Math.round(order.getTotal()));
		}

		// ----- Biên khung giờ Happy Hour: [14:00, 16:00) -----
		LocalTime[] times = { LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(15, 59), LocalTime.of(16, 0) };
		for (LocalTime t : times) {
			hh.setSimulatedTime(t);
			order.setDiscount(hh);
			System.out.println(t + " -> giảm " + Math.round(order.getDiscountAmount()));
		}
		hh.setSimulatedTime(LocalTime.of(14, 30));
		order.setDiscount(hh);

		// ----- Các trường hợp LỖI -----
		try {
			new Order("HD002", 1).pay(100000);
		} catch (EmptyOrderException e) {
			System.out.println("Lỗi: " + e.getMessage());
		}
		try {
			order.pay(50000);
		} catch (InvalidPaymentException e) {
			System.out.println("Lỗi: " + e.getMessage() + ", thiếu " + Math.round(e.getShortage()));
		}

		// ----- Thanh toán thành công -----
		double change = order.pay(100000);
		System.out.println("Tiền thối: " + Math.round(change) + " | trạng thái: " + order.getStatus());

		// ----- Sau khi trả tiền thì đơn bị khóa -----
		try {
			order.addItem(comboSang, 1, "");
		} catch (IllegalStateException e) {
			System.out.println("Thêm món sau khi trả tiền: " + e.getMessage());
		}
		try {
			order.pay(100000);
		} catch (IllegalStateException e) {
			System.out.println("Trả tiền lần 2: " + e.getMessage());
		}

		// ----- Cộng dồn món cùng size, tách món khác size -----
		Order o2 = new Order("HD003", 5);
		o2.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.L), 1, "");
		o2.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.L), 2, "");
		o2.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.S), 1, "");
		System.out.println("Số dòng: " + o2.getItems().size() + ", SL dòng 1: " + o2.getItems().get(0).getQuantity());

		// ----- Đóng gói: không sửa được list bên trong -----
		try {
			o2.getItems().add(null);
		} catch (UnsupportedOperationException e) {
			System.out.println("Lỗi: không được sửa danh sách món từ bên ngoài");
		}
	}
}