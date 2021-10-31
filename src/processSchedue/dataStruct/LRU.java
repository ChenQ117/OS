package processSchedue.dataStruct;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @version v1.0
 * @ClassName: LRU
 * @Description: 最近最久未访问
 * @Author: ChenQ
 * @Date: 2021/10/9 on 12:47
 */
public class LRU extends VisitMethod {
    //置换和命中的情况
    @Override
    public void visit(int pageId, PCB pcb, Bitmap exBitmap) {
        List<PageLine> pageTable = pcb.getPageTable();
        sequence.add(pageId);
        PageLine pageLine = pageTable.get(pageId);
        if (pageLine.getP()==1){
            //命中
            hit++;
            Queue<PageLine> pageLines = new LinkedList<>();
            for (PageLine pageLine1:visitQueue){
                if (pageLine1.equals(pageLine)){
                    continue;
                }
                pageLines.offer(pageLine1);
            }
            pageLines.offer(pageLine);
            visitQueue = pageLines;
            System.out.println("命中");
        }else {
            //置换
            substitute++;
            miss++;
            int oldEsId = pageLine.getEsId();

            PageLine poll = visitQueue.poll();
            poll.setP(0);//修改状态位

            //修改外存地址：找出对换区中的空闲块，将其分配给置换出来的程序段
            boolean flag = false;
            for (int j=0;j<exBitmap.getUnitSize();j++){
                for (int k=0;k<8;k++){
                    if (exBitmap.getBit(exBitmap.getTmp()[j],k) == 0){
                        exBitmap.setBit(j,k,1);//修改位示图
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
            exBitmap.setBit(pageLine.getEsId()/8,pageLine.getEsId()%8,0);//修改位示图
            pageLine.setEsId(-1);
            visitQueue.offer(pageLine);

            poll.setBlockId(-1);
            //输出置换信息
            System.out.println("置换"+poll.getPageId()+"号 old：内存"+pageLine.getBlockId()+"--->外存"+
                    poll.getEsId()+"  new：外存"+oldEsId+"--->内存"+pageLine.getBlockId());
        }
        missPercent = (double)(miss)/(hit+miss);
    }
}
