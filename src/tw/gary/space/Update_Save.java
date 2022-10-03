package tw.gary.space;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class Update_Save {
	private Connection conn;
	private PreparedStatement Pstmt;
	private int nowScore;
	private ResultSet rs1;

	Update_Save() {

		// 連結資料庫
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/space_shooter", prop);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
// 如果分數比原來的高 就更新那筆資料
	public void updateScore(int score) throws Exception {
		String Query_sql = "SELECT * FROM player where id = ?";
		Pstmt = conn.prepareStatement(Query_sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		Pstmt.setInt(1, Login.id);
		ResultSet rs = Pstmt.executeQuery();
		rs.next();
		nowScore = rs.getInt("score");

		if (score > nowScore) {
			String sql = "UPDATE player SET score = ? WHERE id = ?";
			PreparedStatement Pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			Pstmt.setInt(1, score);
			Pstmt.setInt(2, Login.id);
			int x = Pstmt.executeUpdate();
			System.out.println(x);
		}
	}
   //排行榜顯示
	public void drawRank(Graphics g) throws Exception {
		String Query_sql = "SELECT id,user,score FROM player ORDER BY score DESC LIMIT 0,10";
		Pstmt = conn.prepareStatement(Query_sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs1 = Pstmt.executeQuery();
		g.setFont(new Font("arial", Font.BOLD, 70));
		g.setColor(Color.BLUE);
		g.drawString("RANK        " + "USER        " + "SCORE", 20, 70);
		for (int i = 0; rs1.next(); i++) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.setColor(Color.BLACK);
			g.drawString((i + 1) + "", 30, 140 + i * 70);
			g.drawString(rs1.getString("user"), 400, 140 + i * 70);
			String st = rs1.getInt("score") + "";
			switch (st.length()){
				case 1 : st = "        " + st;  break;
				case 2 : st = "      " + st;  break;
				case 3 : st = "    " + st;  break;
				case 4 : st = "  " + st;  break;
			}

			g.drawString(st, 800, 140 + i * 70);

		}
		conn.close();

	}
}
