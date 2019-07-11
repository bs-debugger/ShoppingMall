package com.xq.live.web.utils;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * 获取一组随机数
 * Created by ss on 2018/8/8.
 */
public class RedEnvelope {

	/*
	 * 100元
	 * 1个人	100
	 * 2个人	第一个人x元	第二个人100-x
	 * 3个人	第一个人x元	第二个人y=100-x-0.01	第三个人100-x-y
	 */
        public static void main(String[] args) {
            /*获取一组随机数double类型*/
            Scanner sc = new Scanner( System.in );
            System.out.println( "请输入总金额：" );
            double totalMoney = sc.nextDouble();
            System.out.println( "请输入获取红包人数：" );
            int personNum = sc.nextInt();
            /*List<Double> money= RedEnvelope.RedDivied(totalMoney, personNum);
            System.out.println(money.toString());

            double checkTotalMoney = 0;
            for( int i = 0; i < personNum; i++ ) {
                checkTotalMoney += money.get(i);
            }
            System.out.println( checkTotalMoney );*/


            /*获取一组随机数整数类型*/
            Random random = new Random();
            int ss = random.nextInt(50) % (50 - 10 + 1) + 10;

        /*for (int i=0; i<10;i++){
            int ss = random.nextInt(50) % (50 - 10 + 1) + 10;
            System.out.println(ss);
        }*/

        /*int s = (int)(Math.random()*50+10);
        System.out.println(s);*/
        /*System.out.println(ss);*/
            List<Integer> amountList = divideRedPackage(ss, 6);
            Integer i=0;
            for(Integer amount : amountList){

                System.out.println("抢到金额：" + new BigDecimal(amount));
                //.divide(new BigDecimal(100))
                i+=amount;
            }
            System.out.println(i);
        }
    /*获取一组随机数double类型*/
    public static List<BigDecimal> RedDivied(double totalMoney,Integer personNum){
        /*double[] allocationMoney = new double[personNum];*/
        List<Double> allocationMoney =new ArrayList<Double>();
        List<BigDecimal> biglist=new ArrayList<BigDecimal>();

        for ( int i = personNum; i > 1; i-- ) {
            //double surplusMoney = totalMoney - ( i - 1 ) * 0.01;
            //安全线
            double surplusMoney = ( totalMoney - ( i - 1 ) * 0.01 ) / ( i / 2 );
            //设surplusMoney为15，1、获取[0,1500)随机数，2、获取
                /*[1,1500]随机数，3、获取[0.01,15.00]随机数*/
            double randomlyAssignedMoney = ( (int)( Math.random() *
                    surplusMoney * 100 ) + 1 ) * 0.01;
            //截取小数点后两位输出
            BigDecimal aBigDecimal = new BigDecimal(
                    randomlyAssignedMoney );
            double moneyOfOnePerson = aBigDecimal.setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println( moneyOfOnePerson );
            totalMoney -= randomlyAssignedMoney;
            allocationMoney.add(randomlyAssignedMoney);
            biglist.add(aBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
            /*allocationMoney.get(i-1) = randomlyAssignedMoney;*/
        }
        BigDecimal bg = new BigDecimal( totalMoney );
        double remainingMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(remainingMoney);
        allocationMoney.add(remainingMoney);
        biglist.add(bg.setScale(2, BigDecimal.ROUND_HALF_UP));
        /*allocationMoney[0] = remainingMoney;*/
        /*double checkTotalMoney = 0;
        for( int i = 0; i < personNum; i++ ) {
            checkTotalMoney += allocationMoney[i];
        }
        System.out.println( checkTotalMoney );*/
        return biglist;
    }

    //发金币算法，金额参数以分为单位
    /*获取一组随机数整数类型*/
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum){
        List<Integer> amountList = new ArrayList<Integer>();
        Integer restAmount = totalAmount;
        Integer restPeopleNum = totalPeopleNum;
        Random random = new Random();
        for(int i=0; i<totalPeopleNum-1; i++){
            //随机范围：[1，剩余人均金额的两倍)，左闭右开
            int amount = random.nextInt(restAmount / restPeopleNum * 2 - 1) + 1;
            restAmount -= amount;
            restPeopleNum --;
            amountList.add(amount);
        }

        amountList.add(restAmount);

        return amountList;

    }


    //2、时间差
    public static Long getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;

        return sec+min*60;
        /*return day + "天" + hour + "小时" + min + "分钟";*/
    }
    //2、时间差
    public static Long getDatePoorHour(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;

        return hour*60*60+sec+min*60;
        /*return day + "天" + hour + "小时" + min + "分钟";*/
    }

}
