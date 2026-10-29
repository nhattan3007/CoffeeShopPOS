// ===== model/OrderItem.java =====
package vn.edu.ptit.oop.nhom05.model;

// Một dòng trong giỏ hàng: món nào + số lượng + ghi chú
public class OrderItem {
	private final MenuItem menuItem;
	private int quantity;
	private String note;

	public OrderItem(MenuItem menuItem, int quantity, String note) {
		if (menuItem == null)
			throw new IllegalArgumentException("Món không được null");
		this.menuItem = menuItem;
		setQuantity(quantity);
		this.note = (note == null) ? "" : note;
	}

	public final void setQuantity(int quantity) {
		if (quantity < 1)
			throw new IllegalArgumentException("Số lượng phải ≥ 1");
		this.quantity = quantity;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = (note == null) ? "" : note;
	}

	// Gọi getPrice() qua kiểu cha MenuItem: Drink/Food/Combo tự tính giá của mình
	public double getLineTotal() {
		return menuItem.getPrice() * quantity;
	}

	public double getLineDrinkTotal() {
		return menuItem.getDrinkPrice() * quantity;
	}
}