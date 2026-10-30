package vn.edu.ptit.oop.nhom05.ui.panels;

import javax.swing.table.AbstractTableModel;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderItem;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

// KẾ THỪA AbstractTableModel: JTable hỏi "có bao nhiêu dòng, ô này là gì?", lớp này trả lời
public class CartTableModel extends AbstractTableModel {
	private final String[] cols = { "Món", "SL", "Đơn giá", "Thành tiền", "Ghi chú" };
	private Order order;

	public CartTableModel(Order order) {
		this.order = order;
	}

	/** Khi bắt đầu đơn mới thì đổi sang đơn mới. */
	public void setOrder(Order order) {
		this.order = order;
		refresh();
	}

	/** Báo cho JTable vẽ lại sau khi giỏ hàng thay đổi. */
	public void refresh() {
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return order.getItems().size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public String getColumnName(int c) {
		return cols[c];
	}

	@Override
	public Object getValueAt(int r, int c) {
		OrderItem it = order.getItems().get(r);
		switch (c) {
		case 0:
			return it.getMenuItem().getDisplayName();
		case 1:
			return it.getQuantity();
		case 2:
			return MoneyFormat.format(it.getMenuItem().getPrice());
		case 3:
			return MoneyFormat.format(it.getLineTotal());
		default:
			return it.getNote();
		}
	}
}