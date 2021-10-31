package processSchedue.dataStruct.utils;

import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.PCB;
import processSchedue.dataStruct.PageLine;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * @version v1.0
 * @ClassName: PCBUtils
 * @Description: PCB的工具类
 * @Author: ChenQ
 * @Date: 2021/10/8 on 15:18
 */
public class PCBUtils {
    private Queue<PCB> ready;
    private Queue<PCB> running;
    private Queue<PCB> block;
    private static PCBUtils pcbUtils;

    Scanner sc = new Scanner(System.in);
    private PCBUtils(){
        ready = new LinkedList<>();
        running = new LinkedList<>();
        block = new LinkedList<>();
    }
    public static PCBUtils getPCBUtil(){
        if (pcbUtils == null){
            pcbUtils = new PCBUtils();
        }
        return pcbUtils;
    }

    //创建进程
    public void createPCB(Bitmap bitmap, Bitmap exBitmap, int blockSize){
        System.out.println("请输入进程名和进程大小");
        String name = sc.next();
        int space = sc.nextInt();
        if (space>exBitmap.getBlockSize()*(exBitmap.getUnitSize()-blockSize)){
            System.out.println("内存空间不足");
            return;
        }
        PCB pcb = new PCB(name,space);

        System.out.println("space:"+space);
        //分配内存
        allocateMemory(space,pcb,bitmap,exBitmap,blockSize);
        ready.offer(pcb);
        runProcess();
    }

    //分配内存
    /**
     *
     * @param space 进程大小
     * @param pcb 进程
     * @param bitmap 内存位示图
     * @param exBitmap 对换区位示图
     * @param blockSize 内存分配给改进程的块数
     */
    private void allocateMemory(int space, PCB pcb, Bitmap bitmap, Bitmap exBitmap, int blockSize){
        //分配内存区域
        for (int i=0;i<(space-1+bitmap.getBlockSize())/bitmap.getBlockSize()&&i<blockSize;i++) {
            for (int j = 0; j < bitmap.getUnitSize(); j++) {
                boolean flag = false;
                for (int k = 0; k < 8; k++) {
                    if (bitmap.getBit(bitmap.getTmp()[j], k) == 0) {
                        PageLine pageLine = new PageLine(i, j * 8 + k, 1, -1);
                        pcb.getPageTable().add(pageLine);
                        bitmap.setBit(j, k, 1);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
        //如果进程很小，刚好能全部进内存，则进程创建结束
        if (space<=blockSize*bitmap.getBlockSize()) {
            return;
        }
        space-=(blockSize*bitmap.getBlockSize());
        System.out.println("space:"+space);

        //分配对换区空间
        System.out.println("分配对换区空间"+((space+exBitmap.getBlockSize()-1)/exBitmap.getBlockSize()));
        for (int i=0;i<(space+exBitmap.getBlockSize()-1)/exBitmap.getBlockSize();i++){
            System.out.println("对换区 i="+i);
            boolean flag = false;
            for (int j=0;j<exBitmap.getUnitSize();j++){
                for (int k=0;k<8;k++){
                    if (exBitmap.getBit(exBitmap.getTmp()[j],k) == 0){
                        PageLine pageLine = new PageLine(i+blockSize,-1,0,j*8+k);
                        pcb.getPageTable().add(pageLine);
                        exBitmap.setBit(j,k,1);
                        flag = true;
                        break;
                    }
                }
                if (flag){
                    break;
                }
            }
        }
    }
    //时间片到
    public void timeOut(){
        if (running.size()!=0){
            ready.offer(running.poll());
        }
        runProcess();
    }
    //进程阻塞
    public void pcbBlocked(){
        if (running.size()!=0){
            block.offer(running.poll());
        }
        runProcess();
    }
    //唤醒进程
    public void wakeUp(){
        if (block.size()!=0){
            ready.offer(block.poll());
        }
        runProcess();
    }
    //结束进程
    public void killPcb(Bitmap bitmap, Bitmap exBitmap){
        if (running.size()!=0){
            PCB pcb = running.poll();
            List<PageLine> pageTable = pcb.getPageTable();
            for (int i = 0 ;i<pageTable.size();i++){
                int blockId = pageTable.get(i).getBlockId();
                if (blockId != -1){
                    bitmap.setBit(blockId/8,blockId%8,0);
                    pageTable.get(i).setBlockId(-1)
                            .setP(0);
                    continue;
                }
                int esId = pageTable.get(i).getEsId();
                if (esId != -1){
                    exBitmap.setBit(esId/8,esId%8,0);
                    pageTable.get(i).setEsId(-1);
                }
            }
            pcb.setPageTable(null);
            FIFOUtils.kill();
            LRUUtils.kill();
        }
        runProcess();
    }
    //执行进程
    private void runProcess(){
        if (ready.size()!=0 && running.size() == 0){
            FIFOUtils.kill();
            LRUUtils.kill();
            running.offer(ready.poll());
            List<PageLine> pageTable = running.peek().getPageTable();

            for (int i=0;i<pageTable.size();i++){
                if (pageTable.get(i).getP()==1){
                    FIFOUtils.initAdd(pageTable.get(i));
                    LRUUtils.initAdd(pageTable.get(i));
                }
            }
        }
    }

    public void showPCB(){
        System.out.println("就绪队列：");
        for (PCB pcb:ready){
            System.out.println(pcb);
        }
        System.out.println("执行队列：");
        for (PCB pcb:running){
            System.out.println(pcb);
        }
        System.out.println("阻塞队列：");
        for (PCB pcb:block){
            System.out.println(pcb);
        }
    }
    //地址映射
    public void addressMapping(Bitmap exBitmap,int flag){
        if (running.size()==0){
            System.out.println("当前无正在运行的进程，不可访问");
            return;
        }
        System.out.println("请输入十进制逻辑地址");
        int address = sc.nextInt();
        if (address>=running.peek().getSpace()){
            System.out.println("访问越界");
            return;
        }
        int count = (int)(Math.log(exBitmap.getBlockSize())/Math.log(2.0));//移动的位数
        int pageId = address>>count;//页号
        int mask = (0xffffffff)<<count;
        mask = ~mask;
        int offset = mask&address;//页内地址
        System.out.println("页号："+pageId+" 页内地址："+offset);
        if (flag==0){
            //FIFO算法
            FIFOUtils.FIFO(pageId,running.peek(),exBitmap);
            FIFOUtils.show();
            FIFOUtils.showQueue();
        }else {
            //LRU算法
            LRUUtils.LRU(pageId,running.peek(),exBitmap);
            LRUUtils.show();
            LRUUtils.showQueue();
        }
    }
    public void opt_test(Bitmap exBitmap){
        PCB pcb = running.peek();
        String sequence ="";
        List<Integer> sequence_1 = FIFOUtils.getSequence();
        for (int i=0;i<sequence_1.size();i++){
            sequence+=sequence_1.get(i);
        }
        for (int i=0;i<sequence.length();i++){
            OPTUtils.OPT(Integer.parseInt(sequence.charAt(i)+""),pcb,exBitmap,sequence);
        }
        OPTUtils.show();
    }
}