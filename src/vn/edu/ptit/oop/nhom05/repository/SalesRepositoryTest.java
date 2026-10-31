package vn.edu.ptit.oop.nhom05.repository;

import vn.edu.ptit.oop.nhom05.model.Food;
import vn.edu.ptit.oop.nhom05.model.Order;

public class SalesRepositoryTest {
	public static void main(String[] args) throws Exception {
	    SalesRepository repo = new SalesRepository();
	    Order o = new Order("T1", 1);
	    o.addItem(new Food("F01", "Bánh mì", 25000), 2, "");
	    o.pay(100000);
	    repo.add(o);
	    System.out.println(repo.findAll().size());   // phải ra 1
	    System.out.println(repo.totalRevenue());     // phải ra 50000.0
	    try {
	        repo.add(new Order("T2", 1));            // đơn chưa trả tiền
	    } catch (IllegalArgumentException e) {
	        System.out.println("Lỗi đúng: " + e.getMessage());
	    }
	}
}
