package 实验四;

/**
 * @version v1.0
 * @ClassName: PCB
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/11/22 on 10:48
 */
public class PCB {
    private String name;
    private int size;//进程大小
    private int arrival_time;//到达时间
    private int burst_time;//服务时间
    private int start_time;//开始时间
    private int finished_time;//结束时间
    private int run_time;//已运行时间，周转时间
    private int server_time;//剩余服务时间
    private double w;//带权周转时间
    private double priority;//优先级

    public int getServer_time() {
        return server_time;
    }

    public void setServer_time(int q) {
        this.server_time -= q;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public double getPriority() {
        return priority;
    }

    public void updateW() {
        w = run_time*1.0/burst_time;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public PCB(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public PCB(String name, int size, int arrival_time, int burst_time) {
        this.name = name;
        this.size = size;
        this.arrival_time = arrival_time;
        this.burst_time = burst_time;
        this.server_time = burst_time;
        this.start_time = -1;
    }

    public PCB() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(int arrival_time) {
        this.arrival_time = arrival_time;
    }

    public int getBurst_time() {
        return burst_time;
    }

    public void setBurst_time(int burst_time) {
        this.burst_time = burst_time;
    }

    public int getFinished_time() {
        return finished_time;
    }

    public void setFinished_time(int finished_time) {
        this.finished_time = finished_time;
    }

    public int getRun_time() {
        return run_time;
    }

    public void updateRun_time() {
        this.run_time = finished_time-arrival_time;
    }

    public double getW() {
        return w;
    }

    public String toString1() {
        return  name +
                "\t " + arrival_time +
                "\t " + burst_time +
                "\t " + finished_time +
                "\t " + run_time
                ;
    }


    public String toString2() {
        return  name +
                "\t\t\t " + arrival_time +
                "\t\t\t " + burst_time +
                "\t\t\t " + start_time +
                "\t\t\t " + finished_time +
                "\t\t\t " + run_time +
                "\t\t\t " + String.format("%.2f",w) +
                "\t\t\t " + String.format("%.2f",priority)
                ;
    }

    //计算优先级，x为比例因子，a为该进程在该调度算法下的优先级
    public void updatePriority(double x,double a){
        priority = x*a+priority*(1-x);
    }
    public void updateResponse(int current){
        double waite = current-arrival_time;//等待时间
        if (waite>=0)
            priority = (waite+burst_time)/burst_time;
        else
            priority = 0;
    }
}
