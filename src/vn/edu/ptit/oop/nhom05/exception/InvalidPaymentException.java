// ===== exception/InvalidPaymentException.java =====
package vn.edu.ptit.oop.nhom05.exception;

public class InvalidPaymentException extends Exception {
	private final double shortage; // còn thiếu bao nhiêu tiền

	public InvalidPaymentException(String msg, double shortage) {
		super(msg);
		this.shortage = shortage;
	}

	public double getShortage() {
		return shortage;
	}
}