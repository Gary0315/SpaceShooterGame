package tw.gary.space;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Space_Shooter_panel extends JPanel {
	private int SCREEN_HEIGHT = 800; // 遊戲畫面高度
	private int SCREEN_WIDTH = 996; // 遊戲畫面寬度
	private Timer timer, timer1;
	private Space_Shooter_enemy enemy;
	private LinkedList<Space_Shooter_enemy> enemys;
	private Space_Shooter_bullet bullet, e_bullet, b_bullet;
	private LinkedList<Space_Shooter_bullet> bullets, e_bullets, b_bullets;
	private int enemy_x, enemy_y; // 初始化敵人位置
	private int myPlane_x = SCREEN_WIDTH / 2 - 65, myPlane_y = SCREEN_HEIGHT - 130;// 玩家座標
	public static Graphics g;
	private Space_Shooter_player player;
	private boolean isPress01, isPress02, isPress03, isPress04; // 上下左右判斷布林值
	private int step = 7;// 飛機速度
	private Space_Shooter_collide collide;
	private Space_Shooter_Boss boss;
	private BufferedImage background, background1;
	private BufferedImage[] xues = new BufferedImage[21]; // 血量條圖片
	private int dy; // 背景圖片位移
	public boolean running; // 遊戲是否進行中
	public boolean isPause; // 暫停是否進行中
	public boolean Ranking; // 排行榜是否進行中
	private int enemyNums = 12; // 敵人數量
	public static int score = 0; // 分數
	public static int life = 100;// 我方生命
	public static int life1 = 1000;// BOSS生命
	private int gameTime = 0;// 遊戲進行時間
	private int dtime = 1;
	private Thread t1;

	Space_Shooter_panel() {
		try {
			background = ImageIO.read(new File("images\\mapback.png"));
			background1 = ImageIO.read(new File("images\\mapback5.png"));
			for (int i = 1; i <= 21; i++) {
				// 讀取血量圖片
				xues[i - 1] = ImageIO.read(new File("images\\xue_" + i + ".png"));
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		timer = new Timer();
		timer1 = new Timer();
		timer.schedule(new FrameTask(), 100, 16); // 畫面更新
		timer.schedule(new moveBg(), 100, 16);// 移動背景圖
		timer.schedule(new BulletTimer(), 1000, 2300);// 敵方子彈時間
		timer.schedule(new gameTimer(), 0, 1000);// 遊戲進行時間

		player = new Space_Shooter_player(myPlane_x, myPlane_y);
		bullet = new Space_Shooter_bullet(-100, 0);

		bullets = new LinkedList<Space_Shooter_bullet>();
		b_bullets = new LinkedList<Space_Shooter_bullet>();
		enemys = new LinkedList<Space_Shooter_enemy>();
		e_bullets = new LinkedList<Space_Shooter_bullet>();
		timer1.schedule(new EnemyTimer(), 0, 1000);// 敵人出現間隔
		collide = new Space_Shooter_collide();
		boss = new Space_Shooter_Boss();
		t1 = new warning_sound();

		this.setFocusable(true);
		this.addKeyListener(new myKeyadapter());
	}

	// 暫停
	public void pauseGame() {
		if (isPause) {
			isPause = false;
			dy = 0;
			dtime = 1;
		} else {
			isPause = true;
			dtime = 0;
		}
	}

	// 重新開始
	public void restartGame() {
		running = false;
		isPause = false;
		Ranking = false;
		player.stayed = true;
		boss.stay = false;
		life = 100;
		life1 = 2000;
		score = 0;
		gameTime = 0;
		dtime = 1;
		bullets.clear();
		enemys.clear();
		e_bullets.clear();
		b_bullets.clear();
		myPlane_x = SCREEN_WIDTH / 2 - 65;
		myPlane_y = SCREEN_HEIGHT - 130;
		player = new Space_Shooter_player(myPlane_x, myPlane_y);
		t1 = new warning_sound();
		isPress01 = false;
		isPress02 = false;
		isPress03 = false;
		isPress04 = false;
		boss = new Space_Shooter_Boss();
		Space_Shooter_Frame.start.setEnabled(true);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	private void draw(Graphics g) {
		// 畫出動態背景
		g.drawImage(background, 0, 0 + dy, 1000, 800, null);
		g.drawImage(background, 0, -800 + dy, 1000, 800, null);
		if (running && !isPause) {
			Space_Shooter_panel.g = g;
			// 畫出敵人
			for (int i = 0; i < enemys.size(); i++) {
				if (enemys.get(i) != null) {
					enemys.get(i).DrawEnemy(g, i % 5);
					if (enemys.get(i).getId() == 0) {
						enemys.get(i).tick();
					} else if (enemys.get(i).getId() != 0) {
						enemys.get(i).EnemyY = -500;
					}

				} else if (enemys.get(i) == null) {
					continue;
				}

			}

			// 畫出敵機子彈 以及消失
			for (int i = 0; i < e_bullets.size(); i++) {
				if (e_bullets.get(i) != null) {
					e_bullets.get(i).drawBullet_E(g);
					e_bullets.get(i).tick_E();
					collide.bullet_plane(e_bullets.get(i), player);
					if (e_bullets.get(i).getB_ye() > SCREEN_HEIGHT || !e_bullets.get(i).stay) {
						e_bullets.remove(i);
					}
				} else if (enemys.get(i) == null) {
					continue;
				}
			}
//畫出 BOSS 子彈
			for (int i = 0; i < b_bullets.size(); i++) {
				if (b_bullets.get(i) != null) {
					b_bullets.get(i).drawBullet_B(g);
					b_bullets.get(i).tick_B();
					collide.boss_plane(b_bullets.get(i), player);
					if (b_bullets.get(i).getB_yb1() > SCREEN_HEIGHT || b_bullets.get(i).getB_yb2() > SCREEN_HEIGHT
							|| b_bullets.get(i).getB_yb3() > SCREEN_HEIGHT
							|| b_bullets.get(i).getB_yb4() > SCREEN_HEIGHT
							|| b_bullets.get(i).getB_yb5() > SCREEN_HEIGHT || !b_bullets.get(i).stay) {
						b_bullets.remove(i);
					}
				}
			}

			// 畫出我方飛機
			if (player.getId() <= 29) {
				player.drawMyplane(g);
			}
			// 我方飛機移動
			move();

			// 畫出子彈 移動 及 消失
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).drawBullet(g);
				bullets.get(i).tick();
				if (bullets.get(i).get_Y() < 0 || bullets.get(i).stay == false) {
					bullets.remove(i);
				}
			}

			// 擊中後更改子彈跟敵機屬性
			for (int j = 0; j < bullets.size(); j++) {
				for (int i = 0; i < enemys.size(); i++) {
					if (enemys.get(i) != null) {
						if (bullets.size() != 0) {
							collide.bullet_enemy(bullets.get(j), enemys.get(i));
						}

					}
				}
			}
			// 子彈擊中 BOSS
			for (int i = 0; i < bullets.size(); i++) {
				if (bullets.get(i) != null && boss.getBoss_y() > 20) {
					collide.bullet_boss(bullets.get(i), boss);
				}

			}

			// BOSS撞到我方飛機
			collide.boss_plane(boss, player);

			// 敵人飛機與我方飛機碰撞
			for (int i = 0; i < enemys.size(); i++) {
				if (enemys.get(i) != null) {
					collide.plane_enemy(player, enemys.get(i));
				}

			}
			// WARNING 出現時機
			if (gameTime == 16 || gameTime == 18 || gameTime == 20) {
				g.setFont(new Font("Ink Free", Font.BOLD, 170));
				g.setColor(Color.RED);
				g.drawString("WARNING!!", 40, 440);
			}

			// 擊中敵人 增加分數 跟 敵機消失
			for (int i = 0; i < enemys.size(); i++) {
				if (enemys.get(i) != null) {
					if (enemys.get(i).stay == false && enemys.get(i).getId() == 29) {
						score += 100;
					}
					if (enemys.get(i).getEnemyY() > SCREEN_HEIGHT
							|| (enemys.get(i).stay == false && enemys.get(i).getId() == 29)) {
						enemys.set(i, null);
					}

				} else if (enemys.get(i) == null) {
					continue;
				}

			}

			// RANK
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.BLUE);
			g.drawString("SCORE:" + score, 780, 40);

			// 畫出 我方血量圖
			switch (life) {
			case 100:
				g.drawImage(xues[20], 30, 30, 230, 25, null);
				break;
			case 90:
				g.drawImage(xues[18], 30, 30, 230, 25, null);
				break;
			case 80:
				g.drawImage(xues[16], 30, 30, 230, 25, null);
				break;
			case 70:
				g.drawImage(xues[14], 30, 30, 230, 25, null);
				break;
			case 60:
				g.drawImage(xues[12], 30, 30, 230, 25, null);
				break;
			case 50:
				g.drawImage(xues[10], 30, 30, 230, 25, null);
				break;
			case 40:
				g.drawImage(xues[8], 30, 30, 230, 25, null);
				break;
			case 30:
				g.drawImage(xues[6], 30, 30, 230, 25, null);
				break;
			case 20:
				g.drawImage(xues[4], 30, 30, 230, 25, null);
				break;
			case 10:
				g.drawImage(xues[2], 30, 30, 230, 25, null);
				break;
			case 0:
				g.drawImage(xues[0], 30, 30, 230, 25, null);
				break;
			}

			// 畫出BOSS血量圖
			if (boss.getBoss_y() > 20) {
				switch (life1) {
				case 1000:
					g.drawImage(xues[0], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 950:
					g.drawImage(xues[1], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 900:
					g.drawImage(xues[2], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 850:
					g.drawImage(xues[3], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 800:
					g.drawImage(xues[4], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 750:
					g.drawImage(xues[5], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 700:
					g.drawImage(xues[6], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 650:
					g.drawImage(xues[7], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 600:
					g.drawImage(xues[8], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 550:
					g.drawImage(xues[9], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 500:
					g.drawImage(xues[10], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 450:
					g.drawImage(xues[11], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 400:
					g.drawImage(xues[12], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 350:
					g.drawImage(xues[13], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 300:
					g.drawImage(xues[14], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 250:
					g.drawImage(xues[15], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 200:
					g.drawImage(xues[16], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 150:
					g.drawImage(xues[17], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 100:
					g.drawImage(xues[18], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 50:
					g.drawImage(xues[19], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				case 0:
					g.drawImage(xues[20], boss.getBoss_x() + 100, boss.getBoss_y() - 10, 330, 12, null);
					break;
				}
			}

			if (gameTime > 20 && boss.getId() <= 119) {
				boss.drawBoss(g);
				if (boss.getId() == 0) {
					boss.bossMove();
				} else if (boss.getId() != 0) {
					boss.setBoss_y(-1000);
				}
			}
			// 遊戲結束背景
		} else if (!running && life > 0 && boss.getId() == 0 && !Ranking && player.getId() == 0) {
			g.drawImage(background1, 0, 0, 1000, 800, null);
			// 暫停背景
		} else if (running && isPause) {
			g.drawImage(background, 0, 0, 1000, 800, null);
			// 畫出排行榜
		} else if (!running && Ranking) {
			try {
				new Update_Save().drawRank(g);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		// 我方與BOSS爆炸後結束遊戲判斷
		if (running && player.getId() == 29) {
			running = false;
			gameOver();
		} else if (life1 <= 0 && running && boss.getId() == 119) {
			running = false;
			winGame();
		}

	}

//遊戲結束跳出視窗
	public void gameOver() {
		try {
			new Update_Save().updateScore(score);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		Object[] obj = { "restart" };

		if (JOptionPane.showOptionDialog(null, "score:" + score, "YOU LOSE !", JOptionPane.OK_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, obj, obj[0]) == JOptionPane.OK_OPTION) {
			restartGame();
		}
	}

	// 遊戲獲勝跳出視窗
	public void winGame() {
		try {
			new Update_Save().updateScore(score);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		Object[] obj = { "restart" };
		if (JOptionPane.showOptionDialog(null, "score:" + score, "YOU WIN !", JOptionPane.OK_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, obj, obj[0]) == JOptionPane.OK_OPTION) {
			restartGame();
		}
	}

	// 遊戲更新率
	public class FrameTask extends TimerTask {
		@Override
		public void run() {
			repaint();
		}
	}

	// 我方飛機移動
	private void move() {
		if (isPress01 && player.get_X() >= (996 - Space_Shooter_player.PLANE_SIZE)) {
			isPress01 = false;
		} else if (isPress01) {
			player.setPlane_x(player.get_X() + step);
		}
		if (isPress02 && player.get_X() <= 0) {
			isPress02 = false;
		} else if (isPress02) {
			player.setPlane_x(player.get_X() - step);
		}
		if (isPress03 && player.get_Y() <= 0) {
			isPress03 = false;
		} else if (isPress03) {
			player.setPlane_y(player.get_Y() - step);
		}
		if (isPress04 && player.get_Y() >= (800 - 130)) {
			isPress04 = false;
		} else if (isPress04) {
			player.setPlane_y(player.get_Y() + step);
		}
	}

	// 畫面截圖功能
	public void screenShot() {
		BufferedImage saveImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = saveImg.createGraphics();
		paint(g2d);
		try {
			ImageIO.write(saveImg, "png", new File("dir1/gary.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	// 按鍵事件 改變上下左右的布林值
	public class myKeyadapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				isPress01 = true;
				break;
			case KeyEvent.VK_LEFT:
				isPress02 = true;
				break;

			case KeyEvent.VK_UP:
				isPress03 = true;
				break;
			case KeyEvent.VK_DOWN:
				isPress04 = true;
				break;
			case KeyEvent.VK_SPACE:
				break;
			case KeyEvent.VK_P:
				pauseGame();
				Object[] obj = { "resume" };
				if (JOptionPane.showOptionDialog(null, "pause", "message", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, obj, obj[0]) == JOptionPane.YES_OPTION) {
					pauseGame();
				} else {
					restartGame();
				}
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				isPress01 = false;
				break;
			case KeyEvent.VK_LEFT:
				isPress02 = false;
				break;
			case KeyEvent.VK_UP:
				isPress03 = false;
				break;
			case KeyEvent.VK_DOWN:
				isPress04 = false;
				break;
			case KeyEvent.VK_SPACE:
				bullet = new Space_Shooter_bullet(player.get_X() + (Space_Shooter_player.PLANE_SIZE / 2 - 12),
						player.get_Y() - 27);
				bullets.add(bullet);
				new shoot_sound().start();
				break;
			}
		}
	}

	// 敵人更新TIMERTASK
	class EnemyTimer extends TimerTask {
		@Override
		public void run() {
			if (gameTime < 11 && gameTime > 0) {
				if (enemys.size() < enemyNums) {
					enemy_x = (int) (Math.random() * 880);
					enemy = new Space_Shooter_enemy(enemy_x, enemy_y);
					enemys.add(enemy);
				}
				for (int i = 0; i < enemys.size(); i++) {
					if (enemys.get(i) == null) {
						enemy_x = (int) (Math.random() * 880);
						enemy = new Space_Shooter_enemy(enemy_x, enemy_y);
						enemys.set(i, enemy);
					}
				}
			}
		}
	}

	// 敵人與BOSS 子彈 TIMERTASL
	class BulletTimer extends TimerTask {
		@Override
		public void run() {
			if (running && !isPause) {
				for (int i = 0; i < enemys.size(); i++) {
					if (enemys.get(i) != null) {
						e_bullet = new Space_Shooter_bullet(
								enemys.get(i).getEnemyX() + Space_Shooter_enemy.ENEMY_SIZE / 2,
								enemys.get(i).EnemyY + Space_Shooter_enemy.ENEMY_SIZE);
						e_bullets.add(e_bullet);
					}
				}

				if (boss.getBoss_y() > 0) {
					b_bullet = new Space_Shooter_bullet(boss.getBoss_x() + boss.getBoss_w() / 2 - 30,
							boss.getBoss_y() + boss.getBoss_h() - 50);
					b_bullets.add(b_bullet);

				}

			}
		}

	}

	// WARNING聲音
	class warning_sound extends Thread {
		@Override
		public void run() {
			File file = new File("sounds/warning.wav");
			try {
				AudioInputStream audio = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(audio);
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(6.02f);
				clip.start();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	// shoot聲音
	class shoot_sound extends Thread {
		@Override
		public void run() {
			File file = new File("sounds/shoot.wav");
			try {
				AudioInputStream audio = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(audio);
				clip.start();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	// 畫面移動TimerTask
	class moveBg extends TimerTask {
		@Override
		public void run() {
			if (running) {
				if (dy <= 800) {
					dy++;
				} else {
					dy = 0;
				}
			}
		}
	}

	// 遊戲時間計時 用於計算BOSS出現時間
	class gameTimer extends TimerTask {
		@Override
		public void run() {
			if (running) {
				gameTime += dtime;
			}

			if (gameTime == 16) {
				t1.start();
			}
		}
	}
}
