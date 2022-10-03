package tw.gary.space;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Space_Shooter_Boss {
	private BufferedImage boss;
	private int Boss_x;//BOSS的x座標
	private int Boss_y; //BOSS的y座標
	public static final int Boss_w = 500; //BOSS寬
	public static final int Boss_h = 360; //BOSS高
	private int dx = 2 , dy = 1; //BOSS 移動速度
	public boolean stay; //BOSS 生存
	private int id; //BOSS 爆炸圖片ID
	private Space_Shooter_Break BREAK; //BOSS 爆炸

	Space_Shooter_Boss() {
		//初始化BOSS的狀態
		Boss_x = 175;
		Boss_y = -300;
		stay = true;

		try {
			boss = ImageIO.read(new File("images\\boss.png")); //讀取BOSS圖片
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}



	public void drawBoss(Graphics g) {
		if (stay) {
			g.drawImage(boss, Boss_x, Boss_y, Boss_w, Boss_h, null);
		} else if (id == 0) {
			//依照ID順序畫出爆炸圖片
			new boss_sound().start();
			BREAK = new Space_Shooter_Break(Boss_x, Boss_y);
			BREAK.boss_break(g, id);
			id++;
		}

		if (BREAK != null && id != 0)
			if (id <= 119) {	
				BREAK.boss_break(g, id);
				id++;
			}

	}
//BOSS 移動方式
	public void bossMove() {
		if (Boss_y <= 0 &&Boss_x==175 ) {
			Boss_y += dy;
		} else if(Boss_y>=1) {
			if ((Boss_x + Boss_w > 1000 || Boss_x < 0)) {
				dx *= -1;
			}

			if (Boss_y + Boss_h > 800 || Boss_y <=1&&Boss_x!=175  ) {
				dy *= -1;
			}
			Boss_x += dx;
			Boss_y += dy;
		}
		

		
	}
	
	public int getBoss_w() {
		return Boss_w;
	}

	public int getBoss_h() {
		return Boss_h;
	}

	public int getBoss_x() {
		return Boss_x;
	}

	public int getBoss_y() {
		return Boss_y;
	}
	
	public int getId() {
		return id;
	}
	
	public void setBoss_y(int boss_y) {
		Boss_y = boss_y;
	}
	//BOSS 爆炸聲音
	class boss_sound extends Thread {
		@Override
		public void run() {
			File file  = new File("sounds/HeroBrustSound.wav");
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
