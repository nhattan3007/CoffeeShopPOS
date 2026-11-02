package vn.edu.ptit.oop.nhom05;

import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.ui.MainWindow;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;

public class Main {
	public static void main(String[] args) {
		ChartHelper.applyVietnameseFont(); // đặt font hỗ trợ tiếng Việt cho biểu đồ, gọi một lần
		// Mọi thao tác Swing phải chạy trên Event Dispatch Thread (EDT)
		SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
	}
}