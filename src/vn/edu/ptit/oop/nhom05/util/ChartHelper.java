package vn.edu.ptit.oop.nhom05.util;

import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import vn.edu.ptit.oop.nhom05.model.Category;
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
	public static JFreeChart createTopItemsChart(List<Order> orders) {// tạo biểu đồ cột theo món ăn
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
	        dataset.addValue(e.getValue(), "Số lượng", e.getKey());//set giá trị data 
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
	public static JFreeChart createRevenueByCategoryChart(List<Order> orders) {// Tạo biểu đồ tròn theo loại hàng
	    // Cộng doanh thu theo danh mục; khởi tạo 0 cho mọi danh mục để luôn đủ lát
	    Map<Category, Double> revenue = new EnumMap<>(Category.class);
	    for (Category c : Category.values()) {
	        revenue.put(c, 0.0);
	    }
	    for (Order order : orders) {
	        for (OrderItem it : order.getItems()) {
	            revenue.merge(it.getMenuItem()
	            		.getCategory(), it.getLineTotal(), Double::sum);// Hợp mảng hống kê loại, tổng tiền đơn món, thành tiền
	        }
	    }

	    // Đưa vào dataset, khóa là nhãn hiển thị (getLabel())
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    for (Map.Entry<Category, Double> e : revenue.entrySet()) {
	        dataset.setValue(e.getKey().getLabel(), e.getValue());
	    }

	    // 3. Tạo biểu đồ tròn
	    JFreeChart chart = ChartFactory.createPieChart(
	            "Doanh thu theo danh mục",
	            dataset,
	            true,    // legend
	            true,    // tooltips
	            false);  // urls

	    // 4. Nhãn lát: "Đồ uống: 52.3%" (1 chữ số thập phân để tổng hiển thị sát 100%)
	    PiePlot<?> plot = (PiePlot<?>) chart.getPlot();// ép kiểu sang biểu đồ tròn
	    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
	            "{0}: {2}",				// hiển thị Category và Sum(tổng doanh thu của loại)
	            new DecimalFormat("#,##0"),//định dạng lại số liệu
	            new DecimalFormat("0.0%")));
	    return chart;
	}
	public static JFreeChart createRevenueByHourChart(List<Order> orders) {// Tạo biểu đồ đường theo giờ
	    final int FIRST_HOUR = 7;
	    final int LAST_HOUR = 21;                       //lấy giờ từ 7 -> 21h

	    //  Mảng doanh thu theo giờ, mặc định 0 cho giờ không có đơn
	    double[] revenue = new double[LAST_HOUR - FIRST_HOUR + 1];
	    for (Order order : orders) {
	        int hour = order.getCreatedAt().getHour();//lấy giờ của hóa đơn
	        if (hour >= FIRST_HOUR && hour <= LAST_HOUR) {
	            revenue[hour - FIRST_HOUR] += order.getTotal();// Thống kê tổng doạnh thu trong giờ cụ thể
	        }
	    }

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();// Đưa vào dataset theo thứ tự giờ tăng dần
	    for (int h = FIRST_HOUR; h <= LAST_HOUR; h++) {					//lấy từ 7-21
	        dataset.addValue(revenue[h - FIRST_HOUR], "Doanh thu", h + "h");// dataset dữ liệu từ khoảng tg này
	    }

	    JFreeChart chart = ChartFactory.createLineChart(// Biểu đồ đường
	            "Doanh thu theo giờ",
	            "Giờ trong ngày",
	            "Doanh thu (VND)",
	            dataset,
	            PlotOrientation.VERTICAL,
	            false,   // một series nên tắt legend
	            true,    // tooltips
	            false);  // urls

	    // 4. Hiển thị: chấm tròn ở mỗi điểm, trục Y có dấu phân cách hàng nghìn
	    CategoryPlot plot = chart.getCategoryPlot(); 	// Lấy vùng hiển thị dữ liệu của biểu đồ (CategoryPlot)
	    LineAndShapeRenderer renderer = 				// Lấy bộ điều khiển cách vẽ đường và các điểm dữ liệu
	    		(LineAndShapeRenderer) plot.getRenderer();// Ép kiểu sang LineAndShapeRenderer để sử dụng các chức năng riêng của biểu đồ đường
	    renderer.setDefaultShapesVisible(true);			// Hiển thị hình dạng tại mỗi điểm dữ liệu (mặc định thường là các chấm nhỏ)
	    ((NumberAxis) plot.getRangeAxis())				// Ép kiểu sang NumberAxis để có thể tùy chỉnh cách hiển thị số
	    .setNumberFormatOverride(new DecimalFormat("#,##0"));// Định dạng lại kiểu dữ liệu
	    return chart;
	}
}
