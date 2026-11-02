package vn.edu.ptit.oop.nhom05.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;
import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderItem;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class CheckoutDialog extends JDialog {

	private final Order order;
	private final JComboBox<DiscountStrategy> cboDiscount;

	private final JLabel lblSubtotal;
	private final JLabel lblDiscount;
	private final JLabel lblTotal;

	private final JTextField txtCash;
	private final JLabel lblChange;

	private final JButton btnPay;
	private final JButton btnClose;

	private final JTextArea txtReceipt;

	private boolean paid = false;

	public CheckoutDialog(JFrame parent, Order order, List<DiscountStrategy> options) {
		super(parent, "Thanh toán", true);

		if (order == null) {
			throw new IllegalArgumentException("Đơn hàng không được null");
		}

		if (options == null || options.isEmpty()) {
			throw new IllegalArgumentException("Danh sách chương trình giảm giá không được rỗng");
		}

		this.order = order;

		cboDiscount = new JComboBox<>(options.toArray(new DiscountStrategy[0]));

		lblSubtotal = new JLabel();
		lblDiscount = new JLabel();
		lblTotal = new JLabel();
		lblChange = new JLabel("Chưa thanh toán");

		txtCash = new JTextField();
		txtReceipt = new JTextArea(10, 35);
		txtReceipt.setEditable(false);
		txtReceipt.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 13));

		btnPay = new JButton("Thanh toán");
		btnClose = new JButton("Đóng");

		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 10, 10));

		summaryPanel.add(new JLabel("Chương trình giảm giá:"));
		summaryPanel.add(cboDiscount);

		summaryPanel.add(new JLabel("Tiền tạm tính:"));
		summaryPanel.add(lblSubtotal);

		summaryPanel.add(new JLabel("Tiền giảm:"));
		summaryPanel.add(lblDiscount);

		summaryPanel.add(new JLabel("Tổng thanh toán:"));
		summaryPanel.add(lblTotal);

		summaryPanel.add(new JLabel("Tiền khách đưa:"));
		summaryPanel.add(txtCash);

		summaryPanel.add(new JLabel("Tiền thừa:"));
		summaryPanel.add(lblChange);

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnPay);
		buttonPanel.add(btnClose);

		bottomPanel.add(buttonPanel, BorderLayout.NORTH);
		bottomPanel.add(new JScrollPane(txtReceipt), BorderLayout.CENTER);

		content.add(summaryPanel, BorderLayout.NORTH);
		content.add(bottomPanel, BorderLayout.CENTER);

		setContentPane(content);

		cboDiscount.addActionListener(e -> {
			if (!paid) {
				updateDiscount();
			}
		});

		btnPay.addActionListener(e -> processPayment());
		btnClose.addActionListener(e -> dispose());

		cboDiscount.setSelectedIndex(0);
		updateDiscount();

		setSize(560, 520);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void updateDiscount() {
		if (paid) {
			return;
		}

		DiscountStrategy selected = (DiscountStrategy) cboDiscount.getSelectedItem();

		order.setDiscount(selected);

		lblSubtotal.setText(MoneyFormat.format(order.getSubtotal()));

		lblDiscount.setText(MoneyFormat.format(order.getDiscountAmount()));

		lblTotal.setText(MoneyFormat.format(order.getTotal()));
	}

	private void processPayment() {
		String cashText = txtCash.getText().trim();

		// Kiểm tra ô nhập tiền bị trống
		if (cashText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ", "Lỗi dữ liệu",
					JOptionPane.WARNING_MESSAGE);
			txtCash.requestFocus();
			return;
		}

		// Loại bỏ các ký tự không phải chữ số
		String digits = cashText.replaceAll("[^0-9]", "");

		// Không có chữ số nào, ví dụ nhập abc
		if (digits.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ", "Lỗi dữ liệu",
					JOptionPane.ERROR_MESSAGE);
			txtCash.requestFocus();
			return;
		}

		double cashGiven;

		try {
			// Chỉ chấp nhận số nguyên không âm, không nhận chữ hoặc ký tự lạ
			if (!cashText.matches("\\d+")) {
				throw new NumberFormatException();
			}

			cashGiven = Double.parseDouble(cashText);

			if (!Double.isFinite(cashGiven)) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ! Vui lòng chỉ nhập chữ số.", "Lỗi dữ liệu",
					JOptionPane.ERROR_MESSAGE);
			txtCash.requestFocus();
			return;
		}

		try {
			double change = order.pay(cashGiven);

			paid = true;

			lblChange.setText(MoneyFormat.format(change));

			btnPay.setEnabled(false);
			cboDiscount.setEnabled(false);
			txtCash.setEditable(false);

			txtReceipt.setText(createReceipt(cashGiven, change));

			JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nTiền thừa: " + MoneyFormat.format(change),
					"Thành công", JOptionPane.INFORMATION_MESSAGE);

		} catch (EmptyOrderException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Giỏ hàng trống", JOptionPane.WARNING_MESSAGE);

		} catch (InvalidPaymentException ex) {
			JOptionPane.showMessageDialog(this,
					ex.getMessage() + "\nCòn thiếu: " + MoneyFormat.format(ex.getShortage()), "Thanh toán thất bại",
					JOptionPane.WARNING_MESSAGE);

		} catch (IllegalStateException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể thanh toán", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String createReceipt(double cashGiven, double change) {
		StringBuilder receipt = new StringBuilder();

		receipt.append("========== HOA DON ==========\n");
		receipt.append("Ma don: ").append(order.getId()).append("\n");

		receipt.append("Ban: ").append(order.getTableNo()).append("\n");

		receipt.append("Thoi gian: ")
				.append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");

		receipt.append("-----------------------------\n");

		for (OrderItem item : order.getItems()) {
			receipt.append(item.getMenuItem().getDisplayName()).append(" x").append(item.getQuantity()).append("\n");

			receipt.append("  Thanh tien: ").append(MoneyFormat.format(item.getLineTotal())).append("\n");

			if (!item.getNote().isEmpty()) {
				receipt.append("  Ghi chu: ").append(item.getNote()).append("\n");
			}
		}

		receipt.append("-----------------------------\n");

		receipt.append("Tam tinh: ").append(MoneyFormat.format(order.getSubtotal())).append("\n");

		receipt.append("Giam gia: ").append(MoneyFormat.format(order.getDiscountAmount())).append("\n");

		receipt.append("Tong cong: ").append(MoneyFormat.format(order.getTotal())).append("\n");

		receipt.append("Khach dua: ").append(MoneyFormat.format(cashGiven)).append("\n");

		receipt.append("Tien thua: ").append(MoneyFormat.format(change)).append("\n");

		receipt.append("=============================\n");
		receipt.append("       CAM ON QUY KHACH!\n");

		return receipt.toString();
	}

	public boolean isPaid() {
		return paid;
	}
}