package processSchedue.dataStruct;

import processSchedue.dataStruct.utils.FIFOUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @version v1.0
 * @ClassName: VisitMethod
 * @Description: 访问方式的抽象类
 * @Author: ChenQ
 * @Date: 2021/10/9 on 12:40
 */
public abstract class VisitMethod {
    protected   Queue<PageLine> visitQueue = new LinkedList<>();
    protected   int hit;//命中次数
    protected   int miss;//缺页
    protected   int substitute;//置换
    protected   double missPercent;//缺页率
    protected   List<Integer> sequence = new ArrayList<>();//命中序列
    protected   FIFOUtils fifoUtils;




    //初始创建进程时
    public  void initAdd(PageLine pageLine){
        miss++;
        visitQueue.offer(pageLine);
        sequence.add(pageLine.getPageId());
        missPercent = (double)(miss)/(hit+miss);
    }
    //置换和命中的情况
    public abstract void visit(int pageId, PCB pcb, Bitmap exBitmap);

    public  void show(){
        System.out.println("访问序列："+sequence);
        System.out.println("命中次数："+hit);
        System.out.println("缺页次数："+miss);
        System.out.println("置换次数："+substitute);
        System.out.println("缺页率："+missPercent);
    }
    public  void showQueue(){
        for (PageLine pageLine: visitQueue){
            System.out.println(pageLine);
        }
    }
    //进程结束时
    public  void kill(){
        miss=0;
        hit =0;
        substitute=0;
        missPercent = (double)(miss)/(hit+miss);
        visitQueue.clear();
        sequence.clear();
    }
}
