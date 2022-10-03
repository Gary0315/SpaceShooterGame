package tw.gary.space;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Space_Shooter_player extends JPanel {

	private int Plane_x, Plane_y; // 我方飛機座標
	private Image planePic;// 畫出GIF圖片
	boolean stayed ; // 玩家飛機生存
	private Space_Shooter_Break b;// 爆炸圖片對象
	private int id;// 爆炸圖片
	public static int planeID;// 玩家飛機編號
	public static final int PLANE_SIZE = 90; // 飛機大小

	public Space_Shooter_player(int x, int y) {
		stayed = true;
		// 初始化飛機位置
		this.Plane_x = x;
		this.Plane_y = y;
		// 讀出GIF圖片
		planePic = new ImageIcon("images\\ship.gif").getImage();
	}

	// 劃出飛機圖片以及爆炸
	public void drawMyplane(Graphics g) {
		// 如果生存
		if (stayed) {
			g.drawImage(planePic, Plane_x, Plane_y, PLANE_SIZE, PLANE_SIZE, null);
		} else if (id == 0) {
			b = new Space_Shooter_Break(Plane_x, Plane_y);
			b.plane_break(g, id);
			id++;
		} 
		if (b != null && id != 0) {
			if (id <= 29) {
				b.plane_break(g, id);
				id ++;
			} 
		}
	};

	public int get_X() {
		return Plane_x;
	}

	public int get_Y() {
		return Plane_y;
	}

	public int getId() {
		return id;
	}

	public void setPlane_x(int plane_x) {
		Plane_x = plane_x;
	}

	public void setPlane_y(int plane_y) {
		Plane_y = plane_y;
	}

}
