package processSchedue.dataStruct;

/**
 * @version v1.0
 * @ClassName: Bitmap
 * @Description: 位示图
 * @Author: ChenQ
 * @Date: 2021/10/7 on 19:44
 */
public class Bitmap {
    private int blockSize;//块大小
    private int unitSize;//字节个数
    private char[] tmp;
    private int[] showtmp;
    private int size;//空闲的内存块数
    public Bitmap(){

    }

    public Bitmap(int blockSize, int unitSize) {
        this.blockSize = blockSize;
        this.unitSize = (unitSize/blockSize+7)/8;
        tmp = new char[this.unitSize];
        showtmp = new int[tmp.length*8];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public char[] getTmp() {
        return tmp;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public void setTmp(char[] tmp) {
        this.tmp = tmp;
    }

    public int[] getShowtmp() {
        return showtmp;
    }

    public void setShowtmp(int[] showtmp) {
        this.showtmp = showtmp;
    }

    public void setTmp(int[] itmp) {
        for (int i=0;i<itmp.length;i++){
            tmp[i] = (char)itmp[i];
//            System.out.print(tmp[i]+" ");
        }
    }

    //显示位示图
    public void showTmp(){
        setShowtmp();
        for (int i=0;i<showtmp.length;i++){
            if (i%8==0){
                System.out.println();
                System.out.print("第"+i/8+"字节");
            }
            System.out.print(showtmp[i]+" ");
        }
        System.out.println();
    }

    //判断某字节b的第bit_no位是1还是0
    public int getBit(char b,int bit_no){
        char me = (char) (1<<bit_no);
        if ((me&b)==0){
            return 0;
        }else {
            return 1;
        }
    }

    //设置某字节b的第bit_no位为1或0
    public char setBit(int index,int bit_no,int flag){
        char mask = (char)(1<<bit_no);
        if (flag==0){
            mask = (char) ~mask;
            tmp[index] = (char)(tmp[index]&mask);
            size++;
        }else {
            tmp[index] = (char) (tmp[index]|mask);
            size--;
        }
        return tmp[index];
    }


    public void setShowtmp() {
        size = 0;
        for (int i=0;i<tmp.length;i++){
            for (int j=0;j<8;j++){
                int bit = getBit(tmp[i],j);
                if (bit == 0){
                    size++;
                }
//                showtmp[i*8+j]=getBit(tmp[i],j);
                showtmp[i*8+j]=bit;
            }
        }
    }
}
