package vn.edu.ptit.oop.nhom05.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.edu.ptit.oop.nhom05.model.Order;
import vn.edu.ptit.oop.nhom05.model.OrderStatus;

public class SalesRepository {
	private List<Order> orders=new ArrayList<>();
	public void add(Order paid) {
		if(paid==null||paid.getStatus()!=OrderStatus.PAID) {
			throw new IllegalArgumentException();
		}
		orders.add(paid);
	}
	public List<Order> findAll(){
		return  Collections.unmodifiableList(orders);
	}
	public List<Order> findByDate(LocalDate d){
		List<Order>result=new ArrayList<>();
		for (Order order : orders) {
			if(order.getCreatedAt().toLocalDate().equals(d)) {
				result.add(order);
			}
		}
		return result;
	}
	public double totalRevenue() {
		double total=0;
		for (Order order : orders) {
			total+=order.getTotal();
		}
		return total;
	}
}
