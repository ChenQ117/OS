package processSchedue.dataStruct.utils;

import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.PCB;
import processSchedue.dataStruct.PageLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @version v1.0
 * @ClassName: FIFOUtils
 * @Description: 先进先出访问方式
 * @Author: ChenQ
 * @Date: 2021/10/8 on 19:59
 */
public class OPTUtils {
    private static Queue<PageLine> OPTQueue = new LinkedList<>();
    private static int hit;//命中次数
    private static int miss;//缺页
    private static int substitute;//置换
    private static double missPercent;//缺页率
    private static List<Integer> sequence = new ArrayList<>();//命中序列
    private static OPTUtils optUtils;
    private OPTUtils(){
        OPTQueue = new LinkedList<>();
        hit = 0;
        miss = 0;
        substitute = 0;
        missPercent = (double)(miss)/(hit+miss);
        sequence = new ArrayList<>();
    }

    public static OPTUtils getOptUtils() {
        if (optUtils == null){
            optUtils = new OPTUtils();
        }
        return optUtils;
    }

    //初始创建进程时
    public static void initAdd(PageLine pageLine){
        miss++;
        OPTQueue.offer(pageLine);
        sequence.add(pageLine.getPageId());
        missPercent = (double)(miss)/(hit+miss);
    }
    //最佳置换 置换和命中的情况
    public static void OPT(int pageId, PCB pcb, Bitmap exBitmap, String sequence){
        List<PageLine> pageTable = pcb.getPageTable();
//        sequence.add(pageId);
        PageLine pageLine = pageTable.get(pageId);
        if (pageLine.getP()==1){
            //命中
            hit++;
            System.out.println("命中");
        }else {
            //置换
            substitute++;
            miss++;
            int oldEsId = pageLine.getEsId();

            int max_index = sequence.charAt(0);
            for(PageLine p: OPTQueue){
                int index = sequence.indexOf(p.getBlockId());
                if (index>max_index){
                    max_index = index;
                }
            }
            PageLine poll = OPTQueue.peek();
            for (int i = 0; i< OPTQueue.size(); i++){
                if (OPTQueue.peek().getBlockId()==max_index){
                    poll = OPTQueue.peek();
                }
            }
            poll.setP(0);//修改状态位

            //修改外存地址：找出对换区中的空闲块，将其分配给置换出来的程序段
            boolean flag = false;
            for (int j=0;j<exBitmap.getUnitSize();j++){
                for (int k=0;k<8;k++){
                    if (exBitmap.getBit(exBitmap.getTmp()[j],k) == 0){
//                        exBitmap.setBit(j,k,1);//修改位示图
                        poll.setEsId(j*8+k);
                        flag = true;
                        break;
                    }
                }
                if (flag){
                    break;
                }
            }

            pageLine.setBlockId(poll.getBlockId());
            pageLine.setP(1);
//            exBitmap.setBit(pageLine.getEsId()/8,pageLine.getEsId()%8,0);//修改位示图
            pageLine.setEsId(-1);
            OPTQueue.offer(pageLine);

            poll.setBlockId(-1);
            //输出置换信息
            System.out.println("置换"+poll.getPageId()+"号 old：内存"+pageLine.getBlockId()+"--->外存"+
                    poll.getEsId()+"  new：外存"+oldEsId+"--->内存"+pageLine.getBlockId());
        }
        missPercent = (double)(miss)/(hit+miss);
    }

    public static void show(){
        System.out.println("访问序列："+sequence);
        System.out.println("命中次数："+hit);
        System.out.println("缺页次数："+miss);
        System.out.println("置换次数："+substitute);
        System.out.println("缺页率："+missPercent);
    }
    public static void showQueue(){
        for (PageLine pageLine: OPTQueue){
            System.out.println(pageLine);
        }
    }
    //进程结束时
    public static void kill(){
        miss=0;
        hit =0;
        substitute=0;
        missPercent = (double)(miss)/(hit+miss);
        OPTQueue.clear();
        sequence.clear();
    }
}
