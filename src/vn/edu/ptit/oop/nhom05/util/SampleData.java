package vn.edu.ptit.oop.nhom05.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.edu.ptit.oop.nhom05.exception.EmptyOrderException;
import vn.edu.ptit.oop.nhom05.exception.InvalidPaymentException;
import vn.edu.ptit.oop.nhom05.model.*;


public class SampleData {
	public static List<Order> createOrders() {
	    List<Order> orders = new ArrayList<>();
	    Random rnd = new Random(42); // seed cố định -> biểu đồ giống nhau mỗi lần chạy

	    // Tách menu thành 3 nhóm từ MenuData.getAll()
	    List<Drink> drinks = new ArrayList<>();
	    List<Food> foods = new ArrayList<>();
	    List<Combo> combos = new ArrayList<>();
	    for (MenuItem m : MenuData.getAll()) {
	        if (m instanceof Drink) drinks.add((Drink) m);// instantof để xác định kiểu m phải là kiểu đang cần hay không và khi thêm phải ép kiểu lại
	        else if (m instanceof Food) foods.add((Food) m);
	        else if (m instanceof Combo) combos.add((Combo) m);
	    }

	    Drink.Size[] sizes = Drink.Size.values();
	    int total = 30;						//số dữ liệu mẫu muốn tạo
	    LocalDate today = LocalDate.now(); // lấy ngày hiện tại làm mốc

	    for (int i = 0; i < total; i++) {
	        LocalDate day = today.minusDays(1 + (i % 4));// trải đều 4 ngày gần nhất: hôm qua -> 4 ngày trước
	        int hour = 7 + rnd.nextInt(15);                   // Lấy giờ từ 7h đến (7+15) = 21
	        int minute = (hour == 21) ? 0 : rnd.nextInt(60);  // không vượt 21:00
	        LocalDateTime time = LocalDateTime.of(day, LocalTime.of(hour, minute));

	        Order order = new Order(String.format("HD%03d", i + 1),
	                                1 + rnd.nextInt(10), time);

	        order.addItem(newCup(randomOf(drinks, rnd), sizes[rnd.nextInt(sizes.length)]),
	        		1 + rnd.nextInt(2), null);// random  đồ uống và size, không note

	        
	        if (i % 3 == 0) order.addItem(randomOf(combos, rnd), 1, null);// cứ 3 đơn có 1 đơn gọi combo, còn lại gọi món ăn
	        else order.addItem(randomOf(foods, rnd), 1 + rnd.nextInt(2), null);// còn lại sẽ food và số lượng ngẫu nhiên
	        

	        
	 

	        // giảm giá cho 3/5 số đơn (i%5 = 0,1,2); các đơn còn lại không giảm
	        switch (i % 5) {
	            case 0: order.setDiscount(new PercentDiscount(10)); break; // chỉnh theo constructor thật
	            case 1: order.setDiscount(new MemberDiscount());    break;
	            case 2: order.setDiscount(new HappyHourDiscount()); break;
	            default: break;
	        }

	        payOrder(order);
	        orders.add(order);
	    }
	    return orders;
	}

	private static <T> T randomOf(List<T> list, Random rnd) {
	    return list.get(rnd.nextInt(list.size()));// Lấy random list (ngẫu nhiên số trong size
	}

	private static Drink newCup(Drink base, Drink.Size size) {// Tạo ly mới (object riêng) từ ly mẫu trong menu theo size được chọn.
	    return new Drink(base.getCode(), base.getName(), base.getBasePrice(), size);//trả lại dữ liệu thông tin của ly và size
	}

	private static void payOrder(Order order) {
		double cash = Math.ceil(order.getTotal() / 10_000) * 10_000;// Khách đưa tiền làm tròn lên 10.000đ
	    try {
	        order.pay(cash);
	    } catch (EmptyOrderException | InvalidPaymentException e) {// bọc exception checked thành RuntimeException.
	        throw new IllegalStateException("Không thể thanh toán đơn mẫu " + order.getId(), e);
	    }
	}
}
