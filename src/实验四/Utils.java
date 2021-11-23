package 实验四;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @version v1.0
 * @ClassName: Utils
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/11/22 on 11:54
 */
public class Utils {
    static final String fileName = "src/info.txt";
    static final String FCFS = "FCFS";
    static final String SJF = "FCFS";
    static final String Q = "Q";
    static final int Q1 = 1;
    static final int Q2 = 2;
    static final int Q3 = 4;
    static final double x = 0.7;//比例因子
    public static List<PCB> infoPcb(){
        List<PCB> pcbList = new LinkedList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
            String line = "";
            while ((line = bfr.readLine())!=null){
                String[] split = line.split("\\s+");
                String name = split[0];
                int size = Integer.parseInt(split[1]);
                int arrival_time = Integer.parseInt(split[2]);
                int burst_time = Integer.parseInt(split[3]);
                PCB pcb = new PCB(name,size,arrival_time,burst_time);
                if (split.length==5){
                    pcb.setPriority(Double.parseDouble(split[4]));
                }
                pcbList.add(pcb);
            }
            bfr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return pcbList;
    }
    public static void showInit(List<PCB> pcbList){
        for (int i=0;i<pcbList.size();i++){
            System.out.println(pcbList.get(i).toString1());
        }
    }
    public static void showResult(List<PCB> pcbList){
        /*pcbList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB pcb, PCB t1) {
                return pcb.getName().compareTo(t1.getName());
            }
        });*/
        System.out.println("进程名\t\t到达时间\t\t服务时间\t\t开始时间\t\t完成时间\t\t周转时间\t\t带权周转时间\t\t优先级");
        double sumT = 0;
        double sumW = 0;
        for (int i=0;i<pcbList.size();i++){
            System.out.println(pcbList.get(i).toString2());
            sumT+=pcbList.get(i).getRun_time();
            sumW+=pcbList.get(i).getW();
        }
        System.out.println("平均周转时间："+String.format("%.2f",sumT/pcbList.size()));
        System.out.println("平均带权周转时间："+String.format("%.2f",sumW/pcbList.size()));
    }

    public static List<PCB> getCopyList(List<PCB> pcbList){
        List<PCB> copyList = new ArrayList<>();
        //深拷贝
        for (int i=0;i<pcbList.size();i++){
            PCB pcb1 = pcbList.get(i);
            PCB pcb = new PCB(pcb1.getName(),
                    pcb1.getSize(),
                    pcb1.getArrival_time(),
                    pcb1.getBurst_time());
            copyList.add(pcb);
        }
        return copyList;
    }

    public static List<PCB> FCFS(List<PCB> pcbList){
        List<PCB> fcfsList = getCopyList(pcbList);
        //计算优先级
        for (int i=0;i<fcfsList.size();i++){
            int k=i;
            for (int j=i+1;j<fcfsList.size();j++){
                if (fcfsList.get(j).getArrival_time()<fcfsList.get(k).getArrival_time()){
                    k=j;
                }
            }
            if (k!=i){
                PCB pcb = fcfsList.get(k);
                fcfsList.set(k,fcfsList.get(i));
                fcfsList.set(i,pcb);
            }
            fcfsList.get(i).updatePriority(x,i+1);
        }
        return updatePcb(fcfsList);
    }

    public static List<PCB> SJF(final List<PCB> pcbList){
        List<PCB> SJFList = getCopyList(pcbList);
        //计算优先级
        for (int i=0;i<SJFList.size();i++){
            int k=i;
            for (int j=i+1;j<SJFList.size();j++){
                if (SJFList.get(j).getBurst_time()<SJFList.get(k).getBurst_time()){
                    k=j;
                }
            }
            if (k!=i){
                PCB pcb = SJFList.get(k);
                SJFList.set(k,SJFList.get(i));
                SJFList.set(i,pcb);
            }
            SJFList.get(i).updatePriority(x,i+1);
        }

        return updatePcb(SJFList);
    }
    public static List<PCB> updatePcb(final List<PCB> pcbList){
        //根据优先级排序
        for (int i=0;i<pcbList.size();i++){
            int k=i;
            for (int j=i+1;j<pcbList.size();j++){
                if (pcbList.get(j).getPriority()<pcbList.get(k).getPriority()){
                    k=j;
                }
            }
            if (k!=i){
                PCB pcb = pcbList.get(k);
                pcbList.set(k,pcbList.get(i));
                pcbList.set(i,pcb);
            }
        }
        int currentTime = 0;
        int size = pcbList.size();
        List<PCB> finishedPCB = new LinkedList<>();
        while (finishedPCB.size()<size){
            for (int i=0;i<pcbList.size();i++){
                if (pcbList.get(i).getArrival_time()<=currentTime){
                    PCB remove = pcbList.remove(i);
                    remove.setStart_time(currentTime);
                    remove.setFinished_time(currentTime+remove.getBurst_time());
                    remove.updateRun_time();
                    remove.updateW();
                    finishedPCB.add(remove);
                    currentTime = remove.getFinished_time();
                    break;
                }
            }

        }
        return finishedPCB;
    }
    public static List<PCB> timeTurn( final List<PCB>  pcbList, int q){
        List<PCB> timeList = getCopyList(pcbList);
        //按到达时间从大到小排序，先响应list的末尾的进程
        for (int i=0;i<timeList.size();i++){
            int k=i;
            for (int j=i+1;j<timeList.size();j++){
                if (timeList.get(j).getArrival_time()<timeList.get(k).getArrival_time()){
                    k=j;
                }
            }
            if (k!=i){
                PCB pcb = timeList.get(k);
                timeList.set(k,timeList.get(i));
                timeList.set(i,pcb);
            }
        }
        showResult(timeList);
        List<PCB> finishedList = new LinkedList<>();
        int currentTime = 0;
        int size = timeList.size();
        while (finishedList.size()<size){
            for (int i=0;i<timeList.size();i++){
                PCB pcb = timeList.get(i);
                if (pcb.getArrival_time()<=currentTime){
                    pcb.setServer_time(q);
                    if (pcb.getStart_time() == -1){
                        pcb.setStart_time(currentTime);
                    }
                    //如果服务完成
                    if (pcb.getServer_time()<=0){
                        currentTime += (pcb.getServer_time()+q);
                        finishedList.add(pcb);
                        timeList.remove(i);
                        i--;//每回删除i要前移
                        System.out.print(pcb.getName()+" "+currentTime+",");
                        pcb.setFinished_time(currentTime);
                        pcb.updateRun_time();
                        pcb.updateW();
                    }else {
                        currentTime += q;
                        System.out.print(pcb.getName()+" "+currentTime+",");
                    }
                }
            }
        }
        System.out.println();
        return finishedList;
    }
    //先将队列按照到达时间排序
    public static void sort(List<PCB> HRRNList){
        for (int i=0;i<HRRNList.size();i++){
            int k=i;
            for (int j=i+1;j<HRRNList.size();j++){
                if (HRRNList.get(j).getArrival_time()<HRRNList.get(k).getArrival_time()){
                    k=j;
                }
            }
            if (k!=i){
                PCB pcb = HRRNList.get(k);
                HRRNList.set(k,HRRNList.get(i));
                HRRNList.set(i,pcb);
            }
        }
    }
    public static List<PCB> HRRN(final List<PCB> pcbList){
        List<PCB> HRRNList = getCopyList(pcbList);
        int currentTime = 0;
        sort(HRRNList);
        List<PCB> finishedList = new LinkedList<>();
        int size = pcbList.size();
        while (finishedList.size()<size){
            //每次响应之前先更新响应比
            for (int i=0;i<HRRNList.size();i++){
                HRRNList.get(i).updateResponse(currentTime);
            }
            //按照响应比从高到低排序
            for (int i=0;i<HRRNList.size();i++){
                int k = i;
                for (int j=i+1;j<HRRNList.size();j++){
                    if (HRRNList.get(k).getPriority()<HRRNList.get(j).getPriority()){
                        k = j;
                    }
                }
                if (k!=i){
                    PCB pcb = HRRNList.get(k);
                    HRRNList.set(k,HRRNList.get(i));
                    HRRNList.set(i,pcb);
                }
            }
//            showResult(HRRNList);
            for (int i=0;i<HRRNList.size();i++){
                if (HRRNList.get(i).getArrival_time()<=currentTime){
                    PCB pcb = HRRNList.remove(i);
                    pcb.setStart_time(currentTime);
                    currentTime += pcb.getBurst_time();
                    pcb.setFinished_time(currentTime);
                    pcb.updateRun_time();
                    pcb.updateW();
                    finishedList.add(pcb);
                    System.out.print(pcb.getName()+" "+currentTime+",");
                    break;
                }
            }
        }
        System.out.println();
        return finishedList;
    }
    //进行一次轮转
    private static int turn(Queue<PCB> queue1,Queue<PCB> queue2,int currentTime,List<PCB> finishedList,int q){
        PCB pcb = queue1.peek();
        pcb.setServer_time(q);
        if (pcb.getStart_time() == -1){
            pcb.setStart_time(currentTime);
        }
        //如果服务完成
        if (pcb.getServer_time()<=0){
            currentTime += (pcb.getServer_time()+q);
            finishedList.add(pcb);
            queue1.poll();
            System.out.print(pcb.getName()+" "+currentTime+",");
            pcb.setFinished_time(currentTime);
            pcb.updateRun_time();
            pcb.updateW();
        }else {
            currentTime += q;
            System.out.print(pcb.getName()+" "+currentTime+",");
            queue2.offer(queue1.poll());
        }
        return currentTime;
    }
    public static List<PCB> MutiQueen(final List<PCB> pcbList){
        List<PCB> MutiList = getCopyList(pcbList);
        List<PCB> finishedList = new LinkedList<>();
        //先将队列按照到达时间排序
        sort(MutiList);
        int currentTime = 0;
        Queue<PCB> q1 = new LinkedList<>();
        Queue<PCB> q2 = new LinkedList<>();
        Queue<PCB> q3 = new LinkedList<>();
        for (int i=0;i<MutiList.size();i++){
            if (MutiList.get(i).getArrival_time()<=currentTime){
                q1.offer(MutiList.remove(i--));
            }
        }
        int size = pcbList.size();
        //每次进行完一次进程的处理后就判断有没有新的进程加入
        while (finishedList.size()<size){
            for (int i=0;i<MutiList.size();i++){
                if (MutiList.get(i).getArrival_time()<=currentTime){
                    q1.offer(MutiList.remove(i--));
                }
            }
            if (q1.size()>0){
                currentTime = turn(q1, q2, currentTime, finishedList,Q1);
            }else if (q2.size()>0){
                currentTime = turn(q2, q3, currentTime, finishedList, Q2);
            }else if (q3.size()>0){
                currentTime = turn(q3,q3,currentTime,finishedList,Q3);
            }
        }
        System.out.println();
        return finishedList;
    }
}
