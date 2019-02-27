package cn.yq.GUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取当前的时间
 */
public class Datetime {
    private static Logger logger = LoggerFactory.getLogger(Datetime.class);
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");    // 这里填写的是想要进行转换的时间格式

    public static String getDatetime(){
        // 获取当前时间
       Date date = new Date();
        String str = format.format(date);
        return str;
    }

    public static Date str2Datetime(String str){
        //字符串转为date
        Date date = null;
        try{
            date = format.parse(str);
        }catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }
    public static long dateDiff(String startTime, String endTime, String format) throws Exception {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        //获得两个时间的毫秒时间差异
        diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        long day = diff/nd;//计算差多少天
        long hour = diff%nd/nh;//计算差多少小时
        long min = diff%nd%nh/nm;//计算差多少分钟
        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
        logger.info("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
        return diff/ns;
    }
    public static void main(String[] args) {
        String TIME = getDatetime();
        try {
            long min = dateDiff("2019-01-12 16:55:53",TIME,"yyyy-MM-dd HH:mm:ss");
            System.out.println(min);
            if(min<388800)
                System.out.println("T");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
