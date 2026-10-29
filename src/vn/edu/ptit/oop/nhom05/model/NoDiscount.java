// ===== model/NoDiscount.java =====
package vn.edu.ptit.oop.nhom05.model;

public class NoDiscount implements DiscountStrategy {
	@Override
	public double apply(Order order) {
		return 0;
	}

	@Override
	public String getName() {
		return "Không khuyến mãi";
	}

	@Override
	public String toString() {
		return getName();
	} // để JComboBox hiện đúng tên
}