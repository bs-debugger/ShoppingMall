package com.xq.live.web.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by ss on 2018/12/6.
 */
public class GetUserInfoUtil {

    public static String[] telFirstNew="1342995,1898688,1898689,1898690,1898691,1580728,1899594,1532796,1342996,1342997,1345124,1345125,1345126,1345127,1899593,1337782,1319729,1320726,132179,1322724,1322760,1322761,1323565,1323566,1323567,1347732,1336719,1532797,1338713,1338714,1338715,1339728,1339729,1530728,1532794,1532795,1347731,1334979,1897192,1347729,1359786,1898687,1897250,1897249,1897248,1897247,1897246,1359377,1897193,1359376,1897191,1897190,1890728,1534727,1534661,1534660,1533735,1470712,1470713,1470714,1470716,1476558,1476559,1476560,1476561,1476563,1476564,1477160,1477161,1477162,1477163,1477164,1477165,1477166,1477167,1477168,1477169,1709260,1862796,1453703,1300008,1457110,1457111,1457112,1457113,1367719,1334359,1333986,1333985,1332985,1332984,1331729,1331728,1330728,1867167,1867166,1379786,1363626,1309428,1319726,1319725,1319724,1317748,1317747,1317746,1317745,1316563,1314728,1313572,1313571,1313570,1311710,1363572,1303527,1567193,1361719,1359790,1359789,1359788,1311444,1303526,1309849,1303528,1303529,1304276,1308526,1309427,1363571,1303525,1368720,1860728,1554993,1329719,1329718,1329717,1329716,1329715,1329714,1329422,1329421,1327726,1327719,1326489,1554994,1552797,1552796,1552795,1554905,1552733,1554906,1552731,1552730,1552729,1552728,1552727,1552726,1552794,1554944,1560712,1560711,1560710,1554949,1554948,1554947,1554904,1554945,1567169,1554943,1554942,1554941,1554940,1554908,1554907,1554946,1348707,1347703,1350714,1350713,1350712,1350711,1350710,1350716,1348708,1350717".split(",");
    /**
     * 返回手机号码
     */
    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    public static void main(String[] args) throws Exception {
        /*int count = 100000;
        for (int i = 0; i < count; i++) {
            String randomIp = getRandomIp();
            System.err.println(randomIp);
        }
        Date date = randomDate("2017-08-01","2018-12-01");
        System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));*/
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date = randomDate(dateFormat1.parse("2018-02-01 00:00:00"), dateFormat1.parse("2018-06-01 00:00:00"));
        System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
        for(int i = 0; i < 20000; i++) {
            System.out.println(getTelPhoneNew());
        }
        /*String format = "HH:mm:ss";


//Date nowTime = new SimpleDateFormat(format).parse("09:27:00");
        Date now = new Date();
        DateFormat dateFormat = DateFormat.getTimeInstance();//获取时分秒
//得到当前时间的时分秒
        String nowStr = dateFormat.format(now);


        Date nowTime = new SimpleDateFormat("HH:mm:ss").parse(nowStr);

        Date startTime = new SimpleDateFormat(format).parse("00:27:00");
        Date endTime = new SimpleDateFormat(format).parse("09:27:59");
        System.out.println(isEffectiveDate(nowTime, startTime, endTime));*/
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse("2017-08-01"); // 构造开始日期  
            Date end = format.parse("2018-12-01");

            //Date date = randomDate(start, end);
            Date date = changeNightTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-01 05:31:31"));
            System.out.println(date);*/

    }

    public static String getRandomIp() {
        // ip范围
        int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
                { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
                { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
                { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
                { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
                { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
                { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
                { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
                { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };
        Random rdint = new Random();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }

    /** 
          * 获取随机日期 
          * @param beginDate 起始日期，格式为：yyyy-MM-dd 
          * @param endDate 结束日期，格式为：yyyy-MM-dd 
          * @return 
     String beginDate,String endDate
          */
    public static Date randomDate(Date beginDate,Date endDate){
        try {
            /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate); // 构造开始日期  
            Date end = format.parse(endDate); // 构造结束日期 */

            Date start = beginDate; // 构造开始日期  
            Date end = endDate; // 构造结束日期  
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            //判断时间是否为晚上
            /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = format.parse("2018-01-01 20:00:00");
            Long redisout = RedEnvelope.getDatePoorHour(time, new Date(date));
            if (redisout.equals(0)||redisout.intValue()<0){
                return null;
            }*/
            Date nowDate = new Date(date);
            DateFormat dateFormat = DateFormat.getTimeInstance();//获取时分秒
            //得到当前时间的时分秒
            String nowStr = dateFormat.format(nowDate);
            Date nowTime = new SimpleDateFormat("HH:mm:ss").parse(nowStr);

            Date startTime = new SimpleDateFormat("HH:mm:ss").parse("00:00:00");
            Date endTime = new SimpleDateFormat("HH:mm:ss").parse("08:00:00");
            boolean effectiveDate = isEffectiveDate(nowTime, startTime, endTime);
            if(effectiveDate){
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formatTimeEight(nowDate));
            }
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date changeNightTime(Date beginDate){
        try {
        Date date1 = beginDate;
        long date = date1.getTime();
        //判断时间是否为晚上
            /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = format.parse("2018-01-01 20:00:00");
            Long redisout = RedEnvelope.getDatePoorHour(time, new Date(date));
            if (redisout.equals(0)||redisout.intValue()<0){
                return null;
            }*/
        Date nowDate = new Date(date);
        DateFormat dateFormat = DateFormat.getTimeInstance();//获取时分秒
        //得到当前时间的时分秒
        String nowStr = dateFormat.format(nowDate);
        Date nowTime = new SimpleDateFormat("HH:mm:ss").parse(nowStr);

        Date startTime = new SimpleDateFormat("HH:mm:ss").parse("00:00:00");
        Date endTime = new SimpleDateFormat("HH:mm:ss").parse("08:00:00");
        boolean effectiveDate = isEffectiveDate(nowTime, startTime, endTime);
        if(effectiveDate){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formatTimeEight(nowDate));
        }
        return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTimeEight(Date nowDate) throws Exception {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long rightTime = (long) (nowDate.getTime() + 8 * 60 * 60 * 1000); //把当前得到的时间用date.getTime()的方法写成时间戳的形式，再加上8小时对应的毫秒数
        String newtime = sd.format(rightTime);//把得到的新的时间戳再次格式化成时间的格式
        return newtime;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    public static String getTelPhone() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }

    public static String getTelPhoneNew(){
       Random random = new Random();
        int re = random.nextInt(9999) + 1;
        DecimalFormat mFormat = new DecimalFormat("0000");//确定格式，把1转换为001
        String s = mFormat.format(re);
        return telFirstNew[random.nextInt(telFirstNew.length)] + s;
    }
}
