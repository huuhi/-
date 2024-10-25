package sys;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteTest {
    public static void main(String[] args) {
        String filePath = "E:\\jcode\\newjavaseaiman\\day08_oop\\src\\employees.txt";
        
        try (FileWriter file = new FileWriter(filePath, true)) {
            file.write("测试数据\n");
            System.out.println("数据写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("数据写入失败：" + e.getMessage());
        }
    }
}