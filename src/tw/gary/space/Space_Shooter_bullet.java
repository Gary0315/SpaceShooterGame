package tw.gary.space;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Space_Shooter_bullet {
	private int b_x, b_y //我方子彈座標
	,b_xe,b_ye //敵人子彈座標
	,b_xb1,b_yb1,b_xb2,b_yb2,b_xb3,b_yb3,b_xb4,b_yb4,b_xb5,b_yb5;//BOSS 子彈座標
	private BufferedImage bullet_p1, bullet_p2; // 子彈圖片
	public boolean stay = true; //子彈是否存在
    private int b_w =  25 ; //子彈寬
    private int b_h =  55;	//子彈高	

	public Space_Shooter_bullet(int x, int y) {
		this.b_x = x;
		this.b_y = y;
		this.b_xe = x;
		this.b_ye = y;
		this.b_xb1 = x;
		this.b_yb1 = y;
		this.b_xb2 = x;
		this.b_yb2 = y;
		this.b_xb3 = x;
		this.b_yb3 = y;
		this.b_xb4 = x;
		this.b_yb4 = y;
		this.b_xb5 = x;
		this.b_yb5 = y;
		try {
			bullet_p1 = ImageIO.read(new File("images\\bullet_01.png"));
			bullet_p2 = ImageIO.read(new File("images\\bullet_02.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	//我方子彈移動
	public void tick() {
		b_y -= 10;
	}
	//敵人子彈移動
	public void tick_E() {
		b_ye += 5;
	}
	
	
	//BOSS子彈移動
	public void tick_B() {
		b_yb1 +=4;
		b_xb2 +=2;
		b_yb2 +=4;
		b_xb3 -=2;
		b_yb3 +=4;
		b_xb4 +=4;
		b_yb4 +=4;
		b_xb5 -=4;
		b_yb5 +=4;
	}
	// 畫出我方子彈
	public void drawBullet(Graphics g) {
		g.drawImage(bullet_p1, b_x, b_y, b_w, b_h, null);
	}
	// 畫出敵人子彈	
	public void drawBullet_E(Graphics g) {
		g.drawImage(bullet_p2, b_xe, b_ye, b_h, b_h, null);
	}
	// 畫出BOSS子彈
	public void drawBullet_B(Graphics g) {
		g.drawImage(bullet_p2, b_xb1,b_yb1, b_h, b_h, null);
		g.drawImage(bullet_p2, b_xb2,b_yb2, b_h, b_h, null);
		g.drawImage(bullet_p2, b_xb3,b_yb3, b_h, b_h, null);
		g.drawImage(bullet_p2, b_xb4,b_yb4, b_h, b_h, null);
		g.drawImage(bullet_p2, b_xb5,b_yb5, b_h, b_h, null);
	}

    
    public int getB_w() {
		return b_w;
	}

	public int getB_h() {
		return b_h;
	}
	
	public int get_X() {
		return b_x;
	}

	public int get_Y() {
		return b_y;                                                                                                         
	}

	public int getB_xe() {
		return b_xe;
	}

	public int getB_ye() {
		return b_ye;
	}
	
	public int getB_xb1() {
		return b_xb1;
	}

	public int getB_yb1() {
		return b_yb1;
	}

	public int getB_xb2() {
		return b_xb2;
	}

	public int getB_yb2() {
		return b_yb2;
	}

	public int getB_xb3() {
		return b_xb3;
	}

	public int getB_yb3() {
		return b_yb3;
	}

	
	public int getB_yb4() {
		return b_yb4;
	}

	public int getB_yb5() {
		return b_yb5;
	}

	public int getB_xb4() {
		return b_xb4;
	}

	public int getB_xb5() {
		return b_xb5;
	}


}
