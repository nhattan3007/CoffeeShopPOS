// ===== demo/TestCaseRunner.java =====
package vn.edu.ptit.oop.nhom05.demo;

import java.time.LocalDate;
import java.time.LocalTime;

import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;
import vn.edu.ptit.oop.nhom05.model.Category;
import vn.edu.ptit.oop.nhom05.model.Combo;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.Food;
import vn.edu.ptit.oop.nhom05.model.HappyHourDiscount;
import vn.edu.ptit.oop.nhom05.model.MenuData;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.PercentDiscount;
import vn.edu.ptit.oop.nhom05.repository.SalesRepository;

// Chạy tự động các test case của phần model và repository, in bảng kết quả để đưa vào báo cáo Chương 5.
// Các ca GUI (nhập chữ, đóng cửa sổ...) phải thử tay, xem bảng test case.
public class TestCaseRunner {
	private static int passed = 0;
	private static int failed = 0;

	private static void check(String id, String description, String expected, String actual) {
		boolean ok = expected.equals(actual);
		if (ok)
			passed++;
		else
			failed++;
		System.out.println(id + " | " + description);
		System.out.println("     Mong đợi : " + expected);
		System.out.println("     Thực tế  : " + actual);
		System.out.println("     Kết quả  : " + (ok ? "ĐẠT" : "KHÔNG ĐẠT"));
	}

	private static String money(double x) {
		return String.valueOf(Math.round(x));
	}

	// Đơn mẫu của kịch bản đề: Cà phê sữa L (25.000 gốc) + Combo sáng, giờ giả lập
	// t
	private static Order sampleOrder(HappyHourDiscount hh, LocalTime t) {
		Order o = new Order("HD-T", 1);
		o.addItem(new Drink("D02", "Cà phê sữa", 25000, Drink.Size.L), 1, "");
		o.addItem(MenuData.getByCategory(Category.COMBO).get(0), 1, "");
		hh.setSimulatedTime(t);
		o.setDiscount(hh);
		return o;
	}

	public static void main(String[] args) throws Exception {
		HappyHourDiscount hh = new HappyHourDiscount();

		check("TC01", "Giá ly theo size: Cà phê sữa (gốc 25.000) size L", "35000",
				money(new Drink("D02", "Cà phê sữa", 25000, Drink.Size.L).getPrice()));

		Order o2 = new Order("T2", 1);
		o2.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.L), 1, "");
		o2.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.L), 2, "");
		check("TC02", "Cộng dồn món cùng size: Cà phê đen L x1 rồi L x2", "1 dòng, SL 3",
				o2.getItems().size() + " dòng, SL " + o2.getItems().get(0).getQuantity());

		Order o3 = new Order("T3", 1);
		o3.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.L), 1, "");
		o3.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.S), 1, "");
		check("TC03", "Khác size tách dòng: Cà phê đen L rồi S", "2 dòng", o3.getItems().size() + " dòng");

		check("TC04", "Giá Combo sáng (Bánh mì thịt + Cà phê sữa M, giảm 10%)", "49500",
				money(MenuData.getByCategory(Category.COMBO).get(0).getPrice()));

		Order o5 = sampleOrder(hh, LocalTime.of(14, 30));
		check("TC05", "Happy Hour trong khung (14:30)", "giảm 12400, phải trả 72100",
				"giảm " + money(o5.getDiscountAmount()) + ", phải trả " + money(o5.getTotal()));

		check("TC06", "Happy Hour biên đầu (14:00)", "giảm 12400",
				"giảm " + money(sampleOrder(hh, LocalTime.of(14, 0)).getDiscountAmount()));

		check("TC07", "Happy Hour biên cuối (16:00, không tính)", "giảm 0",
				"giảm " + money(sampleOrder(hh, LocalTime.of(16, 0)).getDiscountAmount()));

		Order o8 = sampleOrder(hh, LocalTime.of(14, 30));
		o8.setDiscount(new PercentDiscount(15));
		check("TC08", "Giảm 15%", "giảm 12675, phải trả 71825",
				"giảm " + money(o8.getDiscountAmount()) + ", phải trả " + money(o8.getTotal()));

		Order o9 = sampleOrder(hh, LocalTime.of(14, 30));
		String r9;
		try {
			o9.pay(50000);
			r9 = "không có lỗi";
		} catch (InvalidPaymentException e) {
			r9 = "thiếu " + money(e.getShortage()) + ", trạng thái " + o9.getStatus();
		}
		check("TC09", "Trả thiếu tiền: phải trả 72.100, khách đưa 50.000", "thiếu 22100, trạng thái OPEN", r9);

		Order o10 = sampleOrder(hh, LocalTime.of(14, 30));
		double change = o10.pay(100000);
		check("TC10", "Trả đủ: khách đưa 100.000", "tiền thối 27900, trạng thái PAID",
				"tiền thối " + money(change) + ", trạng thái " + o10.getStatus());

		String r11;
		try {
			new Order("T11", 1).pay(100000);
			r11 = "không có lỗi";
		} catch (EmptyOrderException e) {
			r11 = e.getMessage();
		}
		check("TC11", "Thanh toán giỏ rỗng", "Giỏ hàng đang trống", r11);

		String r12;
		try {
			o10.addItem(new Food("F01", "Bánh mì", 25000), 1, "");
			r12 = "không có lỗi";
		} catch (IllegalStateException e) {
			r12 = e.getMessage();
		}
		check("TC12", "Thêm món vào đơn đã thanh toán", "Đơn hàng đã đóng, không thể thay đổi", r12);

		String r13;
		try {
			new Food("F9", "Bánh", -5);
			r13 = "không có lỗi";
		} catch (IllegalArgumentException e) {
			r13 = e.getMessage();
		}
		check("TC13", "Giá âm: new Food(\"F9\", \"Bánh\", -5)", "Giá không được âm", r13);

		// ----- Các ca lỗi / biên bổ sung -----
		String r15;
		try {
			new Order("T15", 1).addItem(new Food("F01", "Bánh mì", 25000), 0, "");
			r15 = "không có lỗi";
		} catch (IllegalArgumentException e) {
			r15 = e.getMessage();
		}
		check("TC15", "Số lượng 0 khi thêm món", "Số lượng phải ≥ 1", r15);

		String r16;
		try {
			new SalesRepository().add(new Order("T16", 1));
			r16 = "không có lỗi";
		} catch (IllegalArgumentException e) {
			r16 = "bị từ chối (IllegalArgumentException)";
		}
		check("TC16", "Thêm đơn CHƯA thanh toán vào SalesRepository", "bị từ chối (IllegalArgumentException)", r16);

		SalesRepository repo = new SalesRepository();
		Order paid = sampleOrder(hh, LocalTime.of(14, 30));
		paid.pay(100000);
		repo.add(paid);
		check("TC17", "findByDate với ngày không có đơn (01/01/2000)", "0 đơn",
				repo.findByDate(LocalDate.of(2000, 1, 1)).size() + " đơn");

		Order o18 = sampleOrder(hh, LocalTime.of(14, 30));
		check("TC18", "Biên: khách đưa đúng bằng số phải trả (72.100)", "tiền thối 0, trạng thái PAID",
				"tiền thối " + money(o18.pay(72100)) + ", trạng thái " + o18.getStatus());

		Order o19 = sampleOrder(hh, LocalTime.of(14, 30));
		o19.setDiscount(new PercentDiscount(100));
		check("TC19", "Giảm 100% không làm tổng tiền âm", "phải trả 0", "phải trả " + money(o19.getTotal()));

		Order o20 = sampleOrder(hh, LocalTime.of(14, 30));
		o20.pay(100000);
		hh.setSimulatedTime(LocalTime.of(10, 0)); // đổi giờ giả lập SAU khi đã thanh toán
		check("TC20", "Đơn đã trả không đổi giá khi đổi giờ giả lập Happy Hour", "phải trả 72100",
				"phải trả " + money(o20.getTotal()));

		String r21;
		try {
			((Combo) MenuData.getByCategory(Category.COMBO).get(0)).getChildren().add(new Food("F9", "Bánh", 1000));
			r21 = "không có lỗi";
		} catch (UnsupportedOperationException e) {
			r21 = "bị chặn (UnsupportedOperationException)";
		}
		check("TC21", "Sửa danh sách món bên trong Combo từ bên ngoài", "bị chặn (UnsupportedOperationException)", r21);

		String r22;
		try {
			new PercentDiscount(101);
			r22 = "không có lỗi";
		} catch (IllegalArgumentException e) {
			r22 = e.getMessage();
		}
		check("TC22", "Khuyến mãi 101%", "Phần trăm giảm phải từ 0 đến 100", r22);

		System.out.println();
		System.out.println("TỔNG KẾT: " + passed + " đạt, " + failed + " không đạt, trên " + (passed + failed)
				+ " ca chạy tự động");
	}
}