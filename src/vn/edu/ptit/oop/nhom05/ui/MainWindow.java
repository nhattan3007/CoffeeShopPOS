package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
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
import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.HappyHourDiscount;
import vn.edu.ptit.oop.nhom05.model.MemberDiscount;
import vn.edu.ptit.oop.nhom05.model.MenuItem;
import vn.edu.ptit.oop.nhom05.model.NoDiscount;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderStatus;
import vn.edu.ptit.oop.nhom05.model.PercentDiscount;
import vn.edu.ptit.oop.nhom05.ui.dialogs.CheckoutDialog;
import vn.edu.ptit.oop.nhom05.ui.panels.CartTableModel;
import vn.edu.ptit.oop.nhom05.ui.panels.MenuPanel;
import vn.edu.ptit.oop.nhom05.ui.panels.TablePanel;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class MainWindow extends BaseWindow {

    private Order order;
    private CartTableModel cartModel;
    private JTable table;
    private JLabel totalLabel;

    private int orderCounter;

    /*
     * Không khởi tạo HashMap tại đây.
     * BaseWindow gọi initUI() từ constructor của lớp cha,
     * trước khi các thuộc tính của MainWindow được khởi tạo.
     */
    private Map<Integer, Order> ordersByTable;

    private TablePanel tablePanel;
    private int selectedTableNo = -1;

    public MainWindow() {
        super("Nhóm 05 - Coffee Shop POS", 1100, 700);
    }

    @Override
    protected void initUI() {

        // Phải khởi tạo trước khi sử dụng ordersByTable.
        ordersByTable = new HashMap<>();
        orderCounter = 0;

        // Tạo sơ đồ bàn.
        tablePanel = new TablePanel();

        // Tạo đơn ban đầu cho bàn 1.
        order = createOrder(1);
        ordersByTable.put(1, order);
        selectedTableNo = 1;

        // Khởi tạo bảng hóa đơn.
        cartModel = new CartTableModel(order);
        table = new JTable(cartModel);

        // Nhãn tổng tiền.
        totalLabel = new JLabel();
        totalLabel.setFont(
                totalLabel.getFont().deriveFont(Font.BOLD, 20f)
        );
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setBorder(new EmptyBorder(8, 8, 8, 16));

        // Khu vực hóa đơn.
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        // Khu vực thanh toán.
        JPanel checkoutPanel = new JPanel(new BorderLayout(5, 5));
        checkoutPanel.add(totalLabel, BorderLayout.NORTH);

        JButton btnCheckout = new JButton("THANH TOÁN");
        btnCheckout.setFont(
                btnCheckout.getFont().deriveFont(Font.BOLD, 16f)
        );
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setBackground(new Color(40, 167, 69));
        btnCheckout.setOpaque(true);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setPreferredSize(new Dimension(180, 45));
        btnCheckout.addActionListener(e -> openCheckout());

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        buttonPanel.add(btnCheckout);

        checkoutPanel.add(buttonPanel, BorderLayout.SOUTH);
        cartPanel.add(checkoutPanel, BorderLayout.SOUTH);

        // Xử lý chọn bàn.
        tablePanel.setOnTableSelected(this::selectTable);

        // Khu vực chính.
        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );
        centerPanel.add(tablePanel, BorderLayout.NORTH);

        // Chia giao diện thành menu và hóa đơn.
        JPanel contentPanel = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        MenuPanel menuPanel = new MenuPanel(
                this::handleMenuItemSelected
        );

        contentPanel.add(menuPanel);
        contentPanel.add(cartPanel);

        centerPanel.add(contentPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Nút xóa món đã chọn.
        addToolButton(
                "/resources/icons/delete.png",
                "Xóa dòng đã chọn",
                e -> removeSelectedItem()
        );

        // Menu tạo đơn mới.
        JMenuItem newOrderItem = new JMenuItem("Đơn mới");
        newOrderItem.setAccelerator(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_N,
                        InputEvent.CTRL_DOWN_MASK
                )
        );
        newOrderItem.addActionListener(e -> newOrder());

        fileMenu.insert(newOrderItem, 0);
        fileMenu.insertSeparator(1);

        refreshCart();
        setStatus("Đang chọn bàn 1");
    }

    /**
     * Tạo đơn hàng mới.
     */
    private Order createOrder(int tableNo) {
        orderCounter++;

        String orderId = String.format("HD%03d", orderCounter);

        return new Order(orderId, tableNo);
    }

    /**
     * Chuyển sang bàn được chọn.
     */
    private void selectTable(int tableNo) {

        Order selectedOrder = ordersByTable.get(tableNo);

        // Nếu bàn đã có đơn, mở lại đơn cũ.
        if (selectedOrder != null) {

            if (selectedOrder.getStatus() != OrderStatus.OPEN) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đơn hàng của bàn này đã thanh toán.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            order = selectedOrder;
            selectedTableNo = tableNo;

            cartModel.setOrder(order);
            refreshCart();

            setStatus("Đang xem hóa đơn bàn " + tableNo);
            return;
        }

        // Bàn đang bận nhưng không có đơn trong hệ thống.
        if (tablePanel.isTableOccupied(tableNo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bàn " + tableNo
                            + " đang phục vụ nhưng chưa có hóa đơn trong hệ thống.",
                    "Bàn đang bận",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Bàn trống: tạo đơn mới.
        order = createOrder(tableNo);
        ordersByTable.put(tableNo, order);
        selectedTableNo = tableNo;

        cartModel.setOrder(order);
        refreshCart();

        setStatus("Đã tạo đơn mới cho bàn " + tableNo);
    }

    /**
     * Thêm món vào đơn hàng của bàn đang chọn.
     */
    private void handleMenuItemSelected(MenuItem item) {

        if (order == null || selectedTableNo < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn bàn trước khi gọi món.",
                    "Chưa chọn bàn",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (order.getStatus() != OrderStatus.OPEN) {
            JOptionPane.showMessageDialog(
                    this,
                    "Đơn hàng đã thanh toán.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            order.addItem(item, 1, "");

            ordersByTable.put(order.getTableNo(), order);

            // Đánh dấu bàn đang phục vụ khi có món.
            if (!order.getItems().isEmpty()) {
                tablePanel.setTableOccupied(
                        order.getTableNo(),
                        true
                );
            }

            refreshCart();

            setStatus(
                    "Đã thêm " + item.getName()
                            + " vào bàn " + order.getTableNo()
            );

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể thêm món vào đơn hàng.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Xóa món đang chọn trong bảng.
     */
    private void removeSelectedItem() {

        if (order == null) {
            return;
        }

        if (order.getStatus() != OrderStatus.OPEN) {
            JOptionPane.showMessageDialog(
                    this,
                    "Đơn hàng đã thanh toán, không thể xóa món.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn món cần xóa.",
                    "Chưa chọn món",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        try {
            int modelRow = table.convertRowIndexToModel(selectedRow);

            order.removeItem(modelRow);

            // Nếu không còn món, giải phóng bàn.
            if (order.getItems().isEmpty()) {
                tablePanel.setTableOccupied(
                        order.getTableNo(),
                        false
                );
            }

            refreshCart();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể xóa món.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Tạo đơn mới cho bàn đang chọn.
     */
    private void newOrder() {

        if (selectedTableNo < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn bàn trước.",
                    "Chưa chọn bàn",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Order existingOrder = ordersByTable.get(selectedTableNo);

        // Nếu bàn đã có đơn mở, mở lại đơn đó.
        if (existingOrder != null
                && existingOrder.getStatus() == OrderStatus.OPEN) {

            order = existingOrder;
            cartModel.setOrder(order);
            refreshCart();

            JOptionPane.showMessageDialog(
                    this,
                    "Bàn " + selectedTableNo
                            + " đã có đơn. Đã mở lại hóa đơn hiện tại.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (tablePanel.isTableOccupied(selectedTableNo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bàn này đang phục vụ khách khác.",
                    "Bàn đang bận",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        order = createOrder(selectedTableNo);
        ordersByTable.put(selectedTableNo, order);

        cartModel.setOrder(order);
        refreshCart();

        setStatus("Đã tạo đơn mới cho bàn " + selectedTableNo);
    }

    /**
     * Mở cửa sổ thanh toán.
     */
    private void openCheckout() {

        if (order == null || order.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng thêm món trước khi thanh toán.",
                    "Giỏ hàng trống",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (order.getStatus() != OrderStatus.OPEN) {
            JOptionPane.showMessageDialog(
                    this,
                    "Đơn hàng đã được thanh toán.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        List<DiscountStrategy> discounts = Arrays.asList(
                new NoDiscount(),
                new MemberDiscount(),
                new PercentDiscount(10),
                new HappyHourDiscount()
        );

        // Giữ tham chiếu đến đơn đang thanh toán.
        Order paidOrder = order;

        CheckoutDialog dialog = new CheckoutDialog(
                this,
                paidOrder,
                discounts
        );

        dialog.setVisible(true);

        if (!dialog.isPaid()) {
            return;
        }

        int paidTableNo = paidOrder.getTableNo();
        String paidOrderId = paidOrder.getId();

        // Xóa đơn đã thanh toán.
        ordersByTable.remove(paidTableNo);

        // Giải phóng bàn.
        tablePanel.setTableOccupied(paidTableNo, false);

        JOptionPane.showMessageDialog(
                this,
                "Đơn hàng " + paidOrderId
                        + " đã thanh toán thành công!"
        );

        /*
         * Tạo đơn rỗng chỉ để bảng hóa đơn không hiển thị món cũ.
         * Không đưa đơn rỗng này vào ordersByTable.
         */
        order = createOrder(paidTableNo);
        selectedTableNo = -1;

        cartModel.setOrder(order);
        refreshCart();

        setStatus(
                "Bàn " + paidTableNo
                        + " đã thanh toán. Vui lòng chọn bàn tiếp theo."
        );
    }

    /**
     * Cập nhật bảng hóa đơn và tổng tiền.
     */
    private void refreshCart() {

        if (cartModel == null || order == null || totalLabel == null) {
            return;
        }

        cartModel.refresh();

        totalLabel.setText(
                "Tổng: " + MoneyFormat.format(order.getTotal())
        );

        setStatus(
                "Đơn " + order.getId()
                        + " - Bàn " + order.getTableNo()
                        + " - " + order.getItems().size() + " dòng"
        );
    }
}