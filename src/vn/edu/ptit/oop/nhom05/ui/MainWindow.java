package vn.edu.ptit.oop.nhom05.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import vn.edu.ptit.oop.nhom05.common.BaseWindow;

public class MainWindow extends BaseWindow {

	public MainWindow() {
		super("Nhóm 05 – Coffee Shop POS", 1100, 700);
	}

	@Override
	protected void initUI() {
		// Mọi field khởi tạo ở đây, KHÔNG gán "= new ..." ở chỗ khai báo (bẫy của
		// BaseWindow)
		add(new JLabel("Coffee Shop POS – khung dự án đã chạy được", SwingConstants.CENTER), BorderLayout.CENTER);
		setStatus("Khung dự án đã sẵn sàng");
	}
}