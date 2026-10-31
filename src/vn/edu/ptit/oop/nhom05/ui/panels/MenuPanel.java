package vn.edu.ptit.oop.nhom05.ui.panels;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import vn.edu.ptit.oop.nhom05.model.Category;
import vn.edu.ptit.oop.nhom05.model.MenuData;
import vn.edu.ptit.oop.nhom05.model.MenuItem;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class MenuPanel extends JPanel {

    private final MenuSelectionListener listener;

    public MenuPanel(MenuSelectionListener listener) {
        this.listener = listener;

        setLayout(new GridLayout(1, 1));

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Tất cả món
        tabs.addTab("Tất cả", createMenuGrid(MenuData.getAll()));

        // Các tab theo danh mục
        for (Category category : Category.values()) {
            tabs.addTab(
                    category.getLabel(),
                    createMenuGrid(MenuData.getByCategory(category))
            );
        }

        add(tabs);
    }

    private JPanel createMenuGrid(List<MenuItem> items) {
        JPanel panel = new JPanel(new GridLayout(0, 3, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        for (MenuItem item : items) {
            String buttonText = "<html><center>"
                    + item.getName()
                    + "<br>"
                    + MoneyFormat.format(item.getPrice())
                    + "</center></html>";

            JButton button = new JButton(buttonText);

            button.addActionListener(
                    e -> listener.onItemSelected(item)
            );

            panel.add(button);
        }

        return panel;
    }
}