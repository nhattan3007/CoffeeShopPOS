// ===== model/MenuItem.java =====
package vn.edu.ptit.oop.nhom05.model;

public abstract class MenuItem {
	// ĐÓNG GÓI: tất cả thuộc tính để private, bên ngoài chỉ truy cập qua
	// getter/setter
	private final String code; // final: mã món không đổi sau khi tạo
	private String name;
	private double basePrice;
	private final Category category;

	// protected: chỉ lớp con (Drink, Food, Combo) mới gọi được constructor này
	protected MenuItem(String code, String name, double basePrice, Category category) {
		if (code == null || code.isBlank())
			throw new IllegalArgumentException("Mã món không được rỗng");
		this.code = code.trim();
		this.category = category;
		setName(name); // dùng lại setter để tái sử dụng bước kiểm tra hợp lệ
		setBasePrice(basePrice);
	}

	// final: lớp con không được override, vì constructor đang gọi nó (an toàn)
	public final void setName(String name) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("Tên món không được rỗng");
		this.name = name.trim();
	}

	public final void setBasePrice(double basePrice) {
		if (basePrice < 0)
			throw new IllegalArgumentException("Giá không được âm");
		this.basePrice = basePrice;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public Category getCategory() {
		return category;
	}

	// TRỪU TƯỢNG + ĐA HÌNH: mỗi loại món tự quyết định cách tính giá của mình.
	// Lớp cha chỉ khai báo "phải có", không viết thân hàm.
	public abstract double getPrice();

	// Phần tiền thuộc "đồ uống" trong món này (dùng cho Happy Hour sau này).
	// Mặc định 0 (Food); Drink và Combo sẽ override
	public double getDrinkPrice() {
		return 0;
	}

	// Tên hiển thị trong giỏ hàng; Drink sẽ override để thêm size
	public String getDisplayName() {
		return name;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}