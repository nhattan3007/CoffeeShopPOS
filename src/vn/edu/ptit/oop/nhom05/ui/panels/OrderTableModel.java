// ===== ui/panels/OrderTableModel.java =====
package vn.edu.ptit.oop.nhom05.ui.panels;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

// Bảng danh sách đơn hàng của một ngày (KẾ THỪA AbstractTableModel, giống CartTableModel)
public class OrderTableModel extends AbstractTableModel {
	private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

	private final String[] cols = { "Mã đơn", "Bàn", "Giờ", "Tạm tính", "Giảm", "Tổng" };
	private List<Order> data = new ArrayList<>();

	/** Đổi danh sách đơn đang hiển thị rồi báo JTable vẽ lại. */
	public void setOrders(List<Order> orders) {
		this.data = orders;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return data.size();
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
		Order o = data.get(r);
		switch (c) {
		case 0:
			return o.getId();
		case 1:
			return o.getTableNo();
		case 2:
			return o.getCreatedAt().format(TIME_FMT);
		case 3:
			return MoneyFormat.format(o.getSubtotal());
		case 4:
			return MoneyFormat.format(o.getDiscountAmount());
		default:
			return MoneyFormat.format(o.getTotal());
		}
	}
}