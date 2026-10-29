// ===== model/PercentDiscount.java =====
package vn.edu.ptit.oop.nhom05.model;

public class PercentDiscount implements DiscountStrategy {
	private final double percent; // 0..100

	public PercentDiscount(double percent) {
		if (percent < 0 || percent > 100)
			throw new IllegalArgumentException("Phần trăm giảm phải từ 0 đến 100");
		this.percent = percent;
	}

	// Math.round: làm tròn về đồng, tránh số lẻ kiểu 12720.000000000002
	@Override
	public double apply(Order order) {
		return Math.round(order.getSubtotal() * percent / 100.0);
	}

	@Override
	public String getName() {
		return "Giảm " + (int) percent + "%";
	}

	@Override
	public String toString() {
		return getName();
	}
}