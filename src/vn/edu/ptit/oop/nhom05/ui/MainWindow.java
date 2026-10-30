package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	private JLabel totalLabel;

	public MainWindow() {
		super("Nhóm 05 – Coffee Shop POS", 1100, 700);
	}

	@Override
	protected void initUI() {
		// Khởi tạo mọi thứ BÊN TRONG initUI()
		order = new Order("HD001", 1);
		cartModel = new CartTableModel(order);

		totalLabel = new JLabel();
		totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 20f));
		totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalLabel.setBorder(new EmptyBorder(8, 8, 8, 16));

		// Bảng giỏ hàng + tổng tiền nằm chung trong một panel.
		// Không thêm totalLabel vào SOUTH của cửa sổ vì BaseWindow đã dùng SOUTH cho
		// thanh trạng thái
		JPanel cartPanel = new JPanel(new BorderLayout());
		cartPanel.add(new JScrollPane(new JTable(cartModel)), BorderLayout.CENTER);
		cartPanel.add(totalLabel, BorderLayout.SOUTH);
		add(cartPanel, BorderLayout.CENTER);

		// Nút thử tạm thời (chưa có icon nên BaseWindow hiện chữ). Sẽ bỏ khi có
		// MenuPanel thật
		addToolButton("/resources/icons/add.png", "+ Cà phê đen (thử)", e -> addTestItem());

		refreshCart();
	}

	private void addTestItem() {
		order.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.M), 1, "");
		refreshCart();
	}

	// Gom mọi việc "giỏ hàng vừa đổi" vào một chỗ để các nút khác sau này cũng gọi
	// lại
	private void refreshCart() {
		cartModel.refresh();
		totalLabel.setText("Tổng: " + MoneyFormat.format(order.getTotal()));
		setStatus("Giỏ hàng có " + order.getItems().size() + " dòng");
	}
}