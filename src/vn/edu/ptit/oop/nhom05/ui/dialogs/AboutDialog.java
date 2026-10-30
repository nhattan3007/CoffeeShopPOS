package vn.edu.ptit.oop.nhom05.ui.dialogs;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutDialog extends JDialog{
	public AboutDialog(JFrame parent) {
		super(parent,"Giới thiệu",true);
		//xếp dọc
		setLayout(new GridLayout(0,1));
		//tạo chữ
		JLabel lb1=new JLabel("Nhóm 05 – Coffee Shop POS");
		JLabel lb2=new JLabel("Môn INT1332 – Lập trình hướng đối tượng");
		JLabel lb3=new JLabel("Hà Tuấn Đạt");
		JLabel lb4=new JLabel("Nguyễn Đức Nhật");
		JLabel lb5=new JLabel("Nguyễn Minh Sơn");
		JLabel lb6=new JLabel("Trần Nhật Tân");
		
		//đưa chữ vào 
		add(lb1);
		add(lb2);
		add(lb3);
		add(lb4);
		add(lb5);
		add(lb6);
		
		//Tạo nút
		JButton b=new JButton("Đóng");
		add(b);
		//xử lý nút
		b.addActionListener(e->dispose());
		//điều chỉnh nút
		pack();
		setLocationRelativeTo(parent);
	}
}
