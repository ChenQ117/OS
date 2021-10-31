package FileManager.dataStruct;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version v1.0
 * @ClassName: FCB
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/30 on 16:35
 */
public class FCB {
    private String name;//文件名
    private int size;//文件大小
    private int firstBlock;//第一块块号
    private char type;//类型，1为文件，2为目录，0为已删除目录项
    private String dateTime;//创建时间和日期
    private FCB brother;//下一个兄弟结点
    private FCB child;//第一个孩子结点
    private FCB upper;//父结点 ..

    public FCB(){
        brother = null;
        child = null;
        size = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        dateTime = sdf.format(new Date());
        upper = this;
    }

    public FCB(String name, int size) {
        this.name = name;
        this.size = size;
        this.type = '1';
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        dateTime = sdf.format(new Date());
        this.brother = null;
        this.child = null;
        upper=null;
    }

    public FCB getBrother() {
        return brother;
    }

    public void setBrother(FCB brother) {
        this.brother = brother;
    }

    public FCB getChild() {
        return child;
    }

    public void setChild(FCB child) {
        this.child = child;
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

    public int getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock = firstBlock;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public FCB getUpper() {
        return upper;
    }

    public void setUpper(FCB upper) {
        this.upper = upper;
    }

    @Override
    public String toString() {
        if(this.type=='1'){
            return String.format("%-15s    %-8s    %-6d    %-20s",dateTime,"        ",size,name);
        }else {
            return String.format("%-15s    %-8s    %-6s    %-20s",dateTime,"<DIR>","",name);
        }

    }
}
