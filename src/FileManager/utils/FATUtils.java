package FileManager.utils;

/**
 * @version v1.0
 * @ClassName: FATUtils
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/31 on 0:30
 */
public class FATUtils {
    private static FATUtils fatUtils;
    private FATUtils(){
        fatUtils = new FATUtils();
    }
    public static FATUtils getInstance(){
        if (fatUtils == null){
            fatUtils = new FATUtils();
        }
        return fatUtils;
    }

}
