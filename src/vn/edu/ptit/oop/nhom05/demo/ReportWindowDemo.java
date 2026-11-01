// ===== demo/ReportWindowDemo.java =====
package vn.edu.ptit.oop.nhom05.demo;

import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.repository.SalesRepository;
import vn.edu.ptit.oop.nhom05.ui.ReportWindow;
import vn.edu.ptit.oop.nhom05.util.SampleData;

public class ReportWindowDemo {
	public static void main(String[] args) {
		SalesRepository repo = new SalesRepository();
		for (Order o : SampleData.createOrders()) {
			repo.add(o);
		}
		SwingUtilities.invokeLater(() -> new ReportWindow(repo).setVisible(true));
	}
}