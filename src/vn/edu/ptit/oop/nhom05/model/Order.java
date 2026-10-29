// ===== model/Order.java ===== (QUAN TRONG)
package vn.edu.ptit.oop.nhom05.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;

public class Order {
	private final String id;
	private final int tableNo;
	private final List<OrderItem> items = new ArrayList<>(); // COMPOSITION: Order chứa OrderItem
	private OrderStatus status = OrderStatus.OPEN;
	private final LocalDateTime createdAt;
	private DiscountStrategy discount = new NoDiscount(); // mặc định không giảm
	private double frozenDiscount; // số tiền giảm được CHỐT lúc thanh toán

	public Order(String id, int tableNo) {
		this(id, tableNo, LocalDateTime.now());
	}

	/**
	 * Constructor có giờ tạo: dùng để tạo đơn mẫu cho biểu đồ "doanh thu theo giờ".
	 */
	public Order(String id, int tableNo, LocalDateTime createdAt) {
		if (tableNo < 1)
			throw new IllegalArgumentException("Số bàn phải ≥ 1");
		if (createdAt == null)
			throw new IllegalArgumentException("Thời gian tạo đơn không được null");
		this.id = id;
		this.tableNo = tableNo;
		this.createdAt = createdAt;
	}

	// Đơn đã thanh toán/hủy thì không cho sửa nữa (bảo vệ số liệu doanh thu)
	private void ensureOpen() {
		if (status != OrderStatus.OPEN)
			throw new IllegalStateException("Đơn hàng đã đóng, không thể thay đổi");
	}

	/**
	 * Thêm món; nếu món (cùng tên hiển thị, tức cùng size) đã có thì cộng dồn số
	 * lượng.
	 */
	public void addItem(MenuItem menuItem, int quantity, String note) {
		ensureOpen();
		if (menuItem == null)
			throw new IllegalArgumentException("Món không được null");
		if (quantity < 1)
			throw new IllegalArgumentException("Số lượng phải ≥ 1");
		for (OrderItem it : items) {
			if (it.getMenuItem().getDisplayName().equals(menuItem.getDisplayName())) {
				it.setQuantity(it.getQuantity() + quantity);
				return;
			}
		}
		items.add(new OrderItem(menuItem, quantity, note));
	}

	public void removeItem(int index) {
		ensureOpen();
		items.remove(index);
	}

	// ĐÓNG GÓI: trả về bản chỉ-đọc, bên ngoài không tự add/remove được
	public List<OrderItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	public double getSubtotal() {
		double sum = 0;
		for (OrderItem it : items)
			sum += it.getLineTotal();
		return sum;
	}

	public double getDrinkSubtotal() {
		double sum = 0;
		for (OrderItem it : items)
			sum += it.getLineDrinkTotal();
		return sum;
	}

	public double getDiscountAmount() {
		if (status == OrderStatus.PAID)
			return frozenDiscount; // đã chốt thì không tính lại
		return Math.min(discount.apply(this), getSubtotal()); // ĐA HÌNH: gọi qua interface; không giảm quá tổng tiền
	}

	public double getTotal() {
		return Math.max(0, getSubtotal() - getDiscountAmount());
	}

	/** Thanh toán: kiểm tra giỏ rỗng / tiền thiếu, trả về tiền thối. */
	public double pay(double cashGiven) throws EmptyOrderException, InvalidPaymentException {
		ensureOpen();
		if (items.isEmpty())
			throw new EmptyOrderException("Giỏ hàng đang trống");
		double total = getTotal();
		if (cashGiven < total)
			throw new InvalidPaymentException("Tiền khách đưa chưa đủ", total - cashGiven);
		frozenDiscount = getDiscountAmount(); // chốt số tiền giảm TRƯỚC khi đổi trạng thái
		status = OrderStatus.PAID;
		return cashGiven - total;
	}

	public void setDiscount(DiscountStrategy d) {
		ensureOpen();
		this.discount = (d == null) ? new NoDiscount() : d;
	}

	public DiscountStrategy getDiscount() {
		return discount;
	}

	public String getId() {
		return id;
	}

	public int getTableNo() {
		return tableNo;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}