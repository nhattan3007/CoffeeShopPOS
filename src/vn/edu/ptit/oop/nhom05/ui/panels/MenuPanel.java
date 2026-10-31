package vn.edu.ptit.oop.nhom05.ui.panels;

import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import vn.edu.ptit.oop.nhom05.model.Category;
import vn.edu.ptit.oop.nhom05.model.Drink;
import vn.edu.ptit.oop.nhom05.model.MenuData;
import vn.edu.ptit.oop.nhom05.model.MenuItem;
import vn.edu.ptit.oop.nhom05.util.MoneyFormat;

public class MenuPanel extends JPanel {

    private final MenuSelectionListener listener;

    public MenuPanel(MenuSelectionListener listener) {
        this.listener = listener;

        setLayout(new GridLayout(1, 1));

        JTabbedPane tabs = new JTabbedPane();

        // Tab hiển thị tất cả món.
        tabs.addTab("Tất cả", createMenuGrid(MenuData.getAll()));

        // Các tab theo danh mục.
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

            // Tên món và giá tiền.
            String buttonText = "<html><center>"
                    + item.getName()
                    + "<br>"
                    + MoneyFormat.format(item.getPrice())
                    + "</center></html>";

            JButton button = new JButton(buttonText);

            // Tìm icon theo mã món trong thư mục /icons.
            String iconPath = "/icons/" + item.getCode() + ".png";
            URL iconUrl = getClass().getResource(iconPath);

            // Nếu tìm thấy ảnh thì thu nhỏ và hiển thị.
            // Nếu không có ảnh thì bỏ qua, không gây lỗi.
            if (iconUrl != null) {
                ImageIcon originalIcon = new ImageIcon(iconUrl);

                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(36, 36, Image.SCALE_SMOOTH);

                button.setIcon(new ImageIcon(scaledImage));
                button.setHorizontalTextPosition(JButton.CENTER);
                button.setVerticalTextPosition(JButton.BOTTOM);
                button.setIconTextGap(8);
            }

            // Giữ chức năng chọn món.
            button.addActionListener(e -> handleItemSelection(item));

            panel.add(button);
        }

        return panel;
    }

    private void handleItemSelection(MenuItem item) {

        // Đồ ăn và combo không cần chọn size.
        if (item.getCategory() != Category.DRINK) {
            listener.onItemSelected(item);
            return;
        }

        // Tính giá theo từng size.
        double priceS = item.getBasePrice() * Drink.Size.S.getFactor();
        double priceM = item.getBasePrice() * Drink.Size.M.getFactor();
        double priceL = item.getBasePrice() * Drink.Size.L.getFactor();

        String[] options = {
                "S - " + MoneyFormat.format(priceS),
                "M - " + MoneyFormat.format(priceM),
                "L - " + MoneyFormat.format(priceL)
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Chọn size cho " + item.getName(),
                "Chọn size đồ uống",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Người dùng nhấn Hủy hoặc đóng hộp thoại.
        if (choice < 0 || choice >= options.length) {
            return;
        }

        Drink.Size size;

        switch (choice) {
            case 0:
                size = Drink.Size.S;
                break;
            case 1:
                size = Drink.Size.M;
                break;
            case 2:
                size = Drink.Size.L;
                break;
            default:
                return;
        }

        // Tạo đồ uống mới với size đã chọn.
        Drink selectedDrink = new Drink(
                item.getCode(),
                item.getName(),
                item.getBasePrice(),
                size
        );

        listener.onItemSelected(selectedDrink);
    }
}