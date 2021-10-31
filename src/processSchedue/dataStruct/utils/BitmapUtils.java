package processSchedue.dataStruct.utils;

import java.util.Random;
import processSchedue.dataStruct.Bitmap;
/**
 * @version v1.0
 * @ClassName: BitmapUtils
 * @Description: 位示图工具类
 * @Author: ChenQ
 * @Date: 2021/10/8 on 17:09
 */
public class BitmapUtils {
    private Random random = new Random();
    private static BitmapUtils bitmapUtils;
    private BitmapUtils(){

    }

    public static BitmapUtils getBitmapUtils() {
        if (bitmapUtils == null){
            bitmapUtils = new BitmapUtils();
        }
        return bitmapUtils;
    }

    //设置随机位示图
    public void initBitmap(Bitmap bitmap){
        int itmp[] = new int[bitmap.getUnitSize()];
        for (int i=0;i<itmp.length;i++){
            itmp[i] = random.nextInt(46)+10;
//            System.out.print(itmp[i]+" ");
        }
        bitmap.setTmp(itmp);
        bitmap.setShowtmp();
        bitmap.setBit(0,0,0);//位示图的第一块空出来存储根目录
    }
    //获得第一个空闲块的位置
    public int getNullSpace(Bitmap bitmap){
        for (int j = 0; j < bitmap.getUnitSize(); j++) {
            boolean flag = false;
            for (int k = 0; k < 8; k++) {
                if (bitmap.getBit(bitmap.getTmp()[j], k) == 0) {
                    return j * 8 + k;
                }
            }
        }
        return -1;
    }
}

