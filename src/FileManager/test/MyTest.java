package FileManager.test;

import org.junit.Test;

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
}
