// ===== model/Combo.java =====
package vn.edu.ptit.oop.nhom05.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// MẪU COMPOSITE: Combo LÀ một MenuItem (kế thừa) và CHỨA nhiều MenuItem (children).
// Nhờ vậy giỏ hàng xử lý Combo giống hệt món lẻ.
public class Combo extends MenuItem {
	private final List<MenuItem> children = new ArrayList<>();
	private final double discountRate; // ví dụ 0.1 = giảm 10% khi mua combo

	public Combo(String code, String name, double discountRate) {
		super(code, name, 0, Category.COMBO); // giá combo được TÍNH, không nhập tay
		if (discountRate < 0 || discountRate > 1)
			throw new IllegalArgumentException("Tỷ lệ giảm phải từ 0 đến 1");
		this.discountRate = discountRate;
	}

	public void add(MenuItem item) {
		if (item == null || item == this)
			throw new IllegalArgumentException("Món thêm vào combo không hợp lệ");
		children.add(item);
	}

	// ĐÓNG GÓI: không trả list gốc ra ngoài, nếu không bên ngoài có thể tự
	// add/remove làm hỏng combo
	public List<MenuItem> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public double getPrice() {
		double sum = 0;
		for (MenuItem m : children)
			sum += m.getPrice(); // ĐA HÌNH: m là Drink hay Food, tự biết tính giá
		return sum * (1 - discountRate);
	}

	// Phần đồ uống nằm trong combo (cũng được giảm theo tỷ lệ combo)
	@Override
	public double getDrinkPrice() {
		double sum = 0;
		for (MenuItem m : children)
			sum += m.getDrinkPrice();
		return sum * (1 - discountRate);
	}
}