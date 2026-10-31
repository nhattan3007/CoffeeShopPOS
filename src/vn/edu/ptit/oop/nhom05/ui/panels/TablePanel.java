package vn.edu.ptit.oop.nhom05.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;

import javax.swing.JPanel;

public class TablePanel extends JPanel {

    private static final int TABLE_COUNT = 10;
    private static final int COLUMNS = 5;

    private final boolean[] occupied = new boolean[TABLE_COUNT];

    // Bàn đang được chọn.
    private int selectedTable = -1;

    // Sự kiện khi người dùng chọn bàn.
    private IntConsumer onTableSelected;

    public TablePanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 320));

        // Cho phép chọn cả bàn trống và bàn đang phục vụ.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tableIndex = getTableAt(e.getX(), e.getY());

                if (tableIndex == -1) {
                    return;
                }

                int tableNo = tableIndex + 1;

                selectedTable = tableIndex;
                repaint();

                if (onTableSelected != null) {
                    onTableSelected.accept(tableNo);
                }
            }
        });
    }

    /**
     * Đăng ký sự kiện khi người dùng chọn bàn.
     */
    public void setOnTableSelected(IntConsumer listener) {
        this.onTableSelected = listener;
    }

    /**
     * Lấy số bàn đang được chọn.
     * Trả về -1 nếu chưa chọn bàn.
     */
    public int getSelectedTable() {
        return selectedTable == -1 ? -1 : selectedTable + 1;
    }

    /**
     * Cập nhật trạng thái bàn.
     */
    public void setTableOccupied(int tableNo, boolean isOccupied) {
        if (tableNo < 1 || tableNo > TABLE_COUNT) {
            throw new IllegalArgumentException("Số bàn không hợp lệ");
        }

        occupied[tableNo - 1] = isOccupied;
        repaint();
    }

    /**
     * Kiểm tra bàn có đang phục vụ hay không.
     */
    public boolean isTableOccupied(int tableNo) {
        if (tableNo < 1 || tableNo > TABLE_COUNT) {
            throw new IllegalArgumentException("Số bàn không hợp lệ");
        }

        return occupied[tableNo - 1];
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

    /**
     * Vẽ tiêu đề.
     */
    private void drawTitle(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        g2.drawString("SO DO BAN", 20, 35);
    }

    /**
     * Vẽ chú thích màu sắc.
     */
    private void drawLegend(Graphics2D g2) {
        int y = 55;

        g2.setFont(new Font("Arial", Font.PLAIN, 13));

        // Bàn trống.
        g2.setColor(new Color(76, 175, 80));
        g2.fillRoundRect(20, y, 18, 18, 5, 5);

        g2.setColor(Color.BLACK);
        g2.drawString("Bàn trống", 45, y + 14);

        // Bàn đang phục vụ.
        g2.setColor(new Color(244, 67, 54));
        g2.fillRoundRect(135, y, 18, 18, 5, 5);

        g2.setColor(Color.BLACK);
        g2.drawString("Đang phục vụ", 160, y + 14);
    }

    /**
     * Vẽ 10 bàn.
     */
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

            // Vẽ viền bàn.
            g2.setColor(tableColor.darker());
            g2.drawRoundRect(
                    x, y,
                    tableWidth, tableHeight,
                    18, 18
            );

            // Làm nổi bật bàn đang chọn.
            if (selectedTable == i) {
                g2.setColor(Color.BLUE);
                g2.drawRoundRect(
                        x - 3, y - 3,
                        tableWidth + 6, tableHeight + 6,
                        20, 20
                );
            }

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

            // Vẽ trạng thái bàn.
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            String status = occupied[i]
                    ? "Đang phục vụ"
                    : "Trống";

            int statusWidth = g2.getFontMetrics()
                    .stringWidth(status);

            g2.drawString(
                    status,
                    x + (tableWidth - statusWidth) / 2,
                    y + 52
            );
        }
    }

    /**
     * Xác định bàn được nhấn chuột.
     */
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
