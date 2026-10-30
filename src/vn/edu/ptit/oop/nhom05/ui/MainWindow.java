package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import vn.edu.ptit.oop.nhom05.common.BaseWindow;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.ui.panels.CartTableModel;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class MainWindow extends BaseWindow {
	// KHÔNG viết "= new ..." ở đây: BaseWindow gọi initUI() khi constructor này
	// chưa chạy xong
	private Order order;
	private CartTableModel cartModel;
	private JTable table; // MỚI b1: cần giữ lại để biết dòng nào đang được chọn
	private JLabel totalLabel;
	private int orderCounter; // MỚI b1: đếm số đơn để đặt mã HD001, HD002...

	public MainWindow() {
		super("Nhóm 05 – Coffee Shop POS", 1100, 700);
	}

	@Override
	protected void initUI() {
		orderCounter = 1;
		order = new Order(nextOrderId(), 1);
		cartModel = new CartTableModel(order);
		table = new JTable(cartModel);

		totalLabel = new JLabel();
		totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 20f));
		totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalLabel.setBorder(new EmptyBorder(8, 8, 8, 16));

		JPanel cartPanel = new JPanel(new BorderLayout());
		cartPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		cartPanel.add(totalLabel, BorderLayout.SOUTH);
		add(cartPanel, BorderLayout.CENTER);

		// Nút trên toolbar (chưa có icon nên BaseWindow hiện chữ)
		addToolButton("/resources/icons/add.png", "+ Cà phê đen (thử)", e -> addTestItem());
		addToolButton("/resources/icons/delete.png", "Xóa dòng đã chọn", e -> removeSelectedItem()); // MỚI b1

		// MỚI b1: menu File → Đơn mới (Ctrl+N). fileMenu có sẵn từ BaseWindow, chèn lên
		// đầu menu
		JMenuItem newOrderItem = new JMenuItem("Đơn mới");
		newOrderItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newOrderItem.addActionListener(e -> newOrder());
		fileMenu.insert(newOrderItem, 0);
		fileMenu.insertSeparator(1);

		refreshCart();
	}

	private String nextOrderId() {
		return String.format("HD%03d", orderCounter);
	} // MỚI b1: HD001, HD002...

	private void addTestItem() {
		order.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.M), 1, "");
		refreshCart();
	}

	// MỚI b1: xóa dòng đang chọn trong bảng
	private void removeSelectedItem() {
		int row = table.getSelectedRow(); // -1 nếu chưa chọn dòng nào
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Hãy chọn một dòng cần xóa", "Chưa chọn dòng",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		order.removeItem(table.convertRowIndexToModel(row));
		refreshCart();
	}

	// MỚI b1: bắt đầu đơn mới (hỏi lại nếu đơn hiện tại còn món)
	private void newOrder() {
		if (!order.getItems().isEmpty()) {
			int answer = JOptionPane.showConfirmDialog(this, "Bỏ đơn hiện tại và tạo đơn mới?", "Đơn mới",
					JOptionPane.YES_NO_OPTION);
			if (answer != JOptionPane.YES_OPTION)
				return;
		}
		orderCounter++;
		order = new Order(nextOrderId(), 1);
		cartModel.setOrder(order);
		refreshCart();
	}

	// Gom mọi việc "giỏ hàng vừa đổi" vào một chỗ
	private void refreshCart() {
		cartModel.refresh();
		totalLabel.setText("Tổng: " + MoneyFormat.format(order.getTotal()));
		setStatus("Đơn " + order.getId() + " – " + order.getItems().size() + " dòng");
	}
}