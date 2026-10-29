// ===== model/DiscountStrategy.java =====
package vn.edu.ptit.oop.nhom05.model;

// MẪU STRATEGY + interface tự định nghĩa (đề bắt buộc).
// Mỗi cách giảm giá là một lớp riêng, Order chỉ biết gọi apply() qua kiểu interface.
public interface DiscountStrategy {
	/** Trả về SỐ TIỀN ĐƯỢC GIẢM (không phải tổng sau giảm). */
	double apply(Order order);

	/** Tên hiển thị trong JComboBox của CheckoutDialog. */
	String getName();
}