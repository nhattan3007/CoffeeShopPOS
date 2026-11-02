package vn.edu.ptit.oop.nhom05.demo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.repository.SalesRepository;
import vn.edu.ptit.oop.nhom05.ui.ReportWindow;
import vn.edu.ptit.oop.nhom05.util.SampleData;

// Thử TC24: đóng ReportWindow thì cửa sổ chính phải còn
public class ReportCloseDemo {
	public static void main(String[] args) {
		SalesRepository repo = new SalesRepository();
		for (Order o : SampleData.createOrders()) {
			repo.add(o);
		}
		SwingUtilities.invokeLater(() -> {
			// Cửa sổ chính giả: EXIT_ON_CLOSE, giống MainWindow thật (đóng nó là tắt chương
			// trình)
			JFrame main = new JFrame("Cửa sổ chính (thử)");
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JButton open = new JButton("Mở báo cáo");
			open.addActionListener(e -> new ReportWindow(repo).setVisible(true));
			main.add(open);
			main.setSize(300, 120);
			main.setVisible(true);
		});
	}
}