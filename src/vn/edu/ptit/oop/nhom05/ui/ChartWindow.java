package vn.edu.ptit.oop.nhom05.ui;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.util.ChartHelper;

// Cửa sổ biểu đồ: KẾ THỪA JFrame, gồm 3 tab, mỗi tab một biểu đồ JFreeChart
public class ChartWindow extends JFrame {

	public ChartWindow(List<Order> orders) {
		super("Biểu đồ thống kê");
		// DISPOSE_ON_CLOSE: đóng cửa sổ này chỉ đóng riêng nó.
		// Nếu dùng EXIT_ON_CLOSE thì đóng biểu đồ là tắt luôn cả chương trình.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Top 5 món bán chạy", new ChartPanel(ChartHelper.createTopItemsChart(orders)));
		tabs.addTab("Doanh thu theo giờ", new ChartPanel(ChartHelper.createRevenueByHourChart(orders)));
		tabs.addTab("Doanh thu theo danh mục", new ChartPanel(ChartHelper.createRevenueByCategoryChart(orders)));
		add(tabs);
	}
}