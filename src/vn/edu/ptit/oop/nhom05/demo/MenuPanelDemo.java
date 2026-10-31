package vn.edu.ptit.oop.nhom05.demo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.ui.panels.MenuPanel;

public class MenuPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Thử MenuPanel");

            f.add(new MenuPanel(
                    item -> System.out.println(
                            "Chọn: " + item.getDisplayName()
                                    + " = " + item.getPrice()
                    )
            ));

            f.setSize(800, 500);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}
