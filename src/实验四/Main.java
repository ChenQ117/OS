package 实验四;

import java.util.List;
import java.util.Scanner;

/**
 * @version v1.0
 * @ClassName: Main
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/11/22 on 11:24
 */
public class Main {

    public static void main(String[] args) {
        /*Scanner sc = new Scanner(System.in);
        List<PCB> pcbList= Utils.infoPcb();
        Utils.showInit(pcbList);
        System.out.println("=============================================FCFS=============================================");
        List<PCB> fcfs = Utils.FCFS(pcbList);
        Utils.showResult(fcfs);
        System.out.println("=============================================SJF=============================================");
        List<PCB> sjf = Utils.SJF(pcbList);
        Utils.showResult(sjf);
        System.out.println("请输入时间片大小:");
        int q = sc.nextInt();
        System.out.println("=============================================TimeTurn=============================================");
        List<PCB> timeTurn = Utils.timeTurn(pcbList, q);
        Utils.showResult(timeTurn);
        System.out.println("=============================================HRRN=============================================");
        List<PCB> HRRN = Utils.HRRN(pcbList);
        Utils.showResult(HRRN);
        System.out.println("=============================================MutiQueue=============================================");
        List<PCB> MutiList = Utils.MutiQueen(pcbList);
        Utils.showResult(MutiList);*/
        BankUtils bankUtils = new BankUtils();
        bankUtils.bank();
    }

}
