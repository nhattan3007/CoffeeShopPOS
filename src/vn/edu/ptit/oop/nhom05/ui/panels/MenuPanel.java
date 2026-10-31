package vn.edu.ptit.oop.nhom05.ui.panels;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import vn.edu.ptit.oop.nhom05.model.MenuData;
import vn.edu.ptit.oop.nhom05.model.MenuItem;

public class MenuPanel extends JPanel {

    private final MenuSelectionListener listener;

    public MenuPanel(MenuSelectionListener listener) {
        this.listener = listener;

        setLayout(new GridLayout(0, 3, 8, 8));

        List<MenuItem> menuItems = MenuData.getAll();

        for (MenuItem item : menuItems) {
            JButton button = new JButton(item.getName());

            button.addActionListener(e -> {
                this.listener.onItemSelected(item);
            });

            add(button);
        }
    }
}