package FileManager.test;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @version v1.0
 * @ClassName: Test
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/30 on 19:14
 */
public class MyTest {
    @Test
    public void test_1(){
        String s = "mk a 100";
        s = s.trim();
        String[] split = s.split("\\s+");
        for (int i =0;i<split.length;i++){
            System.out.println(i+split[i]);
        }
//        System.out.println(split.length);
    }
    @Test
    public void test_2(){
        String s = "\\a\\b";
        String[] split1 = s.split("\\\\");
        System.out.println(split1[0].equals(""));
        System.out.println(split1.length);
    }
    @Test
    public void test_3(){
        String s = "*";
        String[] split = s.split("(?=\\*)|(?<=\\*)");
        StringBuilder sb = new StringBuilder();
        System.out.println(split.length);
        for (int i=0;i<split.length;i++){
            System.out.println(i+split[i]);
            if (split[i].equals("*")){
                sb.append("(.)*");
            }else {
                sb.append(split[i]);
            }
        }
//        sb.append(split[split.length-1]);
        System.out.println(sb);
        Pattern pattern = Pattern.compile(sb.toString());
        String b = "0.txt";
        System.out.println(pattern.matcher(b).matches());
    }
}
