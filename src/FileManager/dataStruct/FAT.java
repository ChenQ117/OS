package FileManager.dataStruct;

import FileManager.utils.FCBUtils;
import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.utils.BitmapUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: FAT
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/30 on 18:48
 */
public class FAT {
    private int size;
    private List<Integer> fatList;
    private static FAT fat;
    static BitmapUtils bitmapUtils = BitmapUtils.getBitmapUtils();
    static FCBUtils fcbUtils = FCBUtils.getInstance();

    public static final int FF = 0xFFFFF;//文件或目录尾
    private FAT(int size){
        this.size = size;
        fatList = new LinkedList<Integer>();
        for (int i=0;i<size;i++){
            fatList.add(i,-1);
        }
    }
    public static FAT getFat(int size){
        if (fat == null){
            fat = new FAT(size);
        }
        return fat;
    }
    public void setFatValue(int index,int value){
        fatList.set(index,value);
    }

    public int getFatValue(int index){
        return fatList.get(index);
    }
    public int setFat(int block,Bitmap bitmap){
        int index = bitmapUtils.getNullSpace(bitmap);
        int firstBlock = index;
        bitmap.setBit(index/8,index%8,1);
        if (block>1){
            for (int i=0;i<block-1;i++){
                int value = bitmapUtils.getNullSpace(bitmap);
                fat.setFatValue(index,value);
                index = value;
                bitmap.setBit(index/8,index%8,1);
            }
        }
        fat.setFatValue(index,FF);
        return firstBlock;
    }
    public void show(){
        for (int i=0;i<fatList.size();i++){
            System.out.println(i+":"+fatList.get(i));
        }
    }
}
