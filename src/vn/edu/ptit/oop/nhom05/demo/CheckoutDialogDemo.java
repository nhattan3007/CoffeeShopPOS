package vn.edu.ptit.oop.nhom05.demo;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.model.DiscountStrategy;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.HappyHourDiscount;
import vn.edu.ptit.oop.nhom05.model.NoDiscount;
import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.ui.dialogs.CheckoutDialog;

public class CheckoutDialogDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Tạo đơn hàng mẫu.
            Order order = new Order("DEMO001", 1);

            // Thêm 2 ly cà phê sữa size L.
            Drink coffee = new Drink(
                    "D01",
                    "Cà phê sữa",
                    25000,
                    Drink.Size.L
            );

            order.addItem(coffee, 2, "");

            // Tạo các chương trình giảm giá.
            HappyHourDiscount happyHour = new HappyHourDiscount();
            happyHour.setSimulatedTime(LocalTime.of(14, 30));

            List<DiscountStrategy> discounts = Arrays.asList(
                    new NoDiscount(),
                    happyHour
            );

            // Mở cửa sổ thanh toán.
            JFrame parent = new JFrame();
            parent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            CheckoutDialog dialog = new CheckoutDialog(
                    parent,
                    order,
                    discounts
            );

            dialog.setVisible(true);

            parent.dispose();
        });
    }
}
