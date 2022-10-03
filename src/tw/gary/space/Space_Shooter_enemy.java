package tw.gary.space;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Space_Shooter_enemy {
	public int EnemyX, EnemyY ; //敵人座標
	private BufferedImage[] ENEMY_PIC = new BufferedImage[5];//敵人圖片
	private int ENEMY_SPEED = (int) (Math.random() * 3 + 2); //敵人移動速度
	static final int ENEMY_SIZE = 70; //敵人大小
	public boolean stay = true; // 爆炸是否存在
	private Space_Shooter_Break BREAK; 
	private int id; //爆炸圖片ID

	Space_Shooter_enemy(int x, int y) {
		//初始化敵人座標
		this.EnemyX = x;
		this.EnemyY = y;
		try {
			for (int i = 0; i <= 4; i++) {
				ENEMY_PIC[i] = ImageIO.read(new File("images\\bullet0" + (i + 1) + ".png"));
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}
//敵人移動
	public void tick() {
		EnemyY += ENEMY_SPEED;
	}
//畫出敵人圖片以及stay為false時畫出爆炸圖片
	public void DrawEnemy(Graphics g, int i) {
		if (stay) {
			g.drawImage(ENEMY_PIC[i], EnemyX, EnemyY, null);
		} else if (id == 0) {
			BREAK = new Space_Shooter_Break(EnemyX, EnemyY);
			BREAK.enemy_break(g, i);
			id++;
		}

		if (BREAK != null && id != 0) {
			if (id == 29) {
				BREAK.enemy_break(g, id);
				id = 0;
			} else {
				BREAK.enemy_break(g, id);
				id++;
			}
		}
	}

	public int getEnemyX() {
		return EnemyX;
	}

	public int getEnemyY() {
		return EnemyY;
	}

	public int getId() {
		return id;
	}
	
}
