package FileManager.utils;

import FileManager.dataStruct.FAT;
import FileManager.dataStruct.FCB;
import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.PCB;
import processSchedue.dataStruct.PageLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: FCBUtils
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/30 on 20:09
 */
public class FCBUtils {
    private static List<FCB> fcbList = new LinkedList<>();
    private static FCBUtils fcbUtils;
    private static FAT fat;

    private FCBUtils(){

    }
    public static FCBUtils getInstance(){
        if (fcbUtils == null){
            fcbUtils = new FCBUtils();
            fcbList = new LinkedList<>();
            fat = FAT.getFat(64);
        }
        return fcbUtils;
    }
    public void setBrother(FCB fcb,String path){
        FCB brother = new FCB();
        for (int i =0;i<fcbList.size();i++){
            if (fcbList.get(i).getName().equals(path)){
                brother = fcbList.get(i);
            }
        }
        while (brother.getBrother()!=null){
            brother = brother.getBrother();
        }
        brother.setBrother(fcb);
    }
    public FCB isHasName(String name,FCB root){
        FCB head = root.getChild();
        if (head==null){
            return head;
        }
        while (head!=null){
            if (name.equals(head.getName())){
                return head;
            }
            head = head.getBrother();
        }
        return head;
    }
    //将创建的FCB加入到FCB集合中
    public void addFcb(FCB fcb,FCB root){
        fcbList.add(fcb);
        if (root == fcb){
            return;
        }
        FCB head = root.getChild();
        if (head == null){
            root.setChild(fcb);
            return;
        }
        while (head.getBrother()!=null){
            head = head.getBrother();
        }
        head.setBrother(fcb);
//        fcb.setUpper(root);
    }
    //列出文件目录
    public void DIR_Command(FCB root){
        FCB head = root.getChild();
        System.out.println(String.format("%-15s    %-8s    %-6s    %-20s",root.getDateTime(),"<DIR>","","."));
        System.out.println(String.format("%-15s    %-8s    %-6s    %-20s",root.getDateTime(),"<DIR>","",".."));
        while (head!=null){
            System.out.println(head.toString());
            head = head.getBrother();
        }
    }
    public FCB getFCBByIndex(int index){
        return fcbList.get(index);
    }
    //文件跳转
    public boolean CD_Command(String name,List list){
        boolean flag = false;//判断是否跳转成功
        String path = (String) list.get(0);
        FCB root = (FCB) list.get(1);
        FCB fcb = isHasName(name, root);
        if (fcb==null){
            System.out.println(name+"不存在，无法跳转");
            flag = false;
        }
        else if (fcb.getType() == '1'){
            System.out.println(name+"为一个文件，无法跳转");
            flag = false;
        }
        else if (fcb.getType() == '2'){
            if ("\\".equals(path)){
                path = "";
            }
            path =path+'\\'+name;
            root = fcb;
            flag = true;
            list.add(0,path);
            list.add(1,root);
        }
        return flag;
    }
    //创建文件或文件夹
    public void MK_Command(String name, String fileSize, char type, FCB root, Bitmap bitmap){
        try {
            if (isHasName(name,root)!=null){
                System.out.println(name+"文件或文件夹已存在，无法创建");
                return;
            }
            int size = Integer.parseInt(fileSize);
            int block = (size+bitmap.getBlockSize()-1)/bitmap.getBlockSize();
            if (block>bitmap.getSize()){
                System.out.println("磁盘空间不足，无法创建");
                return;
            }
            FCB fcb = new FCB(name,size);
            fcb.setType(type);
            fcb.setUpper(root);
            int firstBlock = fat.setFat(block,bitmap);
            fcb.setFirstBlock(firstBlock);
            addFcb(fcb,root);
        }catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println("语法错误");
        }
    }
    //删除文件
    public void DEL_Command(String name,FCB root,Bitmap bitmap){
        FCB fcb = isHasName(name,root);
        if (fcb==null){
            System.out.println(name+"文件或文件夹不存在，无法删除");
            return;
        }
        if (fcb.getType()=='2'&&fcb.getChild()!=null){
            System.out.println(name+"文件夹不为空，不允许删除");
        }else {
            int last = fcb.getFirstBlock();//找出在fat表中值为FF的下标
            int index = fat.getFatValue(last);
            while (index!=FAT.FF){
                last = index;
                bitmap.setBit(index/8,index%8,0);
                fat.setFatValue(index,-1);
                index = fat.getFatValue(index);
            }
            System.out.println("151 FCBU:"+last);
            bitmap.setBit(last/8,last%8,0);
            fat.setFatValue(last,-1);
            FCB upper = fcb.getUpper();
            updateFCB(upper,name);
            fcb.setBrother(null);
            fcb.setName("");
            fcb.setUpper(null);
            fcbList.remove(fcb);
        }
    }
    //更新upper目录下的文件与文件之间的父子兄弟关系
    public void updateFCB(FCB upper,String name){
        FCB child = upper.getChild();
        if (child.getName().equals(name)){
            upper.setChild(child.getBrother());
        }else{
            FCB brother = child;
            while (brother.getBrother()!=null){
                if (brother.getBrother().getName().equals(name)){
                    brother.setBrother(brother.getBrother().getBrother());
                    break;
                }
                brother = brother.getBrother();
            }
        }
    }
    //列出文件管理的树形结构
    public void tree_Command(int n,FCB head){
        if (head == null){
            if (fcbList.size()>0){
                head = fcbList.get(0);
            }
        }
        if (head!=null){
            head = head.getChild();
            while (head!=null){
                for (int i=0;i<n;i++){
                    System.out.print("————");
                }
                System.out.println(head.getName());
                n++;
                tree_Command(n,head);
                n--;
                head = head.getBrother();
            }
        }

    }
}
