# AI Usage Log

## Mảnh 1 — MenuPanel cơ bản

- Người thực hiện: Nhật
- Branch: feature/c-gui
- Mục tiêu: Tạo giao diện chọn món bằng Java Swing.
- Các file: MenuSelectionListener.java, MenuPanel.java, MenuPanelDemo.java.
- AI hỗ trợ: Hướng dẫn xây dựng MenuPanel sử dụng MenuData.getAll(), GridLayout(0, 3, 8, 8) và JButton cho từng món. Khi nhấn nút, gọi MenuSelectionListener.onItemSelected(item).
- Kiểm thử: Chạy MenuPanelDemo.java, kiểm tra 13 nút và thông tin món trong Console.
- Kết quả kiểm thử: Cập nhật sau khi chạy thử thực tế.
- Ghi chú: Đọc hiểu code trước khi commit.
## Mảnh 2 — Tab danh mục và hiển thị giá

- **Branch:** `feature/c-gui`
- **Mục tiêu:** Bổ sung tab danh mục và hiển thị giá cho MenuPanel.
- **AI hỗ trợ:** Hướng dẫn sử dụng JTabbedPane để tạo các tab, MenuData.getByCategory() để lọc món, và MoneyFormat.format() để định dạng giá.
- **Thay đổi:** MenuPanel có 4 tab Tất cả, Đồ uống, Đồ ăn và Combo. Mỗi nút hiển thị tên món và giá trên hai dòng.
  - **Kiểm thử:** Chạy MenuPanelDemo.java, kiểm tra số lượng món và giá hiển thị ở từng tab.
- **Kết quả:** [Ghi kết quả thực tế sau khi kiểm tra đủ 4 tab.]
- **Các file liên quan:** MenuPanel.java.