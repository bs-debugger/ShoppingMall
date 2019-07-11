package com.xq.live.web.utils;

import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.in.SoWriteOffInVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/5/9.
 */
public class CutOutTimeUtils {
    public static void main(String[] args) throws Exception {
        CutOutTimeUtils outTimeUtils= new CutOutTimeUtils();
        Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-08-23");// 定义起始日期
        Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-09");// 定义结束日期
        SoWriteOffInVo inVo = new SoWriteOffInVo();
        inVo.setBegainTime(d1);
        inVo.setEndTime(d2);
        outTimeUtils.demo(inVo);
        //outTimeUtils.demo("2017-01-01","2018-01-01");
    }

    /**
     * 根据一段时间区间，按月份拆分成多个时间段
     * @param inVo
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<SoWriteOffInVo> getValueForDate(SoWriteOffInVo inVo) {
        List<SoWriteOffInVo> list = null;
        try {
            list = new ArrayList<SoWriteOffInVo>();

            String firstDay = "";
            String lastDay = "";
            Date d1=inVo.getBegainTime();
            Date d2=inVo.getEndTime();
            Calendar dd = Calendar.getInstance();// 定义日期实例
            dd.setTime(d1);// 设置日期起始时间
            Calendar cale = Calendar.getInstance();

            Calendar c = Calendar.getInstance();
            c.setTime(d2);

            int startDay = d1.getDate();
            int endDay = d2.getDate();

            SoWriteOffInVo offInVo=null;

            while (dd.getTime().before(d2)) {// 判断是否到结束日期
                offInVo = new SoWriteOffInVo();
                cale.setTime(dd.getTime());

                if(dd.getTime().equals(d1)){
                    cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
                    offInVo.setBegainTime(d1);
                    offInVo.setEndTime(cale.getTime());
                    offInVo.setShopId(inVo.getShopId());

                }else if(dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)){
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    offInVo.setBegainTime(cale.getTime());
                    offInVo.setEndTime(d2);
                    offInVo.setShopId(inVo.getShopId());

                }else {
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    offInVo.setBegainTime(cale.getTime());
                    cale.set(Calendar.DAY_OF_MONTH, dd
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    offInVo.setEndTime(cale.getTime());
                    offInVo.setShopId(inVo.getShopId());
                }
                list.add(offInVo);
                dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

            }

            if(endDay<startDay){
                offInVo = new SoWriteOffInVo();

                cale.setTime(d2);
                cale.set(Calendar.DAY_OF_MONTH,1);//取第一天

                offInVo.setBegainTime(cale.getTime());
                offInVo.setEndTime(d2);
                offInVo.setShopId(inVo.getShopId());
                list.add(offInVo);
            }
        } catch (Exception e) {
            return null;
        }

        return list;
    }

    /**
     * 根据一段时间区间，按月份拆分成多个时间段
     * @param inVo
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<OrderWriteOffInVo> getValueForDateNew(OrderWriteOffInVo inVo) {
        List<OrderWriteOffInVo> list = null;
        try {
            list = new ArrayList<OrderWriteOffInVo>();

            String firstDay = "";
            String lastDay = "";
            Date d1=inVo.getBeginTime();
            Date d2=inVo.getEndTime();
            Calendar dd = Calendar.getInstance();// 定义日期实例
            dd.setTime(d1);// 设置日期起始时间
            Calendar cale = Calendar.getInstance();

            Calendar c = Calendar.getInstance();
            c.setTime(d2);

            int startDay = d1.getDate();
            int endDay = d2.getDate();

            OrderWriteOffInVo offInVo=null;

            while (dd.getTime().before(d2)) {// 判断是否到结束日期
                offInVo = new OrderWriteOffInVo();
                cale.setTime(dd.getTime());

                if(dd.getTime().equals(d1)){
                    cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
                    offInVo.setBeginTime(d1);
                    offInVo.setEndTime(cale.getTime());
                    offInVo.setShopId(inVo.getShopId());

                }else if(dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)){
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    offInVo.setBeginTime(cale.getTime());
                    offInVo.setEndTime(d2);
                    offInVo.setShopId(inVo.getShopId());

                }else {
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    offInVo.setBeginTime(cale.getTime());
                    cale.set(Calendar.DAY_OF_MONTH, dd
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    offInVo.setEndTime(cale.getTime());
                    offInVo.setShopId(inVo.getShopId());
                }
                list.add(offInVo);
                dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

            }

            if(endDay<startDay){
                offInVo = new OrderWriteOffInVo();

                cale.setTime(d2);
                cale.set(Calendar.DAY_OF_MONTH,1);//取第一天

                offInVo.setBeginTime(cale.getTime());
                offInVo.setEndTime(d2);
                offInVo.setShopId(inVo.getShopId());
                list.add(offInVo);
            }
        } catch (Exception e) {
            return null;
        }

        return list;
    }

    /**
     * 截取字符串后六位,不足的左补0
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if(strLen>=strLength){
            String substring = str.substring(str.length() - strLength);
            return substring;
        }
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
            sb.append("0").append(str);// 左补0
            // sb.append(str).append("0");//右补0
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    //测试
    public void demo(SoWriteOffInVo inVo){
        List<SoWriteOffInVo> listOff=CutOutTimeUtils.getValueForDate(inVo);
        //List<KeyValueForDate> list = CutOutTimeUtils.getKeyValueForDate(begainTime,endTime);
        System.out.println("开始日期--------------结束日期");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(SoWriteOffInVo date : listOff){
            System.out.println(sdf.format(date.getBegainTime())+"-----"+sdf.format(date.getEndTime()));
        }
    }

}
