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

            Order order = new Order("DEMO001", 1);

            Drink coffee = new Drink(
                    "D01",
                    "Cà phê sữa",
                    25000,
                    Drink.Size.L
            );

            order.addItem(coffee, 2, "Ít đá");

            HappyHourDiscount happyHour = new HappyHourDiscount();
            happyHour.setSimulatedTime(LocalTime.of(14, 30));

            List<DiscountStrategy> discounts = Arrays.asList(
                    new NoDiscount(),
                    happyHour
            );

            JFrame parent = new JFrame();

            CheckoutDialog dialog = new CheckoutDialog(
                    parent,
                    order,
                    discounts
            );

            dialog.setVisible(true);

            System.out.println("Đã thanh toán: " + dialog.isPaid());

            parent.dispose();
        });
    }
}
