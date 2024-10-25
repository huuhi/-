package sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagerUI extends JFrame {
    private JFrame frame;
    private JTable table; // 表格
    private DefaultTableModel model; // 表格模型，用于管理表格的数据
    private JTextField NameTextFieldSearch; // 搜索输入框
    private JButton btnSearch; // 搜索按钮
    private JButton btnAdd; // 添加按钮
    private JButton btnShowAll; // 显示全部按钮
    private String filePath = "E:\\jcode\\newjavaseaiman\\day08_oop\\src\\employees.txt";

    public EmployeeManagerUI(String username) {
        super("欢迎 " + username + " 进入管理系统");
        frame = this;
        initialize();
        frame.setVisible(true);

        // 加载员工信息
        loadEmployees();
    }

    private void initialize() {
        // 设置全局字体大小
        UIManager.put("defaultFont", new java.awt.Font("楷体", Font.PLAIN, 16));

        this.setBounds(100, 100, 800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        // 输入框和搜索按钮
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(240, 240, 240));
        NameTextFieldSearch = new JTextField(20);
        NameTextFieldSearch.setFont(new Font("楷体", Font.PLAIN, 14));
        btnSearch = new JButton("搜索");
        btnSearch.setFont(new Font("楷体", Font.PLAIN, 14));
        btnAdd = new JButton("添加");
        btnAdd.setFont(new Font("楷体", Font.PLAIN, 14));
        btnShowAll = new JButton("显示全部");
        btnShowAll.setFont(new Font("楷体", Font.PLAIN, 14));
        topPanel.add(NameTextFieldSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnAdd);
        topPanel.add(btnShowAll);

        // 创建表格模型
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "姓名", "性别", "年龄", "电话", "职位", "入职日期", "薪水", "部门"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置所有单元格为不可编辑
            }
        };

        table = new JTable(model);
        table.setFont(new Font("楷体", Font.PLAIN, 14)); // 设置表格字体大小
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // 右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("编辑");
        JMenuItem deleteItem = new JMenuItem("删除");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) { // 检查是否为鼠标右键
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        // 绑定事件到菜单项
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    showEditDialog(selectedRow);
                }
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Object idObj = model.getValueAt(selectedRow, 0); // 假设ID在第一列
                    int id;
                    try {
                        id = Integer.parseInt(idObj.toString()); // 尝试转换为整数
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "ID不是一个有效的整数: " + idObj);
                        return;
                    }
                    int option = JOptionPane.showConfirmDialog(frame, "确定删除 ID: " + id + " 的记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // 从模型中移除行
                        model.removeRow(selectedRow);

                        // 更新文件
                        updateFileAfterDelete(selectedRow);
                    }
                }
            }
        });

        // 搜索按钮监听器
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchValue = NameTextFieldSearch.getText();
                if (!searchValue.isEmpty()) {
                    performSearch(searchValue);
                } else {
                    JOptionPane.showMessageDialog(frame, "请输入搜索条件！");
                }
            }
        });

        // 添加按钮监听器
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里可以添加添加新员工的逻辑
                new AddEmployee(EmployeeManagerUI.this); // 传递 EmployeeManagerUI 实例
            }
        });

        // 显示全部按钮监听器
        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    }

    protected void loadEmployees() {
        // 清空表格模型
        model.setRowCount(0);

        // 从文件中读取员工信息
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "文件读取失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFileAfterDelete(int deletedRow) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {

            String line;
            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                if (currentRow != deletedRow) {
                    writer.write(line);
                    writer.newLine();
                }
                currentRow++;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "文件更新失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 删除原始文件
            File originalFile = new File(filePath);
            originalFile.delete();

            // 重命名临时文件为原文件名
            File tempFile = new File(filePath + ".tmp");
            tempFile.renameTo(originalFile);
        }
    }

    private void showEditDialog(final int selectedRow) {
        final Object[] rowData = new Object[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            rowData[i] = model.getValueAt(selectedRow, i);
        }

        JPanel panel = new JPanel();
        List<JTextField> textFields = new ArrayList<>();

        for (int i = 0; i < model.getColumnCount(); i++) {
            JLabel label = new JLabel(model.getColumnName(i) + ": ");
            JTextField textField = new JTextField(rowData[i].toString());
            panel.add(label);
            panel.add(textField);
            textFields.add(textField);
        }

        int result = JOptionPane.showConfirmDialog(frame, panel, "编辑员工信息", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < textFields.size(); i++) {
                model.setValueAt(textFields.get(i).getText(), selectedRow, i);
            }
            updateFileAfterEdit(selectedRow, textFields);
        }
    }

    private void updateFileAfterEdit(int selectedRow, List<JTextField> textFields) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {

            String line;
            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                if (currentRow == selectedRow) {
                    StringBuilder updatedLine = new StringBuilder();
                    for (JTextField textField : textFields) {
                        updatedLine.append(textField.getText()).append(",");
                    }
                    updatedLine.deleteCharAt(updatedLine.length() - 1); // 移除最后一个逗号
                    writer.write(updatedLine.toString());
                } else {
                    writer.write(line);
                }
                writer.newLine();
                currentRow++;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "文件更新失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 删除原始文件
            File originalFile = new File(filePath);
            originalFile.delete();

            // 重命名临时文件为原文件名
            File tempFile = new File(filePath + ".tmp");
            tempFile.renameTo(originalFile);
        }
    }

    private void performSearch(String searchValue) {
        // 清空表格模型
        model.setRowCount(0);

        // 从文件中读取员工信息
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 9 && (parts[0].equals(searchValue) || parts[1].contains(searchValue))) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "文件读取失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        loadEmployees();
    }
}