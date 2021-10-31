package FileManager.dataStruct;

import FileManager.utils.FCBUtils;
import processSchedue.dataStruct.Bitmap;
import processSchedue.dataStruct.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @version v1.0
 * @ClassName: Main
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/30 on 18:54
 */
public class Main {
    static FCB root = new FCB();
    static FAT fat = FAT.getFat(64);
    static BitmapUtils bitmapUtils = BitmapUtils.getBitmapUtils();
    static Bitmap bitmap =new Bitmap(1024,65536);
    static FCBUtils fcbUtils = FCBUtils.getInstance();
    static final int FF = 0xFFFFF;//文件或目录尾
    static String path = "";

    public static void main(String[] args){
        init();
        Scanner sc = new Scanner(System.in);
        path = ""+root.getName();
        while (true) {
            showPath(path);
            String command = sc.nextLine();
            command = command.trim();
            String[] split = command.split("\\s+");
            switch (split[0]){
                case "mk":
                case "MK":{
                    if (split.length == 3){
                        fcbUtils.MK_Command(split[1],split[2],'1',root,bitmap);
                    }else if (split.length == 2){
                        fcbUtils.MK_Command(split[1],"0",'1',root,bitmap);
                    }else {
                        System.out.println(command+"语法错误");
                    }
                }
                break;
                case "md":
                case "MD":{
                    if (split.length == 2){
                        fcbUtils.MK_Command(split[1],"0",'2',root,bitmap);
                    }else {
                        System.out.println(command+"语法错误");
                    }
                }
                break;
                case "FAT":
                case "fat":{
                    System.out.println("FAT:");
                    fat.show();
                }
                break;
                case "info":
                case "INFO":{
                    System.out.println("磁盘空间：");
                    bitmap.showTmp();
                }
                break;
                case "dir":
                case "DIR":{
                    fcbUtils.DIR_Command(root);
                }
                break;
                case "cd":
                case "CD":{
                    if (split.length == 2){
                        split[1] = split[1].trim();
                        if (split[1].equals("\\")){
                            cd_com(split[1]);
                            break;
                        }
                        String[] split1 = split[1].split("\\\\");
                        if (split1[0].equals("")){
                            split1[0] = "\\";
                        }
                        //定义两个用于保存数据的变量，若跳转失败可以还原root和path
                        FCB fcbTmp = root;
                        String pathTmp = path;
                        for (int i=0;i<split1.length;i++){
                            if(!cd_com(split1[i])){
                                //如果没有跳转成功
                                System.out.println("跳转失败");
                                root = fcbTmp;
                                path = pathTmp;
                            }
                        }
                    }
                }
                break;
                case "del":
                case "DEL":{
                    if (split.length == 2){
                        fcbUtils.DEL_Command(split[1],root,bitmap);
                    }else {
                        System.out.println(command+"语法错误");
                    }
                }
                break;
                case "exit":
                case "EXIT":
                    return;
                case "tree":
                case "TREE":{
                    fcbUtils.tree_Command(0,null);
                }
                break;
                case "help":
                case "HELP":{
                    commandHelp();
                }
                break;
                default:
                    System.out.println("命令错误，输入help可以查看命令提示！");
                    break;
            }
        }
    }
    public static void init(){
        bitmapUtils.initBitmap(bitmap);
        root.setName("\\");
        root.setType('2');
        root.setSize(0);
        root.setUpper(root);
        root.setFirstBlock(0);
        fat.setFatValue(0,FF);
        bitmap.setBit(0,0,1);//根目录为位示图第一块
        fcbUtils.addFcb(root,root);
    }

    public static void commandHelp(){
        System.out.println("mk filename:创建一个大小为0的文件");
        System.out.println("mk filename size:创建一个指定大小的文件");
        System.out.println("md directoryname:创建一个空文件夹");
        System.out.println("del filename:删除指定的文件或指定的空文件夹");
        System.out.println("cd path:实现绝对路径或相对路径的跳转");
        System.out.println("dir:显示当前目录下的文件或文件夹信息");
        System.out.println("info:查看磁盘块占用情况");
        System.out.println("fat:查看fat表");
        System.out.println("tree:查看树形结构");
        System.out.println("help:查看帮助");
        System.out.println("exit:结束");
    }
    public static void showPath(String path){
        System.out.print("D:");
//        path = path+"\\"+root.getName();
        System.out.print(path+">");
    }
    public static boolean cd_com(String split_1){
        boolean flag = true;//判断是否跳转成功
        if ("..".equals(split_1)){
            if (!root.getName().equals("\\")){
                //如果不是已经在根目录下
                path = path.substring(0,path.indexOf(root.getName())-1);
                root = root.getUpper();
            }
        }else if (split_1.startsWith("\\")){
            //                            String[] split1 = split[1].split("\\\\");
            if (!root.getName().equals("\\")){
                //如果不是已经在根目录下
                path = "\\";
                root = fcbUtils.getFCBByIndex(0);
            }
        }else if (".".equals(split_1)){
            //获得当前目录
        }else {
            List list = new ArrayList();//用于传值
            list.add(path);
            list.add(root);
            flag =  fcbUtils.CD_Command(split_1.trim(), list);
            path = (String) list.get(0);
            root = (FCB) list.get(1);
        }
        return flag;
    }
}
