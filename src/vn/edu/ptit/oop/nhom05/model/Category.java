// ===== model/Category.java =====
package vn.edu.ptit.oop.nhom05.model;

public enum Category {
	// Mỗi giá trị enum kèm 1 nhãn tiếng Việt để hiển thị lên giao diện sau này
	DRINK("Đồ uống"), FOOD("Đồ ăn"), COMBO("Combo");

	private final String label; // nhãn hiển thị

	Category(String label) {
		this.label = label;
	} // constructor của enum

	public String getLabel() {
		return label;
	}
}