package vn.edu.ptit.oop.nhom05.demo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.ui.panels.TablePanel;

public class TablePanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sơ đồ bàn quán cà phê");

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new TablePanel());

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}