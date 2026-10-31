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

- Branch: feature/c-gui
- Mục tiêu: Bổ sung tab danh mục và hiển thị giá cho MenuPanel.
- AI hỗ trợ: Hướng dẫn sử dụng JTabbedPane để tạo các tab, MenuData.getByCategory() để lọc món, và MoneyFormat.format() để định dạng giá.
- Thay đổi: MenuPanel có 4 tab Tất cả, Đồ uống, Đồ ăn và Combo. Mỗi nút hiển thị tên món và giá trên hai dòng. 
- **Kiểm thử: Chạy MenuPanelDemo.java, kiểm tra số lượng món và giá hiển thị ở từng tab.
- Kết quả: Kiểm thử thành công. Tab Tất cả hiển thị 13 món, Đồ uống 6 món, Đồ ăn 5 món và Combo 2 món. Giá món được hiển thị đúng định dạng, Combo sáng hiển thị 49.500 đ.
- Các file liên quan: MenuPanel.java.

## Mảnh 3 — Chọn size đồ uống

- Branch: feature/c-gui
- Mục tiêu: Cho phép chọn size S, M, L khi chọn đồ uống.
- AI hỗ trợ: Hướng dẫn sử dụng JOptionPane.showOptionDialog, tính giá theo Drink.Size.getFactor() và tạo đối tượng Drink mới theo size đã chọn.
- Xử lý: Đồ uống hiển thị hộp thoại chọn size; đồ ăn và combo được chuyển trực tiếp cho listener; hủy hộp thoại không gọi listener.
- Kiểm thử: Chọn size L của Cà phê sữa, hủy chọn size, chọn Bánh mì thịt và chọn Combo sáng.
- Kết quả: Chọn size L thành công, Console in Chọn: Cà phê sữa (L) = 35000.0.
- File thay đổi: MenuPanel.java.

## Mảnh 4 — Thêm icon cho các nút món ăn

- Branch: feature/c-gui
- Mục tiêu: Hiển thị icon cho các nút món ăn, đồ uống và combo.
- AI hỗ trợ: Hướng dẫn sử dụng getResource() để tải ảnh PNG theo mã món, kiểm tra ảnh tồn tại trước khi sử dụng và dùng `Image.getScaledInstance()` để thu nhỏ icon.
- Xử lý: Tìm ảnh trong thư mục /icons theo mã món; nếu tìm thấy thì hiển thị icon kích thước 64 × 64 pixel phía trên tên món và giá tiền; nếu không tìm thấy ảnh thì bỏ qua, không gây lỗi.
- Kiểm thử: Kiểm tra icon của đồ uống, đồ ăn và combo; kiểm tra tên món và giá tiền không bị che; kiểm tra ứng dụng vẫn hoạt động khi không tìm thấy ảnh.
- Kết quả: Hiển thị icon theo mã món, thu nhỏ ảnh để giao diện dễ nhìn và giữ nguyên chức năng chọn món, chọn size đồ uống.
- File thay đổi: MenuPanel.java, các file PNG trong src/resources/icons/.

## Mảnh 5 — Tạo giao diện CheckoutDialog và chọn giảm giá

- Branch: feature/c-gui
- Mục tiêu: Tạo cửa sổ thanh toán, hiển thị tiền tạm tính, tiền giảm giá và tổng thanh toán.
- AI hỗ trợ: Hướng dẫn xây dựng giao diện Java Swing bằng JDialog, JComboBox và JLabel; tích hợp Order, DiscountStrategy, NoDiscount và HappyHourDiscount.
- Xử lý: Hiển thị danh sách chương trình giảm giá; cập nhật tiền tạm tính, tiền giảm và tổng thanh toán khi thay đổi chương trình.
- Kiểm thử: Tạo đơn hàng gồm 2 ly cà phê sữa size L, giá gốc 25.000 đ/ly. Kiểm tra không khuyến mãi cho tổng tiền 70.000 đ và Happy Hour lúc 14:30 cho tiền giảm 14.000 đ, tổng thanh toán 56.000 đ.
- Kết quả: Giao diện thanh toán và chức năng chọn chương trình giảm giá hoạt động đúng theo kết quả kiểm thử.
- File thay đổi: CheckoutDialog.java, CheckoutDialogDemo.java, docs/ai-log.md.