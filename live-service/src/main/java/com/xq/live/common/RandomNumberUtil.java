package com.xq.live.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * com.xq.live.common
 *
 * @author zhangpeng32
 * Created on 2018/5/10 下午2:19
 * @Description:
 */
public class RandomNumberUtil {
    /**
     *  随机金额奖励
     * @param max 表示订单金额，奖励不超过支付的金额， rate为比率，为随机数的小数位
     *            如果rate = 10 ，则返回值为 1.10， 0.30等。如果rate =100 则返回值为 1.11，2.31等
     * @return
     */
    public static BigDecimal randomNumber(int max, int rate){
        Random random = new Random();
        int randomNum = random.nextInt(max * rate) + 1;
        BigDecimal result = BigDecimal.valueOf(randomNum).divide(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    public static void main(String[] args) {
        for(int i=0; i<10000; i++){
            BigDecimal b = RandomNumberUtil.randomNumber(3, 10);
            System.out.println(b);
        }
    }
}
