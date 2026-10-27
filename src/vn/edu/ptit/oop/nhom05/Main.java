package vn.edu.ptit.oop.nhom05;

import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom05.ui.MainWindow;

public class Main {
	public static void main(String[] args) {
// Mọi thao tác Swing phải chạy trên Event Dispatch Thread (EDT)
		SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
	}
}