package vn.edu.ptit.oop.nhom05;

import vn.edu.ptit.oop.nhom05.model.Food;
import vn.edu.ptit.oop.nhom05.model.MenuItem;

public class ModelDemo {
	public static void main(String[] args) {
		// Biến kiểu cha (MenuItem) giữ đối tượng con (Food): đây là đa hình
		MenuItem banhMi = new Food("F01", "Bánh mì thịt", 25000);
		System.out
				.println(banhMi.getDisplayName() + " | " + banhMi.getCategory().getLabel() + " | " + banhMi.getPrice());

		// Thử dữ liệu sai: phải bị chặn
		try {
			new Food("F02", "   ", 1000);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage());
		}
		try {
			new Food("F03", "Bánh", -5);
		} catch (IllegalArgumentException e) {
			System.out.println("Bắt được lỗi: " + e.getMessage());
		}
	}
}