// ===== ui/ReportWindow.java =====
package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.repository.SalesRepository;
import vn.edu.ptit.oop.nhom05.ui.panels.OrderTableModel;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

// Cửa sổ báo cáo: chọn ngày -> xem các đơn đã thanh toán của ngày đó + tổng doanh thu
public class ReportWindow extends JFrame {
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final SalesRepository repo;
	private final List<LocalDate> days = new ArrayList<>(); // các ngày có đơn, ngày mới nhất ở đầu
	private final JComboBox<String> dayCombo = new JComboBox<>();
	private final OrderTableModel tableModel = new OrderTableModel();
	private final JLabel summaryLabel = new JLabel();

	public ReportWindow(SalesRepository repo) {
		super("Báo cáo bán hàng");
		this.repo = repo;
		// DISPOSE_ON_CLOSE: đóng báo cáo không được tắt cả chương trình
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);

		collectDays();
		for (LocalDate d : days) {
			dayCombo.addItem(d.format(DATE_FMT));
		}
		// Gắn listener SAU KHI addItem: gắn trước thì việc thêm mục đầu tiên cũng bắn
		// sự kiện
		dayCombo.addActionListener(e -> showSelectedDay());

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.add(new JLabel("Chọn ngày:"));
		top.add(dayCombo);

		summaryLabel.setFont(summaryLabel.getFont().deriveFont(Font.BOLD, 16f));
		summaryLabel.setBorder(new EmptyBorder(8, 12, 8, 12));

		add(top, BorderLayout.NORTH);
		add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
		add(summaryLabel, BorderLayout.SOUTH);

		showSelectedDay();
	}

	// Gom các ngày khác nhau có đơn (không trùng), sắp ngày mới nhất lên đầu
	private void collectDays() {
		for (Order o : repo.findAll()) {
			LocalDate d = o.getCreatedAt().toLocalDate();
			if (!days.contains(d)) {
				days.add(d);
			}
		}
		Collections.sort(days, Collections.reverseOrder());
	}

	// Hiện các đơn của ngày đang chọn và cập nhật tổng doanh thu
	private void showSelectedDay() {
		int index = dayCombo.getSelectedIndex();
		if (index < 0) { // chưa có đơn nào trong kho
			tableModel.setOrders(new ArrayList<>());
			summaryLabel.setText("Chưa có đơn hàng nào đã thanh toán");
			return;
		}
		List<Order> orders = new ArrayList<>(repo.findByDate(days.get(index)));
		orders.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt())); // theo giờ tăng dần

		double sum = 0;
		for (Order o : orders) {
			sum += o.getTotal();
		}
		tableModel.setOrders(orders);
		summaryLabel.setText("Số đơn: " + orders.size() + "   |   Tổng doanh thu ngày: " + MoneyFormat.format(sum));
	}
}