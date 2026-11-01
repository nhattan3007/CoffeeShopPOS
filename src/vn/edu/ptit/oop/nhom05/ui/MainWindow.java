package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
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
import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;
import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.HappyHourDiscount;
import vn.edu.ptit.oop.nhom05.model.MemberDiscount;
import vn.edu.ptit.oop.nhom05.model.NoDiscount;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.PercentDiscount;
import vn.edu.ptit.oop.nhom05.ui.panels.CartTableModel;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class MainWindow extends BaseWindow {
	// KHÔNG viết "= new ..." ở đây: BaseWindow gọi initUI() khi constructor này
	// chưa chạy xong
	private Order order;
	private CartTableModel cartModel;
	private JTable table;
	private JLabel subtotalLabel;
	private JLabel discountLabel;
	private JLabel totalLabel;
	private JComboBox<DiscountStrategy> discountCombo; // MỚI b2
	private HappyHourDiscount happyHour; // MỚI b2: MỘT đối tượng dùng chung
	private List<Order> paidOrders; // MỚI b3: tạm giữ đơn đã trả tiền
	private int orderCounter;

	public MainWindow() {
		super("Nhóm 05 – Coffee Shop POS", 1100, 700);
	}

	@Override
	protected void initUI() {
		// Khởi tạo mọi thứ BÊN TRONG initUI()
		orderCounter = 1;
		paidOrders = new ArrayList<>();
		happyHour = new HappyHourDiscount();
		order = new Order(nextOrderId(), 1);
		cartModel = new CartTableModel(order);
		table = new JTable(cartModel);

		// ----- MỚI b2: hộp chọn khuyến mãi -----
		// JComboBox hiện tên mỗi mục bằng toString(), mà mỗi lớp giảm giá đã viết
		// toString() = getName()
		discountCombo = new JComboBox<>();
		discountCombo.addItem(new NoDiscount());
		discountCombo.addItem(new PercentDiscount(15));
		discountCombo.addItem(new MemberDiscount());
		discountCombo.addItem(happyHour);
		// Gắn listener SAU KHI addItem: nếu gắn trước, việc thêm mục đầu tiên cũng bắn
		// sự kiện
		discountCombo.addActionListener(e -> onDiscountChanged());

		// ----- Khu vực tổng tiền: 3 dòng canh phải -----
		subtotalLabel = new JLabel();
		discountLabel = new JLabel();
		totalLabel = new JLabel();
		totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 20f));
		subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		discountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel figures = new JPanel(new GridLayout(3, 1));
		figures.add(subtotalLabel);
		figures.add(discountLabel);
		figures.add(totalLabel);

		JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		discountPanel.add(new JLabel("Khuyến mãi:"));
		discountPanel.add(discountCombo);

		JPanel summary = new JPanel(new BorderLayout());
		summary.setBorder(new EmptyBorder(8, 8, 8, 16));
		summary.add(discountPanel, BorderLayout.WEST);
		summary.add(figures, BorderLayout.EAST);

		// Bảng + khu tổng tiền chung một panel (SOUTH của cửa sổ đã dành cho thanh
		// trạng thái)
		JPanel cartPanel = new JPanel(new BorderLayout());
		cartPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		cartPanel.add(summary, BorderLayout.SOUTH);
		add(cartPanel, BorderLayout.CENTER);

		// ----- Toolbar -----
		addToolButton("/resources/icons/add.png", "+ Cà phê đen (thử)", e -> addTestItem());
		addToolButton("/resources/icons/delete.png", "Xóa dòng đã chọn", e -> removeSelectedItem());
		addToolButton("/resources/icons/pay.png", "Thanh toán", e -> checkout()); // MỚI b3

		// ----- Menu File → Đơn mới (Ctrl+N) -----
		JMenuItem newOrderItem = new JMenuItem("Đơn mới");
		newOrderItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newOrderItem.addActionListener(e -> newOrder());
		fileMenu.insert(newOrderItem, 0);
		fileMenu.insertSeparator(1);

		// ----- MỚI b2: menu View → Giả lập giờ Happy Hour -----
		final JCheckBoxMenuItem simulateItem = new JCheckBoxMenuItem("Giả lập giờ Happy Hour (14:30)");
		simulateItem.addActionListener(e -> {
			happyHour.setSimulatedTime(simulateItem.isSelected() ? LocalTime.of(14, 30) : null);
			refreshCart();
			setStatus(simulateItem.isSelected() ? "Đang giả lập giờ 14:30" : "Đã tắt giả lập giờ");
		});
		viewMenu.add(simulateItem);

		refreshCart();
	}

	private String nextOrderId() {
		return String.format("HD%03d", orderCounter);
	}

	// MỚI b2: lấy khuyến mãi đang chọn (getItemAt trả đúng kiểu DiscountStrategy,
	// không cần ép kiểu)
	private DiscountStrategy getSelectedDiscount() {
		return discountCombo.getItemAt(discountCombo.getSelectedIndex());
	}

	// MỚI b2: đổi khuyến mãi => đơn tính lại, không có if/else theo loại khuyến mãi
	private void onDiscountChanged() {
		order.setDiscount(getSelectedDiscount());
		refreshCart();
	}

	private void addTestItem() {
		order.addItem(new Drink("D01", "Cà phê đen", 20000, Drink.Size.M), 1, "");
		refreshCart();
	}

	private void removeSelectedItem() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Hãy chọn một dòng cần xóa", "Chưa chọn dòng",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		order.removeItem(table.convertRowIndexToModel(row));
		refreshCart();
	}

	// File → Đơn mới: hỏi xác nhận nếu đơn hiện tại còn món
	private void newOrder() {
		if (!order.getItems().isEmpty()) {
			int answer = JOptionPane.showConfirmDialog(this, "Bỏ đơn hiện tại và tạo đơn mới?", "Đơn mới",
					JOptionPane.YES_NO_OPTION);
			if (answer != JOptionPane.YES_OPTION)
				return;
		}
		startNewOrder();
	}

	// MỚI b3: tách riêng việc "chuyển sang đơn mới" để cả newOrder() và checkout()
	// dùng chung
	private void startNewOrder() {
		orderCounter++;
		order = new Order(nextOrderId(), 1);
		order.setDiscount(getSelectedDiscount()); // giữ lựa chọn khuyến mãi đang hiện trên hộp
		cartModel.setOrder(order);
		refreshCart();
	}

	// MỚI b3: thanh toán tạm bằng JOptionPane (CheckoutDialog của C xong sẽ thay
	// vào đây)
	private void checkout() {
		if (order.getItems().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống", "Không thể thanh toán",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		String input = JOptionPane.showInputDialog(this,
				"Phải trả: " + MoneyFormat.format(order.getTotal()) + "\nNhập tiền khách đưa (VND):", "Thanh toán",
				JOptionPane.QUESTION_MESSAGE);
		if (input == null)
			return; // bấm Cancel

		// Bỏ mọi ký tự không phải số (dấu chấm, chữ "đ"...); giới hạn 10 chữ số để
		// không tràn số
		String digits = input.replaceAll("[^0-9]", "");
		if (digits.isEmpty() || digits.length() > 10) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ", "Sai dữ liệu",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		double cash = Long.parseLong(digits);

		try {
			double change = order.pay(cash);
			String message = "Thanh toán thành công!\nPhải trả: " + MoneyFormat.format(order.getTotal())
					+ "\nKhách đưa: " + MoneyFormat.format(cash) + "\nTiền thối: " + MoneyFormat.format(change);
			paidOrders.add(order); // giữ lại để làm báo cáo sau này
			startNewOrder();
			setStatus("Đã thanh toán – tổng đã bán " + paidOrders.size() + " đơn");
			JOptionPane.showMessageDialog(this, message, "Hóa đơn", JOptionPane.INFORMATION_MESSAGE);
		} catch (EmptyOrderException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Không thể thanh toán", JOptionPane.WARNING_MESSAGE);
		} catch (InvalidPaymentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage() + "\nCòn thiếu: " + MoneyFormat.format(e.getShortage()),
					"Chưa đủ tiền", JOptionPane.WARNING_MESSAGE);
		}
	}

	// Gom mọi việc "giỏ hàng vừa đổi" vào một chỗ
	private void refreshCart() {
		cartModel.refresh();
		subtotalLabel.setText("Tạm tính: " + MoneyFormat.format(order.getSubtotal()));
		discountLabel.setText("Giảm giá: " + MoneyFormat.format(order.getDiscountAmount()));
		totalLabel.setText("Phải trả: " + MoneyFormat.format(order.getTotal()));
		setStatus("Đơn " + order.getId() + " – " + order.getItems().size() + " dòng");
	}
}