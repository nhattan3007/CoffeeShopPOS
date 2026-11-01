package vn.edu.ptit.oop.nhom05.demo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.ui.ChartWindow;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class ChartWindowDemo {
	public static void main(String[] args) {
		ChartHelper.applyVietnameseFont(); // gọi MỘT lần trước khi tạo biểu đồ
		SwingUtilities.invokeLater(() -> {
			// Cửa sổ chính giả (EXIT_ON_CLOSE) để thử xem đóng biểu đồ có tắt cả chương
			// trình không
			JFrame main = new JFrame("Cửa sổ chính (thử)");
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JButton open = new JButton("Mở biểu đồ");
			open.addActionListener(e -> new ChartWindow(SampleData.createOrders()).setVisible(true));
			main.add(open);
			main.setSize(300, 120);
			main.setVisible(true);
		});
	}
}