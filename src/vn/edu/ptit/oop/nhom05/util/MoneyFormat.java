package vn.edu.ptit.oop.nhom05.util;

import java.text.NumberFormat;
import java.util.Locale;

// Lớp tiện ích: định dạng tiền kiểu Việt Nam, dùng chung cho cả bảng, hóa đơn, báo cáo
public final class MoneyFormat {
	private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));

	private MoneyFormat() {
	} // private: không cho tạo đối tượng, chỉ gọi hàm static

	public static String format(double amount) {
		return FORMAT.format(Math.round(amount)) + " đ"; // 84500 -> "84.500 đ"
	}
}