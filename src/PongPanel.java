import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PongPanel extends JPanel implements ActionListener, KeyListener {

	private boolean titleScreen = true;
	private boolean playingAI = false;
	private boolean playing = false;
	private boolean gameOver = false;

	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean wPressed = false;
	private boolean sPressed = false;

	private int ballX = 250;
	private int ballY = 250;
	private int diameter = 20;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;

	private int playerOneX = 25;
	private int playerOneY = 250;
	private int playerOneWidth = 10;
	private int playerOneHeight = 50;

	private int playerTwoX = 465;
	private int playerTwoY = 250;
	private int playerTwoWidth = 10;
	private int playerTwoHeight = 50;

	private int paddleSpeed = 5;
	private double paddleSpeedAI = 5.5;

	private int winningScore = 5;
	private int playerOneScore = 0;
	private int playerTwoScore = 0;
	private String recentScore = "";
	private int playerVictory = 1;

	// construct a PongPanel
	public PongPanel() {
		setBackground(Color.BLACK);

		// listen to a key pressed
		setFocusable(true);
		addKeyListener(this);

		// call step() 60 fps
		Timer timer = new Timer(1000 / 120, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		step();
	}

	public void step() {

		if (playing) {
			if (upPressed) {
				if (playerOneY - paddleSpeed > 0)
					playerOneY -= paddleSpeed;
			} else if (downPressed) {
				if (playerOneY + playerOneHeight + paddleSpeed < getHeight())
					playerOneY += paddleSpeed;
			}
			if (!playingAI) {
				if (wPressed) {
					if (playerTwoY - paddleSpeed > 0)
						playerTwoY -= paddleSpeed;
				} else if (sPressed) {
					if (playerTwoY + playerTwoHeight + paddleSpeed < getHeight())
						playerTwoY += paddleSpeed;
				}
			}

			// where will the ball be after it moves?
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			int playerTwoLeft = playerTwoX;
			int playerTwoTop = playerTwoY;
			int playerTwoBottom = playerTwoY + playerTwoHeight;

			if (playingAI) {
				Random ran = new Random();
				int value = ran.nextInt(99) + 1;
				if (!(value % 3 == 0)) {
					if (nextBallTop < playerTwoTop && playerTwoTop - paddleSpeedAI > 0) {
						playerTwoY -= paddleSpeed - 1;
					}
					if (nextBallBottom > playerTwoBottom && playerTwoBottom + paddleSpeedAI < getHeight()) {
						playerTwoY += paddleSpeedAI;
					}
				}
			}
			// ball bounces off top and bottom of screen
			if (nextBallTop < 0 || nextBallBottom > getHeight()) {
				ballDeltaY *= -1;
			}

			// ball bounces off the left side
			if (nextBallLeft < playerOneRight) {
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {
					System.out.print("Player Two SCORED");
					playerTwoScore++;
					if (playerTwoScore >= winningScore) {
						playerVictory = 2;
						playing = false;
						gameOver = true;
					}
					recentScore = "P2 has Scored!";
					ballX = 250;
					ballY = 250;
				} else {
					ballDeltaX *= -1;
				}
			}

			// if (nextBallLeft < 0) {
			// ballDeltaX *= -1;
			// }

			// ball bounces off the right side
			if (nextBallRight > playerTwoLeft) {
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {
					System.out.println("Player One SCORED");
					playerOneScore++;
					if (playerOneScore >= winningScore) {
						playerVictory = 1;
						playing = false;
						gameOver = true;
					}
					recentScore = "P1 has Scored!";
					ballX = 250;
					ballY = 250;
				} else {
					ballDeltaX *= -1;
				}
			}
			// if (nextBallRight > getWidth()) {
			// ballDeltaX *= -1;
			// }

			ballX += ballDeltaX;
			ballY += ballDeltaY;
		}

		// stuff has moved, so tell the JPanel to repaint itself
		repaint();

	}

	// paint a ball
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.setColor(Color.WHITE);
		if (titleScreen) {
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 72));
			g.drawString("Pong", 150, 200);
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
			g.drawString("Press p to play.", 160, 300);
			g.drawString("Press Enter to play against AI", 85, 350);
		} else if (playing) {
			// goal lines
			g.drawLine(playerOneX + playerOneWidth, 0, playerOneX + playerOneWidth, getHeight());
			g.drawLine(playerTwoX, 0, playerTwoX, getHeight());

			// center dotted line
			for (int lineY = 0; lineY < getHeight(); lineY += 75) {
				g.drawLine(250, lineY + 15, 250, lineY + 35);
			}
			if (!(recentScore == "")) {
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
				g.drawString(recentScore, 200, 400);
			}
			// print score
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 150, 250);
			g.drawString(String.valueOf(playerTwoScore), 350, 250);

			g.fillOval(ballX, ballY, diameter, diameter);
			g.fillRect(playerOneX, playerOneY, playerOneWidth, playerOneHeight);
			g.fillRect(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight);
		} else if (gameOver) {
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 72));
			if (playerVictory == 1) {
				g.drawString("P1 Wins!", 75, 200);
			} else if (playerVictory == 2) {
				g.drawString("P2 Wins!", 75, 200);
			}
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
			g.drawString("Press space to restart", 115, 300);
			g.drawString("Press esc to exit", 140, 375);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (titleScreen) {
			if (e.getKeyCode() == KeyEvent.VK_P) {
				titleScreen = false;
				playing = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				titleScreen = false;
				playing = true;
				playingAI = true;
			}
		}
		if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}
		}
		if (gameOver) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				gameOver = false;
				playing = true;
				ballX = 250;
				ballY = 250;
				playerOneY = 250;
				playerTwoY = 250;
				playerOneScore = 0;
				playerTwoScore = 0;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}