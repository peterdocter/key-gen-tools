import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created on 3/14/15.
 */
public class MakeExamDb {
    public static void main(String[] args) {
        String path = "/Users/liyong/Downloads/传染病学.txt";
        readFileByLines(path);
    }

    public static int insertDb(String type, String question, String options, int answer){
        DbUtils getConn=new DbUtils();
        ResultSet rs = null;
        Connection conn=getConn.getConnection();
        try {
            PreparedStatement ps=conn.prepareStatement("INSERT INTO question(type,question,options,answer) VALUES(?,?,?,?)");
            ps.setString(1, type);
            ps.setString(2, question);
            ps.setString(3, options);
            ps.setInt(4, answer);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            String tempString = "";
            int flag = 1;
            String type = file.getName().split("\\.")[0];
            String question = "";
            String options = "";
            int answer = 0;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("B1型题")) {
                    break;
                }
                if (tempString.contains("型题") || tempString.equals("")){
                    flag = 1;
                    options =  "";
                    continue;
                }

                if (tempString.contains("答案")) {
                    answer = tempString.charAt(tempString.length() - 1)- 'A';
                    System.out.println("insert :" + type + question + options + answer);
                    insertDb(type, question, options, answer);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                if (flag == 1) {
                    question = tempString;
                } else {
                    options += tempString;
                    options += "|";
                }
                flag++;
               // System.out.println("flag: " + flag + ": " + tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

}
