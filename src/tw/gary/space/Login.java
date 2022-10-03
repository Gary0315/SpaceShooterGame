package tw.gary.space;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class Login extends JFrame {
	private JButton login;
	private String user, password;
	private Connection conn;
	public static int id;
	
	//如果有使用資料庫就可以去做新增帳號密碼 跟 可以顯示排行榜
	Login() {
        super("Login");
		// 連結資料庫
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/space_shooter", prop);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// 輸入帳號密碼姓名
		JLabel userLabel = new JLabel("user:");
		JLabel passwordLabel = new JLabel("password:");
		TextField userText = new TextField();
		JPasswordField passwordText = new JPasswordField();
		login = new JButton("login");
		login.setBounds(130, 120, 80, 30);
		setBounds(700, 400, 270, 200);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		add(login);

		Font font = new Font(null, Font.BOLD, 16);
		userLabel.setFont(font);
		userLabel.setBounds(20, 20, 40, 30);
		add(userLabel);
		passwordLabel.setFont(font);
		passwordLabel.setBounds(20, 55, 100, 30);
		add(passwordLabel);
		userText.setBounds(120, 28, 100, 20);
		add(userText);
		passwordText.setBounds(120, 63, 100, 20);
		add(passwordText);

		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				user = userText.getText();
				char[] x = passwordText.getPassword();
				password = new String(x);
				try {
					login(user, password);
					conn.close();
				} catch (Exception e1) {
					System.out.println(e1.toString());
				}
				dispose();
			}
		});
	}
//登入帳密驗證 如果user不重複 就直接新增至 資料庫
	public void login(String user, String password) throws Exception {
		if (user != null && password != null) {
			String sql1 = "SELECT * FROM player where user = ?";
			PreparedStatement Pstmt = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			Pstmt.setString(1, user);
			ResultSet rs = Pstmt.executeQuery();
			if (rs.next()) {
				if (BCrypt.checkpw(password, rs.getString("password"))) {
					new Space_Shooter_Frame();
					id = rs.getInt("id");
				} else {
					new Login();
				}

			} else {
				String sql2 = "INSERT INTO player (user,password)VALUES (?,?)";
				PreparedStatement Pstmt1 = conn.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				Pstmt1.setString(1, user);
				Pstmt1.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
				Pstmt1.executeUpdate();
				
				Pstmt = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				Pstmt.setString(1, user);
				rs = Pstmt.executeQuery();
				rs.next();
				id = rs.getInt("id");
				new Space_Shooter_Frame();
			}
		}
	}	
}
