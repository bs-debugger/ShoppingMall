package com.xq.live.web.utils;

import java.util.Random;

/**
 * 根据区间概率得出随机数
 * Created by ss on 2018/8/14.
 */
public class MathRandom {

    public static void main(String[] args) {
        Integer o1=0;
        Integer o2=0;
        Integer o3=0;
        Integer o4=0;
        Integer o5=0;
        Integer o6=0;

        for (int i=0;i<100;i++){
            Integer num=PercentageRandom();
            if (num>=1&&num<=5){
                o1++;
                System.out.println("[1-5]：数目"+num+"个金币");
            }else if (num>=6&&num<=10){
                o2++;
                System.out.println("[6-10]：数目"+num+"个金币");
            }if (num>=11&&num<=20){
                o3++;
                System.out.println("[11-20]：数目"+num+"个金币");
            }else if (num>=21&&num<=30){
                o4++;
                System.out.println("[21-30]：数目"+num+"个金币");
            }if (num>=31&&num<=40){
                o5++;
                System.out.println("[31-40]：数目"+num+"个金币");
            }else if (num>=41&&num<=50){
                o6++;
                System.out.println("[41-50]：数目"+num+"个金币");
            }

        }

        System.out.println("[1-5]：个数"+o1);
        System.out.println("[6-10]：个数"+o2);
        System.out.println("[11-20]：个数"+o3);
        System.out.println("[21-30]：个数"+o4);
        System.out.println("[31-40]：个数"+o5);
        System.out.println("[41-50]：个数"+o6);
    }


    /**
     * [1-5]出现的概率为%50
     */
    public final static double rate0 = 0.50;
    /**
     * [6-10]出现的概率为%30
     */
    public final static double rate1 = 0.30;
    /**
     * [11-20]出现的概率为%15
     */
    public final static double rate2 = 0.15;
    /**
     * [21-30]出现的概率为%4.4
     */
    public final static double rate3 = 0.044;
    /**
     * [31-40]出现的概率为%0.5
     */
    public final static double rate4 = 0.005;
    /**
     * [41-50]出现的概率为%0.1
     */
    public final static double rate5 = 0.001;
    /**
     * Math.random()产生一个double型的随机数，判断一下
     * 例如[1-5]出现的概率为%50，则介于0到0.50中间的返回[1-5]
     * @return int
     *
     */
    public static Integer PercentageRandom()
    {
        double randomNumber;
        randomNumber = Math.random();
        if (randomNumber >= 0 && randomNumber <= rate0)
        {
            Random random = new Random();
            int num = random.nextInt(5) % (5 - 1 + 1) + 1;
            return num;
        }
        else if (randomNumber >= rate0 && randomNumber <= rate0 + rate1)
        {
            Random random = new Random();
            int num = random.nextInt(10) % (10 - 6 + 1) + 6;
            return num;
        }
        else if (randomNumber >= rate0 + rate1
                && randomNumber <= rate0 + rate1 + rate2)
        {
            Random random = new Random();
            int num = random.nextInt(20) % (20 - 11 + 1) + 11;
            return num;
        }
        else if (randomNumber >= rate0 + rate1 + rate2
                && randomNumber <= rate0 + rate1 + rate2 + rate3)
        {
            Random random = new Random();
            int num = random.nextInt(30) % (30 - 21 + 1) + 21;
            return num;
        }
        else if (randomNumber >= rate0 + rate1 + rate2 + rate3
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4)
        {
            Random random = new Random();
            int num = random.nextInt(40) % (40 - 31 + 1) + 31;
            return num;
        }
        else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4
                + rate5)
        {
            Random random = new Random();
            int num = random.nextInt(50) % (50 - 41 + 1) + 41;
            return num;
        }
        return -1;
    }
}
