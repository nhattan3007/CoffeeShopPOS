// ===== model/Drink.java =====
package vn.edu.ptit.oop.nhom05.model;

public class Drink extends MenuItem {

	// ENUM CÓ HÀNH VI: mỗi size mang theo hệ số giá của nó.
	// Không cần viết if (size == L) ... ở bất cứ chỗ nào khác.
	public enum Size {
		S(1.0), M(1.2), L(1.4);

		private final double factor;

		Size(double factor) {
			this.factor = factor;
		}

		public double getFactor() {
			return factor;
		}
	}

	private Size size;

	public Drink(String code, String name, double basePrice, Size size) {
		super(code, name, basePrice, Category.DRINK);
		setSize(size); // dùng lại setter để kiểm tra size
	}

	public final void setSize(Size size) {
		if (size == null)
			throw new IllegalArgumentException("Phải chọn size");
		this.size = size;
	}

	public Size getSize() {
		return size;
	}

	// ĐA HÌNH: giá của Drink = giá gốc × hệ số size
	@Override
	public double getPrice() {
		return getBasePrice() * size.getFactor();
	}

	// Cả ly nước đều là "phần đồ uống" -> Happy Hour sẽ giảm trên số này
	@Override
	public double getDrinkPrice() {
		return getPrice();
	}

	// Tên hiển thị có kèm size để "Cà phê sữa (L)" và "Cà phê sữa (S)" là 2 dòng
	// khác nhau trong giỏ
	@Override
	public String getDisplayName() {
		return getName() + " (" + size + ")";
	}
}