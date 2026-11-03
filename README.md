# Coffee Shop POS – INT1332 (Nhóm 05)

Ứng dụng quản lý bán hàng cho quán cà phê, viết bằng **Java Swing + JFreeChart 1.5.6**.
Bài tập lớn môn Lập trình hướng đối tượng – PTIT.

## Thành viên

| STT | Họ tên | MSSV | Vai trò |
|---|---|---|---|
| 1 | Tân (nhóm trưởng) | N24DVCN042 | A – Tích hợp, MainWindow · B – Model |
| 2 | Nguyễn Đức Nhật | N24DVCN033 | C – Giao diện (MenuPanel, CheckoutDialog, TablePanel) |
| 3 | Nguyễn Minh Sơn | N24DVCN039 | D – Dữ liệu, biểu đồ (ChartHelper, SampleData) |
| 4 | Hà Tuấn Đạt | N24DVCN014  | Hỗ trợ – AboutDialog, SalesRepository |

## Tính năng

- Chọn bàn, gọi món (đồ uống có size S/M/L, đồ ăn, combo), giỏ hàng có thêm/xóa/sửa số lượng.
- 4 chiến lược giảm giá: không giảm, thành viên, giảm theo %, Happy Hour (14:00–16:00, giảm 20% đồ uống).
- Thanh toán tiền mặt, tính tiền thối, báo lỗi khi nhập sai hoặc thiếu tiền.
- Báo cáo đơn đã thanh toán theo ngày.
- Biểu đồ: doanh thu theo ngày, doanh thu theo giờ, top món bán chạy.
- Menu File/View/Help, toolbar, status bar, hộp thoại About.

## Cấu trúc mã nguồn
src/vn/edu/ptit/oop/nhom05/
├── Main.java
├── model/ Category, MenuItem (abstract), Food, Drink, Combo,
│ OrderItem, Order, DiscountStrategy + 4 cài đặt, MenuData
├── exception/ EmptyOrderException, InvalidPaymentException
├── repository/ SalesRepository
├── util/ MoneyFormat, ChartHelper, SampleData
├── ui/ BaseWindow, MainWindow, ReportWindow, ChartWindow
│ ├── panels/ MenuPanel, TablePanel, CartTableModel, OrderTableModel
│ └── dialogs/ CheckoutDialog, AboutDialog
└── test/ TestCaseRunner
src/resources/icons/ ảnh món và icon
lib/ jfreechart-1.5.6.jar
docs/ sơ đồ UML

## Thiết kế OOP (tóm tắt)

- **Trừu tượng / kế thừa**: `MenuItem` (abstract) → `Food`, `Drink`, `Combo`.
- **Đa hình**: `getPrice()` được gọi qua kiểu cha, không dùng `instanceof`.
- **Composite**: `Combo` chứa danh sách `MenuItem`.
- **Strategy**: `DiscountStrategy` với `NoDiscount`, `MemberDiscount`, `PercentDiscount`, `HappyHourDiscount`.
- **Đóng gói**: `Order` trả về danh sách không sửa được, setter có kiểm tra dữ liệu.
- **Ngoại lệ tự định nghĩa**: `EmptyOrderException`, `InvalidPaymentException`.

## Cách chạy

**Cách 1 – Eclipse / STS**
1. `File → Import → Existing Projects into Workspace`, chọn thư mục project.
2. Đảm bảo `lib/jfreechart-1.5.6.jar` nằm trong Build Path.
3. Chuột phải `Main.java` → `Run As → Java Application`.

**Cách 2 – JAR** (cần JDK 17+)
java -jar app/OOP_Nhom05.jar


Để xem Happy Hour khi không đúng giờ: menu **View → Giả lập giờ Happy Hour (14:30)**.

## Kiểm thử

Chạy `vn.edu.ptit.oop.nhom05.test.TestCaseRunner` (21 ca tự động). Các ca thao tác tay (TC14, TC23, TC24, TC25) nằm trong báo cáo.

## Liên kết

- Git: https://github.com/nhattan3007/CoffeeShopPOS
- Video demo dự phòng: <dán link>