// ===== model/MemberDiscount.java =====
package vn.edu.ptit.oop.nhom05.model;

public class MemberDiscount implements DiscountStrategy {
	private static final double RATE = 0.10; // thành viên giảm 10% toàn bill

	@Override
	public double apply(Order order) {
		return Math.round(order.getSubtotal() * RATE);
	}

	@Override
	public String getName() {
		return "Thành viên (-10%)";
	}

	@Override
	public String toString() {
		return getName();
	}
}