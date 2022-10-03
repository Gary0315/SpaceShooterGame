package tw.gary.space;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// 描述 飛機爆炸
public class Space_Shooter_Break {
	private BufferedImage bk[];
	private int x, y;
	
	public Space_Shooter_Break(int x, int y) {
		// 放入六張爆炸圖片
		bk = new BufferedImage[6];
		for (int i = 0; i < bk.length; i++) {
			try {
				bk[i] = ImageIO.read(new File("images\\bomb_enemy_"+ i + ".png"));
			} catch (IOException e) {
				System.out.println(e.toString());
			}		
		}
		// 初始化座標
		this.x = x;
		this.y = y;
		
	}

	// 繪製 敵方飛機爆炸
	public void enemy_break(Graphics g, int i) {
		g.drawImage(bk[i / 5], x, y, Space_Shooter_enemy.ENEMY_SIZE+75, Space_Shooter_enemy.ENEMY_SIZE+75, null);

	      
	}
    // 繪製 BOSS飛機爆炸
	public void boss_break(Graphics g, int i) {
		g.drawImage(bk[i / 20], x, y, Space_Shooter_Boss.Boss_w, Space_Shooter_Boss.Boss_h, null); 
	}
	
	
	// 繪製玩家飛機 爆炸
	public void plane_break(Graphics g, int i) {
		g.drawImage(bk[i / 5], x, y, Space_Shooter_player.PLANE_SIZE+100, Space_Shooter_player.PLANE_SIZE+100,null);

	}

}
