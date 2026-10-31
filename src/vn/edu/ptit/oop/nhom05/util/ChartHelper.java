package vn.edu.ptit.oop.nhom05.util;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderItem;

public class ChartHelper {
	public static void applyVietnameseFont() {//bảo đảm không bị lỗi font trên chart
		StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
		theme.setExtraLargeFont(new Font("Segoe UI", Font.BOLD, 18)); // tiêu đề
		theme.setLargeFont(new Font("Segoe UI", Font.BOLD, 14)); // nhãn trục
		theme.setRegularFont(new Font("Segoe UI", Font.PLAIN, 12)); // chú giải, giá trị
		ChartFactory.setChartTheme(theme); // gọi MỘT lần trong main(), trước khi tạo biểu đồ
	}
	public static JFreeChart createTopItemsChart(List<Order> orders) {
	    // 1. Cộng số lượng theo tên gốc (getName), không có size -> các size gộp chung một món
	    Map<String, Integer> qtyByName = new HashMap<>();// set cập key-value cho món ăn và số lượng
	    for (Order order : orders) {
	        for (OrderItem it : order.getItems()) {
	            qtyByName.merge(it.getMenuItem().getName(), it.getQuantity(), Integer::sum);// cộng lại nếu có key(tên giống nhau)
	        }
	    }

	    // Sắp xếp giảm dần theo số lượng (bằng nhau thì theo tên cho ổn định)
	    List<Map.Entry<String, Integer>> sorted = new ArrayList<>(qtyByName.entrySet());
	    sorted.sort((a, b) -> {
	        int cmp = Integer.compare(b.getValue(), a.getValue()); // ưu tiên sort Value(số lượng)
	        return cmp != 0 ? cmp : a.getKey().compareTo(b.getKey()); // khi số lượng bằng thì sort key(tên)
	    });

	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();// Lấy 5 món đầu
	    for (int i = 0; i < Math.min(5, sorted.size()); i++) {
	        Map.Entry<String, Integer> e = sorted.get(i);
	        dataset.addValue(e.getValue(), "Số lượng", e.getKey());
	    }

	    
	    return ChartFactory.createBarChart(// Tạo biểu đồ cột
	            "Top 5 món bán chạy",   // tiêu đề
	            "Món",                  // trục X
	            "Số lượng",             // trục Y
	            dataset,
	            PlotOrientation.VERTICAL,
	            false,                  // legend: chỉ có một series nên tắt
	            true,                   // tooltips
	            false);                 // urls
	}
}
