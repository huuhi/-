package sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEmployee extends JFrame {
    private JTextField txtId, txtName, txtSex, txtAge, txtPhone, txtPosition, txtSalary, txtDepartment;
    private JFormattedTextField txtHireDate;
    private JButton btnSave, btnCancel;
    private EmployeeManagerUI managerUI;

    public AddEmployee(EmployeeManagerUI managerUI) {
        super("添加员工");
        this.managerUI = managerUI;
        setLayout(new BorderLayout());

        // 创建表单布局部分
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // 初始化按钮监听器
        initButtonListeners();

        // 显示窗口
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Font labelFont = new Font("楷体", Font.PLAIN, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtId = new JTextField(10);
        panel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("姓名:");
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        txtName = new JTextField(10);
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel sexLabel = new JLabel("性别:");
        sexLabel.setFont(labelFont);
        panel.add(sexLabel, gbc);

        gbc.gridx = 1;
        txtSex = new JTextField(10);
        panel.add(txtSex, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel ageLabel = new JLabel("年龄:");
        ageLabel.setFont(labelFont);
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        txtAge = new JTextField(10);
        panel.add(txtAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel phoneLabel = new JLabel("电话:");
        phoneLabel.setFont(labelFont);
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        txtPhone = new JTextField(10);
        panel.add(txtPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel positionLabel = new JLabel("职位:");
        positionLabel.setFont(labelFont);
        panel.add(positionLabel, gbc);

        gbc.gridx = 1;
        txtPosition = new JTextField(10);
        panel.add(txtPosition, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel hireDateLabel = new JLabel("入职日期:");
        hireDateLabel.setFont(labelFont);
        panel.add(hireDateLabel, gbc);

        gbc.gridx = 1;
        txtHireDate = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        panel.add(txtHireDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel salaryLabel = new JLabel("薪水:");
        salaryLabel.setFont(labelFont);
        panel.add(salaryLabel, gbc);

        gbc.gridx = 1;
        txtSalary = new JTextField(10);
        panel.add(txtSalary, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel departmentLabel = new JLabel("部门:");
        departmentLabel.setFont(labelFont);
        panel.add(departmentLabel, gbc);

        gbc.gridx = 1;
        txtDepartment = new JTextField(10);
        panel.add(txtDepartment, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        btnSave = new JButton("添加");
        btnCancel = new JButton("取消");
        btnSave.setPreferredSize(new Dimension(100, 30));
        btnCancel.setPreferredSize(new Dimension(100, 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void initButtonListeners() {
        btnSave.addActionListener(e -> saveEmployee());
        btnCancel.addActionListener(e -> disposeWindow(e));
    }

    private void saveEmployee() {
        // 获取输入框中的数据
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String sex = txtSex.getText().trim();
        String age = txtAge.getText().trim();
        String phone = txtPhone.getText().trim();
        String position = txtPosition.getText().trim();
        Date hireDate;
        try {
            hireDate = new SimpleDateFormat("yyyy-MM-dd").parse(txtHireDate.getText().trim());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "日期格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String salary = txtSalary.getText().trim();
        String department = txtDepartment.getText().trim();

        // 构建员工信息字符串
        StringBuilder employeeInfo = new StringBuilder();
        employeeInfo.append(id).append(",")
                .append(name).append(",")
                .append(sex).append(",")
                .append(age).append(",")
                .append(phone).append(",")
                .append(position).append(",")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(hireDate)).append(",")
                .append(salary).append(",")
                .append(department).append("\n");

        // 将员工信息写入文件
        try (FileWriter file = new FileWriter("E:\\jcode\\newjavaseaiman\\day08_oop\\src\\employees.txt", true)) {
            file.write(employeeInfo.toString());
            JOptionPane.showMessageDialog(this, "员工信息添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            managerUI.refreshTable(); // 刷新主界面中的表格
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "文件写入失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 清空输入框
        txtId.setText("");
        txtName.setText("");
        txtSex.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtPosition.setText("");
        txtHireDate.setValue(null);
        txtSalary.setText("");
        txtDepartment.setText("");
    }

    private void disposeWindow(ActionEvent e) {
        dispose(); // 关闭窗口
    }
}