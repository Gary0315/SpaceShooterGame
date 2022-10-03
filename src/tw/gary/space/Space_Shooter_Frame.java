package tw.gary.space;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Space_Shooter_Frame extends JFrame {
	private JButton pause, restart, rank, screenshot;
	public static JButton start;
	private Space_Shooter_panel jpanel = new Space_Shooter_panel();

// 遊戲視窗框架
	Space_Shooter_Frame() {
		super("Space Shooter");
		JPanel top = new JPanel(new FlowLayout());
		start = new JButton("Start");
		pause = new JButton("Pause(P)");
		restart = new JButton("restart");
		rank = new JButton("Rank");
		screenshot = new JButton("ScreenShot");
		start.setFocusable(false);
		pause.setFocusable(false);
		restart.setFocusable(false);
		rank.setFocusable(false);
		screenshot.setFocusable(false);

		// 點擊Start按鈕的事件發生
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jpanel.running = true;
				new click_sound().start();
				start.setEnabled(false);
			}
		});

		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new click_sound().start();
				jpanel.pauseGame();
				Object[] obj = { "resume", };
				if (JOptionPane.showOptionDialog(null, "pause", "message", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, obj, obj[0]) == JOptionPane.YES_OPTION) {
					jpanel.pauseGame();
				} else {
					jpanel.restartGame();
				}

			}
		});

		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new click_sound().start();
				jpanel.restartGame();
				jpanel.Ranking = false;
			}
		});

		rank.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new click_sound().start();
				jpanel.Ranking = true;
			}
		});

		screenshot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jpanel.screenShot();
			}
		});
		// 視窗版面配置
		setLayout(new BorderLayout());
		top.add(start);
		top.add(pause);
		top.add(restart);
		top.add(rank);
		top.add(screenshot);

		add(top, BorderLayout.NORTH);
		add(jpanel, BorderLayout.CENTER);
		// focus 設定false可以讓按鍵不要focus在上面
		top.setFocusable(false);
		setResizable(false);
		setBounds(360, 100, 1012, 875);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 遊戲BGM
				new Thread() {
					@Override
					public void run() {
						File file = new File("sounds/OPSound.mid");
						try {
							AudioInputStream audio = AudioSystem.getAudioInputStream(file);
							Clip clip = AudioSystem.getClip();
							clip.open(audio);
							//循環播放
							clip.loop(Clip.LOOP_CONTINUOUSLY);
							clip.start();
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
				}.start();
		
	}

	// 點擊視窗按鈕音效
	class click_sound extends Thread {
		@Override
		public void run() {

			File file = new File("sounds/ClickSound.wav");
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
}
