// ===== model/Food.java =====
package vn.edu.ptit.oop.nhom05.model;

public class Food extends MenuItem { // KẾ THỪA: Food là một MenuItem
	public Food(String code, String name, double basePrice) {
		super(code, name, basePrice, Category.FOOD); // gọi constructor lớp cha
	}

	@Override // ĐA HÌNH: Food tính giá theo cách của Food
	public double getPrice() {
		return getBasePrice();
	}
}