package sys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReadTest {
    public static void main(String[] args) {
        String filePath = "day08_oop\\src\\employees.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件读取失败：" + e.getMessage());
        }
    }
}