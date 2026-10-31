package vn.edu.ptit.oop.nhom05.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class CheckoutDialog extends JDialog {

    private final Order order;
    private final JComboBox<DiscountStrategy> cboDiscount;

    private final JLabel lblSubtotal;
    private final JLabel lblDiscount;
    private final JLabel lblTotal;

    public CheckoutDialog(
            JFrame parent,
            Order order,
            List<DiscountStrategy> options
    ) {
        super(parent, "Thanh toán", true);

        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không được null");
        }

        this.order = order;

        cboDiscount = new JComboBox<>(options.toArray(new DiscountStrategy[0]));

        lblSubtotal = new JLabel();
        lblDiscount = new JLabel();
        lblTotal = new JLabel();

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 10, 12));

        summaryPanel.add(new JLabel("Chương trình giảm giá:"));
        summaryPanel.add(cboDiscount);

        summaryPanel.add(new JLabel("Tiền tạm tính:"));
        summaryPanel.add(lblSubtotal);

        summaryPanel.add(new JLabel("Tiền giảm:"));
        summaryPanel.add(lblDiscount);

        summaryPanel.add(new JLabel("Tổng thanh toán:"));
        summaryPanel.add(lblTotal);

        content.add(summaryPanel, BorderLayout.CENTER);

        setContentPane(content);

        // Cập nhật tổng tiền khi người dùng đổi chương trình giảm giá.
        cboDiscount.addActionListener(e -> updateDiscount());

        if (cboDiscount.getItemCount() > 0) {
            cboDiscount.setSelectedIndex(0);
        }

        updateDiscount();

        setSize(480, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void updateDiscount() {
        DiscountStrategy selected =
                (DiscountStrategy) cboDiscount.getSelectedItem();

        order.setDiscount(selected);

        lblSubtotal.setText(MoneyFormat.format(order.getSubtotal()));
        lblDiscount.setText(MoneyFormat.format(order.getDiscountAmount()));
        lblTotal.setText(MoneyFormat.format(order.getTotal()));
    }
}