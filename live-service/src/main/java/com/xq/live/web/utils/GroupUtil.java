package com.xq.live.web.utils;

import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.ActUserOut;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ss on 2018/5/2.
 */
public class GroupUtil {
    private static Long viewArticleTime = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        /*Date date1 = dateFormat1.parse("2018-02-01 00:00:00");
        Date date2 = dateFormat1.parse("2018-01-01 00:00:00");
        int res = date1.compareTo(date2);
        System.out.println(res);

        Date date = new Date();*/


        for (int s=10;s>0;s--){
            int i= (int) (Math.random()*(20)+1);
            System.out.println(i);
        }

        /*So a = new So();
        a.setUserName("李鹏");
        a.setIsDui(1);
        a.setPayType(2);
        String s = JSON.toJSONString(a);
        So jsonObject = JSON.parseObject(s,So.class);

        int i = 0;*/
       /* DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        Date newDate = dateFormat1.parse("2018-01-01 00:00:00");*/
        //System.out.println(newDate);

        /***循环添加
         * listShop.add(0,"A");
         listUser.add(0,"A");
         listShop.add(1,"B");
         listUser.add(1,"B");
         listShop.add(2,"C");
         listUser.add(2,"C");
         listShop.add(3,"D");
         listUser.add(3,"D");
         listShop.add(4,"E");
         listUser.add(4,"E");
         */
        //System.out.println(getBigDecimalRandom(0.2,2));
    }

    //将商家和用户列表进行匹配分组(传递一个商家ID和用户实体集合)
    public static Map<Long,ActUserOut> groupTwo(List<Long> shop,List<ActUserOut> user){
        Map<Long,ActUserOut> maplist = new HashMap<Long,ActUserOut>();
        Random random = new Random();
        //根据商家和用户列表长度进行判断后匹配组队
        if (shop.size()>user.size()){//修改用户顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <user.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < user.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<shop.size();j++){
                maplist.put(shop.get(j), user.get(ran[j]));
            }
            return maplist;
        }else if (shop.size()==user.size()){//修改用户顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <user.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < user.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<shop.size();j++){
                maplist.put(shop.get(j), user.get(ran[j]));
            }
            return maplist;
        }else {//修改商家顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <shop.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < shop.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<shop.size();j++){
                maplist.put(shop.get(ran[j]), user.get(j));
            }
            return maplist;
        }
    }

    //将商家和用户列表进行匹配分组(传递商家实体集合和用户实体集合)
    public static Map<ActShopOut,ActUserOut> group(List<ActShopOut> shop,List<ActUserOut> user){
        Map<ActShopOut,ActUserOut> maplist = new HashMap<ActShopOut,ActUserOut>();
        Random random = new Random();
        //根据商家和用户列表长度进行判断后匹配组队
        if (shop.size()>user.size()){//修改用户顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <user.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < user.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<user.size();j++){
                maplist.put(shop.get(j), user.get(ran[j]));
            }
            return maplist;
        }else if (shop.size()==user.size()){//修改用户顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <user.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < user.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<user.size();j++){
                maplist.put(shop.get(j), user.get(ran[j]));
            }
            return maplist;
        }else {//修改商家顺序
            List<Integer> randoms = new ArrayList<Integer>();
            int[] ran = new int[user.size()];
            // 将所有的可能出现的数字放进候选list
            for(int i = 0; i <shop.size(); i++){
                randoms.add(i);
            }
            // 从候选list中取出放入数组，已经被选中的就从这个list中移除
            for(int i = 0; i < shop.size(); i++){
                int index = getRandom(0, randoms.size()-1);
                ran[i] = randoms.get(index);
                randoms.remove(index);
            }
            for (int j=0;j<shop.size();j++){
                maplist.put(shop.get(ran[j]), user.get(j));
            }
            return maplist;
        }

    }

    /**
     * 根据min和max随机生成一个范围在[min,max]的随机数，包括min和max
     * @param min
     * @param max
     * @return int
     */
    public static Integer getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt( max - min + 1 ) + min;
    }

    /**
     * 根据min和max随机生成一个范围在[min,max]的随机数，包括min和max
     * @param min
     * @param max
     * @return int
     */
    public static BigDecimal getBigDecimalRandom(double min, double max){
        if (max<=min){
            return null;
        }
        //随机数
        double doubleNumber=min+ Math.random() * (max - min);
        BigDecimal amount=new BigDecimal(doubleNumber);
        double f1=amount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal amounts=new BigDecimal(f1);
/*        System.out.println(doubleNumber + "*******" + f1);
        System.out.println("*******");*/

        return amounts;
    }
}
