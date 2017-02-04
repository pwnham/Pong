import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) {

		JFrame frame = new JFrame("Pong");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		PongPanel pongPanel = new PongPanel();
		frame.add(pongPanel, BorderLayout.CENTER);

		frame.setSize(500, 500);
		frame.setVisible(true);

	}
}
