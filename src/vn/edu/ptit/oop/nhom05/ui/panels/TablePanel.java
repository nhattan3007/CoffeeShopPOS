package vn.edu.ptit.oop.nhom05.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TablePanel extends JPanel {

    private static final int TABLE_COUNT = 10;
    private static final int COLUMNS = 5;

    private final boolean[] occupied = new boolean[TABLE_COUNT];

    public TablePanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 320));

        // Tạo dữ liệu mẫu: bàn 2, 5 và 8 đang được sử dụng.
        occupied[1] = true;
        occupied[4] = true;
        occupied[7] = true;

        // Xử lý khi nhấn vào một bàn.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tableIndex = getTableAt(e.getX(), e.getY());

                if (tableIndex == -1) {
                    return;
                }

                occupied[tableIndex] = !occupied[tableIndex];

                repaint();

                String status = occupied[tableIndex]
                        ? "Đang phục vụ"
                        : "Trống";

                JOptionPane.showMessageDialog(
                        TablePanel.this,
                        "Bàn " + (tableIndex + 1)
                                + "\nTrạng thái: " + status,
                        "Thông tin bàn",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            drawTitle(g2);
            drawLegend(g2);
            drawTables(g2);
        } finally {
            g2.dispose();
        }
    }

    // Vẽ tiêu đề.
    private void drawTitle(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        g2.drawString("SO DO BAN", 20, 35);
    }

    // Vẽ chú thích màu sắc.
    private void drawLegend(Graphics2D g2) {
        int y = 55;

        g2.setFont(new Font("Arial", Font.PLAIN, 13));

        g2.setColor(new Color(76, 175, 80));
        g2.fillRoundRect(20, y, 18, 18, 5, 5);

        g2.setColor(Color.BLACK);
        g2.drawString("Bàn trống", 45, y + 14);

        g2.setColor(new Color(244, 67, 54));
        g2.fillRoundRect(135, y, 18, 18, 5, 5);

        g2.setColor(Color.BLACK);
        g2.drawString("Đang phục vụ", 160, y + 14);
    }

    // Vẽ 10 bàn bằng Graphics2D.
    private void drawTables(Graphics2D g2) {
        int startX = 20;
        int startY = 100;

        int tableWidth = 110;
        int tableHeight = 75;

        int gapX = 20;
        int gapY = 25;

        for (int i = 0; i < TABLE_COUNT; i++) {
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            int x = startX + col * (tableWidth + gapX);
            int y = startY + row * (tableHeight + gapY);

            Color tableColor = occupied[i]
                    ? new Color(244, 67, 54)
                    : new Color(76, 175, 80);

            // Vẽ nền bàn.
            g2.setColor(tableColor);
            g2.fillRoundRect(
                    x, y,
                    tableWidth, tableHeight,
                    18, 18
            );

            // Vẽ viền.
            g2.setColor(tableColor.darker());
            g2.drawRoundRect(
                    x, y,
                    tableWidth, tableHeight,
                    18, 18
            );

            // Vẽ số bàn.
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            String tableName = "Bàn " + (i + 1);

            int textWidth = g2.getFontMetrics()
                    .stringWidth(tableName);

            g2.drawString(
                    tableName,
                    x + (tableWidth - textWidth) / 2,
                    y + 30
            );

            // Vẽ trạng thái.
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            String status = occupied[i] ? "Đang phục vụ" : "Trống";

            int statusWidth = g2.getFontMetrics()
                    .stringWidth(status);

            g2.drawString(
                    status,
                    x + (tableWidth - statusWidth) / 2,
                    y + 52
            );
        }
    }

    // Xác định người dùng vừa nhấn vào bàn nào.
    private int getTableAt(int mouseX, int mouseY) {
        int startX = 20;
        int startY = 100;

        int tableWidth = 110;
        int tableHeight = 75;

        int gapX = 20;
        int gapY = 25;

        for (int i = 0; i < TABLE_COUNT; i++) {
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            int x = startX + col * (tableWidth + gapX);
            int y = startY + row * (tableHeight + gapY);

            if (mouseX >= x
                    && mouseX <= x + tableWidth
                    && mouseY >= y
                    && mouseY <= y + tableHeight) {
                return i;
            }
        }

        return -1;
    }
}