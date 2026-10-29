// ===== model/HappyHourDiscount.java =====
package vn.edu.ptit.oop.nhom05.model;

import java.time.LocalTime;

public class HappyHourDiscount implements DiscountStrategy {
	private final LocalTime start = LocalTime.of(14, 0);
	private final LocalTime end = LocalTime.of(16, 0);
	private final double rate = 0.20; // giảm 20% phần đồ uống
	private LocalTime simulatedTime; // null = dùng giờ tạo đơn thật

	/**
	 * Dùng khi demo: ép giờ giả lập (vd 14:30). Truyền null để quay về giờ thật.
	 */
	public void setSimulatedTime(LocalTime t) {
		this.simulatedTime = t;
	}

	@Override
	public double apply(Order order) {
		LocalTime t = (simulatedTime != null) ? simulatedTime : order.getCreatedAt().toLocalTime();
		boolean inHappyHour = !t.isBefore(start) && t.isBefore(end); // [14:00, 16:00)
		// Giảm trên phần ĐỒ UỐNG, lấy qua getDrinkPrice() => không cần instanceof
		return inHappyHour ? Math.round(order.getDrinkSubtotal() * rate) : 0;
	}

	@Override
	public String getName() {
		return "Happy Hour (14h–16h, -20% đồ uống)";
	}

	@Override
	public String toString() {
		return getName();
	}
}