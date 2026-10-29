// ===== exception/EmptyOrderException.java =====
package vn.edu.ptit.oop.nhom05.exception;

// Exception TỰ ĐỊNH NGHĨA (yêu cầu bắt buộc của đề).
// Kế thừa Exception (không phải RuntimeException) => CHECKED: ai gọi pay() bị ép phải xử lý
public class EmptyOrderException extends Exception {
	public EmptyOrderException(String msg) {
		super(msg);
	}
}