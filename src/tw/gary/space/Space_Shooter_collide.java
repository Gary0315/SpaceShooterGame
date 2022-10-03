package tw.gary.space;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Space_Shooter_collide {
	//子彈與飛機碰撞
	public void bullet_enemy(Space_Shooter_bullet b, Space_Shooter_enemy e) {
		if ((b.get_X() >= e.getEnemyX() - b.getB_w())
				&& (b.get_X() <= e.getEnemyX() + Space_Shooter_enemy.ENEMY_SIZE + 50)
				&& (b.get_Y() >= (e.getEnemyY() - b.getB_h()))
				&& b.get_Y() <= e.getEnemyY() + Space_Shooter_enemy.ENEMY_SIZE) {
			b.stay = false;
			e.stay = false;
			new break_sound().start();
		}
	}
    //飛機與敵人碰撞
	void plane_enemy(Space_Shooter_player m, Space_Shooter_enemy e) {
		if (m.get_X() >= e.getEnemyX() - Space_Shooter_player.PLANE_SIZE
				&& m.get_X() <= e.getEnemyX() + Space_Shooter_enemy.ENEMY_SIZE
				&& m.get_Y() >= e.getEnemyY() - Space_Shooter_player.PLANE_SIZE
				&& m.get_Y() <= e.getEnemyY() + Space_Shooter_enemy.ENEMY_SIZE) {
			new break_sound().start();
			new plane_sound().start();
			e.stay = false;
			if (Space_Shooter_panel.life <= 30) {
				m.stayed = false;
				Space_Shooter_panel.life = 0;
			}else {
				Space_Shooter_panel.life -= 30;
			}
		}
	}
// 敵人子彈與我方飛機碰撞
	void bullet_plane(Space_Shooter_bullet b, Space_Shooter_player m) {
		if (b.getB_xe() >= m.get_X()- b.getB_w()
				&& b.getB_xe() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
				&& b.getB_ye() >= m.get_Y()
				&& b.getB_ye() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE) {
			b.stay = false;
			new plane_sound().start();

			if (Space_Shooter_panel.life <= 10) {
				m.stayed = false;
				Space_Shooter_panel.life = 0;
			} else
				Space_Shooter_panel.life -= 10;
		}
	}
	// BOSS 子彈與我方飛機碰撞
	void boss_plane(Space_Shooter_bullet b, Space_Shooter_player m) {
		if((b.getB_xb1() >= m.get_X() - b.getB_w()
			&& b.getB_xb1() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
			&&b.getB_yb1() >= m.get_Y() 
			&& b.getB_yb1() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE)
				||( b.getB_xb2() >= m.get_X() - b.getB_w()
				&& b.getB_xb2() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
				&&b.getB_yb2() >= m.get_Y() 
				&& b.getB_yb2() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE)
				||( b.getB_xb3() >= m.get_X() - b.getB_w()
				&& b.getB_xb3() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
				&&b.getB_yb3() >= m.get_Y() 
				&& b.getB_yb3() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE)
				||( b.getB_xb4() >= m.get_X() - b.getB_w()
				&& b.getB_xb4() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
				&&b.getB_yb4() >= m.get_Y() 
				&& b.getB_yb4() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE)
				||( b.getB_xb5() >= m.get_X() - b.getB_w()
				&& b.getB_xb5() <= m.get_X() + Space_Shooter_player.PLANE_SIZE - b.getB_w()
				&&b.getB_yb5() >= m.get_Y() 
				&& b.getB_yb5() <= m.get_Y() + Space_Shooter_player.PLANE_SIZE)) {
			new plane_sound().start();
			b.stay = false;
// 我方飛機扣血
			if (Space_Shooter_panel.life <= 20) {
				m.stayed = false;
				Space_Shooter_panel.life = 0;
			} else
				Space_Shooter_panel.life -= 20;
		}
	}
	//我方子彈與BOSS碰撞
	void bullet_boss(Space_Shooter_bullet b, Space_Shooter_Boss m) {
		if(b.get_X() >= m.getBoss_x()
				&& b.get_X() <= m.getBoss_x()+m.getBoss_w() - b.getB_w()
				&& b.get_Y() >= m.getBoss_y()
				&& b.get_Y() <= m.getBoss_y()+m.getBoss_h()
				&& m.getBoss_y() >= 0) {
			new boss_sound().start();
			//打到BOSS增加分數 跟 BOSS扣血
			Space_Shooter_panel.score += 500;
			b.stay = false;
			if(Space_Shooter_panel.life1 <= 50) {
				m.stay = false;
				Space_Shooter_panel.life1 = 0;
			} else
				Space_Shooter_panel.life1 -= 50;
		}
	}
	// BOSS 跟 我方飛機碰撞
	void boss_plane(Space_Shooter_Boss b, Space_Shooter_player m) {
		if(b.getBoss_x() >= m.get_X()-b.getBoss_w()+50
				&& b.getBoss_x()+50 <= m.get_X()+Space_Shooter_player.PLANE_SIZE
				&& b.getBoss_y() >= m.get_Y()-b.getBoss_h()+50
				&& b.getBoss_y()+100 <= m.get_Y()+Space_Shooter_player.PLANE_SIZE) {
			m.stayed = false;
			Space_Shooter_panel.life = 0;
		}
	}
	//爆炸聲音
	class break_sound extends Thread {
		@Override
		public void run() {
			File file  = new File("sounds/Break.wav");
			try {
				AudioInputStream audio = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(audio);  
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-2f);
				clip.start();
			} catch (Exception e) {
				System.out.println(e.toString());
			} 
		}
	}
	
	class plane_sound extends Thread {
		@Override
		public void run() {
			File file  = new File("sounds/explosion.wav");
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
	
	class boss_sound extends Thread {
		@Override
		public void run() {
			File file  = new File("sounds/bomb.wav");
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
