package sys;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginUI extends JFrame {
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JButton loginButton; // 登录按钮
    private JButton registerButton; // 注册按钮

    public LoginUI() {
        super("登录界面");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null); // 居中显示

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // 设置字体和颜色
        Font customFont = new Font("楷体", Font.BOLD, 18);
        Color primaryColor = new Color(66, 135, 245);
        Color secondaryColor = new Color(204, 204, 204); // 更浅的颜色用于注册按钮

        // 标题
        JLabel titleLabel = new JLabel("二次元人事管理系统");
        titleLabel.setBounds(50, 30, 300, 30);
        titleLabel.setFont(new Font("楷体", Font.BOLD, 24));
        panel.add(titleLabel);

        // 用户名标签
        JLabel usernameLabel = new JLabel("登录名：");
        usernameLabel.setBounds(50, 100, 150, 30);
        usernameLabel.setFont(customFont);
        panel.add(usernameLabel);

        // 用户名输入框
        usernameField = new JTextField();
        usernameField.setBounds(160, 100, 190, 30);
        usernameField.setFont(customFont);
        panel.add(usernameField);

        // 密码标签
        JLabel passwordLabel = new JLabel("密   码：");
        passwordLabel.setBounds(50, 150, 150, 30);
        passwordLabel.setFont(customFont);
        panel.add(passwordLabel);

        // 密码输入框
        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 190, 30);
        passwordField.setFont(customFont);
        passwordField.setEchoChar('*'); // 设置密码显示为星号
        panel.add(passwordField);

        // 登录按钮
        loginButton = new JButton("登   录");
        loginButton.setBounds(50, 200, 150, 30);
        loginButton.setFont(customFont);
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> {
            String loginName = usernameField.getText();
            char[] password = passwordField.getPassword();
            if (validateUser(loginName, new String(password))) {
                JOptionPane.showMessageDialog(this, "登录成功！");
                // 获取用户名
                String username = getUsernameFromLogin(loginName);
                new EmployeeManagerUI(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "登录名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton);

        // 注册按钮
        registerButton = new JButton("注   册");
        registerButton.setBounds(200, 200, 150, 30);
        registerButton.setFont(customFont);
        registerButton.setBackground(secondaryColor);
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(e -> {
            RegisterDialog.showRegistrationDialog(this);
        });
        panel.add(registerButton);

        // 添加面板到窗口
        this.add(panel);
        this.setVisible(true);
    }

    private boolean validateUser(String loginName, String password) {
        List<String> users = readUsersFromFile();
        for (String user : users) {
            String[] parts = user.split(":");
            if (parts.length == 3 && parts[0].equals(loginName) && parts[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    private String getUsernameFromLogin(String loginName) {
        List<String> users = readUsersFromFile();
        for (String user : users) {
            String[] parts = user.split(":");
            if (parts.length == 3 && parts[0].equals(loginName)) {
                return parts[2];
            }
        }
        return null;
    }

    private List<String> readUsersFromFile() {
        List<String> users = new ArrayList<>();
        File file = new File("E:\\jcode\\newjavaseaiman\\day08_oop\\src\\users.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new LoginUI());
    }
}