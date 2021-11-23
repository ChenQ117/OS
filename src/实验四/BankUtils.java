package 实验四;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: BankUtils
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/11/22 on 22:39
 */
public class BankUtils {
    final String fileName = "src/bank.txt";
    int[][] Max;
    int[][] Allocation;
    int[][] Need;
    int[] Work;
    String sequence = "";//用于记录完成序列
    int m = 0;
    int n = 0;
    boolean[] finished;

    public void export(){
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
            String line = bfr.readLine();
            String[] split1 = line.split("\\s+");
            n = Integer.valueOf(split1[0]);
            m = Integer.valueOf(split1[1]);
            Max = new int [n][m];
            Allocation = new int [n][m];
            Need = new int [n][m];;
            Work = new int[m];
            finished = new boolean[n];
            line = bfr.readLine();
            String[] split2 = line.split("\\s+");
            for (int i=0;i<split2.length;i++){
                Work[i] = Integer.parseInt(split2[i]);
            }
            line = bfr.readLine();
            int i=0;
            while ((line = bfr.readLine())!=null){
                String[] split = line.split("\\s+");
                for (int j=0;j<m;j++){
                    Max[i][j] = Integer.parseInt(split[j]);
                    Allocation[i][j] = Integer.parseInt(split[m+j]);
                    Work[j] -= Allocation[i][j];
                    Need[i][j] = Max[i][j]-Allocation[i][j];
                }
                i++;
            }
            bfr.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void init(){
        for (int i=0;i<finished.length;i++){
            finished[i] = false;
        }
    }
    public void bank(){
        export();
        init();
        System.out.println("pName\tWork\tMax\t\t\tAllocation\t\tNeed\tW_A\t");
        while (isNoFinished()){
            boolean flag = false;//用于判断每次遍历所有进程是否有进程被处理，如果没有的话说明无安全序列
            for (int i=0;i<n;i++){
                int k = 0;//用于判断第i个进程是否可以回收
                for (int j=0;j<m;j++){
                    if (!finished[i] && Work[j]>=Need[i][j])
                        k++;
                }
                if (k == m){
                    finished[i] = true;
                    sequence+=i+"\t";
                    show(i);
                    for (int j=0;j<m;j++){
                        Work[j] += Allocation[i][j];
                    }
                    showW_A();
                    flag = true;
                }
            }
            if (!flag){
                System.out.println("无安全序列");
                break;
            }
        }
        System.out.println("执行序列为："+sequence);
    }
    public void show(int i){
        System.out.print(i+"\t");
        for (int j=0;j<m;j++){
            System.out.print(Work[j]+" ");
        }
        System.out.print("\t");
        for (int j=0;j<m;j++){
            System.out.print(Max[i][j]+" ");
        }
        System.out.print("\t");
        for (int j=0;j<m;j++){
            System.out.print(Allocation[i][j]+" ");
        }
        System.out.print("\t");
        for (int j=0;j<m;j++){
            System.out.print(Need[i][j]+" ");
        }
        System.out.print("\t");
    }
    public void showW_A(){
        for (int j=0;j<m;j++){
            System.out.print(Work[j]+" ");
        }
        System.out.println("\t");
    }
    //判断是否有还没完成的进程
    public boolean isNoFinished(){
        for (int i=0;i<finished.length;i++){
            if (!finished[i])
                return true;
        }
        return false;
    }
}
