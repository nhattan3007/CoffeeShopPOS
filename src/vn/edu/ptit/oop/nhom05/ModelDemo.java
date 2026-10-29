package vn.edu.ptit.oop.nhom05;

import vn.edu.ptit.oop.nhom05.model.Combo;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.Food;
import vn.edu.ptit.oop.nhom05.model.MenuItem;

public class ModelDemo {
	public static void main(String[] args) {
		// ----- Bước 1: Food + kiểm tra dữ liệu sai -----
		MenuItem banhMi = new Food("F01", "Bánh mì thịt", 25000);
		System.out
				.println(banhMi.getDisplayName() + " | " + banhMi.getCategory().getLabel() + " | " + banhMi.getPrice());
		try {
			new Food("F02", "   ", 1000);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage());
		}

		// ----- Bước 2: Drink với 3 size -----
		MenuItem cfS = new Drink("D02", "Cà phê sữa", 30000, Drink.Size.S);
		MenuItem cfM = new Drink("D02", "Cà phê sữa", 30000, Drink.Size.M);
		MenuItem cfL = new Drink("D02", "Cà phê sữa", 30000, Drink.Size.L);
		System.out.println(cfS.getDisplayName() + " = " + cfS.getPrice()); // 30000.0
		System.out.println(cfM.getDisplayName() + " = " + cfM.getPrice()); // 36000.0
		System.out.println(cfL.getDisplayName() + " = " + cfL.getPrice()); // 42000.0
		System.out.println("Phần đồ uống của Food = " + banhMi.getDrinkPrice()); // 0.0

		// ----- Bước 2: Combo (Composite) -----
		Combo comboSang = new Combo("C01", "Combo sáng", 0.10);
		comboSang.add(new Food("F01", "Bánh mì thịt", 25000)); // 25000
		comboSang.add(new Drink("D01", "Cà phê đen", 20000, Drink.Size.M)); // 24000
		System.out.println(comboSang.getDisplayName() + " = " + comboSang.getPrice()); // (25000+24000)*0.9 = 44100.0
		System.out.println("Phần đồ uống trong combo = " + comboSang.getDrinkPrice()); // 24000*0.9 = 21600.0

		// ----- ĐA HÌNH: một danh sách kiểu cha, mỗi phần tử tự tính giá theo kiểu thật
		// -----
		MenuItem[] menu = { banhMi, cfL, comboSang };
		double tong = 0;
		for (MenuItem m : menu) {
			tong += m.getPrice(); // không cần if/instanceof
		}
		System.out.println("Tổng 3 món = " + tong); // 25000 + 42000 + 44100 = 111100.0

		// ----- Kiểm tra dữ liệu sai -----
		try {
			new Drink("D09", "Trà", 20000, null);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage()); // Phải chọn size
		}
		try {
			comboSang.add(null);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage()); // Món thêm vào combo không hợp lệ
		}
		try {
			new Combo("C09", "Combo sai", 1.5);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage()); // Tỷ lệ giảm phải từ 0 đến 1
		}
		try {
			comboSang.getChildren().add(banhMi); // thử "lén" thêm vào list gốc
		} catch (UnsupportedOperationException e) {
			System.out.println("Bắt được lỗi: không được sửa danh sách món của combo từ bên ngoài");
		}
	}
}