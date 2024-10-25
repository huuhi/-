package sys;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterDialog extends JDialog {
    private JTextField usernameInput;
    private JTextField loginNameInput;
    private JPasswordField passwordInput;
    private JFrame parentFrame;

    public RegisterDialog(JFrame parentFrame) {
        super(parentFrame, "用户注册", true);
        this.parentFrame = parentFrame;
        setSize(300, 250);
        setLocationRelativeTo(parentFrame);
        setLayout(null);

        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setBounds(20, 20, 100, 30);
        add(usernameLabel);

        // 用户名输入框
        usernameInput = new JTextField();
        usernameInput.setBounds(120, 20, 150, 30);
        add(usernameInput);

        // 登录名标签
        JLabel loginNameLabel = new JLabel("登录名：");
        loginNameLabel.setBounds(20, 60, 100, 30);
        add(loginNameLabel);

        // 登录名输入框
        loginNameInput = new JTextField();
        loginNameInput.setBounds(120, 60, 150, 30);
        add(loginNameInput);

        // 密码标签
        JLabel passwordLabel = new JLabel("密   码：");
        passwordLabel.setBounds(20, 100, 100, 30);
        add(passwordLabel);

        // 密码输入框
        passwordInput = new JPasswordField();
        passwordInput.setBounds(120, 100, 150, 30);
        passwordInput.setEchoChar('*');
        add(passwordInput);

        // 注册按钮
        JButton registerButton = new JButton("注   册");
        registerButton.setBounds(100, 160, 100, 30);
        registerButton.addActionListener(e -> {
            String username = usernameInput.getText();
            String loginName = loginNameInput.getText();
            char[] password = passwordInput.getPassword();
            registerUser(loginName, new String(password), username);
            dispose();
            JOptionPane.showMessageDialog(parentFrame, "注册成功！");
        });
        add(registerButton);

        setVisible(true);
    }

    public static void showRegistrationDialog(JFrame parentFrame) {
        new RegisterDialog(parentFrame);
    }

    private void registerUser(String loginName, String password, String username) {
        List<String> users = readUsersFromFile();
        users.add(loginName + ":" + password + ":" + username);
        writeUsersToFile(users);
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

    private void writeUsersToFile(List<String> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\jcode\\newjavaseaiman\\day08_oop\\src\\users.txt"))) {
            for (String user : users) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}