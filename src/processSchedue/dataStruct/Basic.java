package processSchedue.dataStruct;

import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.utils.BitmapUtils;
import processSchedue.dataStruct.utils.PCBUtils;

import java.util.Scanner;

/**
 * @version v1.0
 * @ClassName: Basic
 * @Description: 实现进程的基本功能
 * @Author: ChenQ
 * @Date: 2021/10/3 on 15:30
 */
public class Basic {
    static Scanner sc = new Scanner(System.in);
    static Bitmap bitmap;
    static Bitmap exBitmap;
    static BitmapUtils bitmapUtils;
    static PCBUtils pcbUtils;
    public static void main(String[] args) {
        init();
        menu();
        while (true){
            switch (sc.nextInt()){
                case 0:
                    pcbUtils.addressMapping(exBitmap,1);
                    break;
                case 1:
                    System.out.println("进程空闲块数："+bitmap.getSize()+" ex:"+exBitmap.getSize());
                    pcbUtils.createPCB(bitmap,exBitmap,3);
                    break;
                case 2:
                    pcbUtils.timeOut();
                    break;
                case 3:
                    pcbUtils.pcbBlocked();
                    break;
                case 4:
                    pcbUtils.wakeUp();
                    break;
                case 5:
                    pcbUtils.killPcb(bitmap,exBitmap);
                    System.out.println("kill进程空闲块数："+bitmap.getSize()+" ex:"+exBitmap.getSize());
                    break;
                case 6:
                    pcbUtils.opt_test(exBitmap);
                case 7:
                    show();
                    break;
            }
        }

    }
    //显示内存和进程信息
    public static void show(){
        System.out.println("内存位示图");
        bitmap.showTmp();
        System.out.println("对换区位示图");
        exBitmap.showTmp();
        pcbUtils.showPCB();
    }
    //内存初始化
    public static void init(){
        bitmapUtils = BitmapUtils.getBitmapUtils();
        pcbUtils = PCBUtils.getPCBUtil();
        System.out.println("请输入块大小和内存大小");
//        bitmap = new Bitmap(sc.nextInt(),sc.nextInt());
        bitmap = new Bitmap(1024,65536);
        bitmapUtils.initBitmap(bitmap);
        bitmap.showTmp();
        exBitmap = new Bitmap(bitmap.getBlockSize(),65536*2);
        bitmapUtils.initBitmap(exBitmap);
        exBitmap.showTmp();
    }
    public static void menu(){
        System.out.println("1.创建进程");
        System.out.println("2.时间片到");
        System.out.println("3.阻塞进程");
        System.out.println("4.唤醒进程");
        System.out.println("5.结束进程");
        System.out.println("6.显示进程");
    }


}
