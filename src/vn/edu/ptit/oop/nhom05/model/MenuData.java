// ===== model/MenuData.java =====
package vn.edu.ptit.oop.nhom05.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Dữ liệu menu mẫu của quán. final + constructor private: lớp tiện ích, không ai new được.
public final class MenuData {
    private static final List<MenuItem> ALL = new ArrayList<>();

    // static block: chạy một lần duy nhất khi lớp được nạp lần đầu
    static {
        // ĐỒ UỐNG: giá gốc = giá size S; size M, L nhân hệ số trong Drink.Size
        ALL.add(new Drink("D01", "Cà phê đen", 20000, Drink.Size.S));
        ALL.add(new Drink("D02", "Cà phê sữa", 25000, Drink.Size.S));
        ALL.add(new Drink("D03", "Bạc xỉu", 28000, Drink.Size.S));
        ALL.add(new Drink("D04", "Trà đào", 32000, Drink.Size.S));
        ALL.add(new Drink("D05", "Trà sữa trân châu", 35000, Drink.Size.S));
        ALL.add(new Drink("D06", "Sinh tố bơ", 40000, Drink.Size.S));

        // ĐỒ ĂN
        ALL.add(new Food("F01", "Bánh mì thịt", 25000));
        ALL.add(new Food("F02", "Bánh croissant", 30000));
        ALL.add(new Food("F03", "Tiramisu", 38000));
        ALL.add(new Food("F04", "Khoai tây chiên", 28000));
        ALL.add(new Food("F05", "Mì Ý sốt bò", 55000));

        // COMBO (mẫu Composite): mỗi combo dùng đối tượng món RIÊNG, không dùng chung với danh sách trên
        Combo sang = new Combo("C01", "Combo sáng", 0.10);
        sang.add(new Food("F01", "Bánh mì thịt", 25000));
        sang.add(new Drink("D02", "Cà phê sữa", 25000, Drink.Size.M));
        ALL.add(sang);

        Combo chieu = new Combo("C02", "Combo chiều", 0.15);
        chieu.add(new Food("F03", "Tiramisu", 38000));
        chieu.add(new Drink("D04", "Trà đào", 32000, Drink.Size.M));
        ALL.add(chieu);
    }

    private MenuData() { }

    /** Toàn bộ menu (chỉ đọc). */
    public static List<MenuItem> getAll() { return Collections.unmodifiableList(ALL); }

    /** Lọc theo danh mục: trả về danh sách mới. */
    public static List<MenuItem> getByCategory(Category category) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem m : ALL) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }
}