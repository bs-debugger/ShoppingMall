package com.xq.live.web.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.Calendar;
import java.util.Set;

/**
 *
 * 利用redis做延迟任务
 * Created by lipeng on 2018/8/11.
 */
public class AppTest {
    private static final String ADDR = "120.78.70.187";

    private static final int PORT = 6379;

    private static JedisPool jedisPool = new JedisPool(ADDR, PORT);



    public static Jedis getJedis() {

        return jedisPool.getResource();

    }



    //生产者,生成5个订单放进去

    public static void main(String[] args) {

        AppTest appTest =new AppTest();

        appTest.productionDelayMessage();

        appTest.consumerDelayMessage();

    }



    //消费者，取订单

    public void productionDelayMessage(){

        for(int i=0;i<5;i++){

            //延迟3秒

            Calendar cal1 = Calendar.getInstance();

            cal1.add(Calendar.SECOND, 3);

            int second3later = (int) (cal1.getTimeInMillis() / 1000);

            AppTest.getJedis().zadd("OrderId",second3later,"OID0000001"+i);

            System.out.println(System.currentTimeMillis()+"ms:redis生成了一个订单任务：订单ID为"+"OID0000001"+i);

        }

    }

    public void consumerDelayMessage(){

        Jedis jedis = AppTest.getJedis();

        while(true){

            Set<Tuple> items = jedis.zrangeWithScores("OrderId", 0, 1);

            if(items == null || items.isEmpty()){

                System.out.println("当前没有等待的任务");

                try {

                    Thread.sleep(500);

                } catch (InterruptedException e) {


                    e.printStackTrace();

                }

                continue;

            }

            int  score = (int) ((Tuple)items.toArray()[0]).getScore();

            Calendar cal = Calendar.getInstance();

            int nowSecond = (int) (cal.getTimeInMillis() / 1000);

            if(nowSecond >= score){

                String orderId = ((Tuple)items.toArray()[0]).getElement();

                jedis.zrem("OrderId", orderId);

                System.out.println(System.currentTimeMillis() +"ms:redis消费了一个任务：消费的订单OrderId为"+orderId);

            }

        }

    }
}
