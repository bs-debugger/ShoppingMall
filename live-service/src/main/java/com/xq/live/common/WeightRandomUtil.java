package com.xq.live.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
/**
 * com.xq.live.common
 * 来源https://blog.csdn.net/BuquTianya/article/details/51051672
 * @author zhangpeng32
 * Created on 2018/5/10 下午1:59
 * @Description:
 */
public class WeightRandomUtil {

    public static void main(String[] args){
        WeightRandomUtil wr = new WeightRandomUtil();
        wr.initWeight(new String[]{"1","2","3","4"}, new Integer[]{100,100,200,600});

        Random r = new Random();

        for(int i = 0; i < 10; i++){
            Integer rv = r.nextInt(wr.getMaxRandomValue());
            System.out.println(rv);
            System.out.println(wr.getElementByRandomValue(rv).getKey() + " " + rv);
        }

        HashMap<String, Integer> keyCount = new HashMap<String, Integer>();
        keyCount.put("1", 0);
        keyCount.put("2", 0);
        keyCount.put("3", 0);
        keyCount.put("4", 0);
        for(int i = 0; i < 10000; i++){
            Integer rv = r.nextInt(wr.getMaxRandomValue());
            String key = wr.getElementByRandomValue(rv).getKey();
            keyCount.put(key, keyCount.get(key).intValue()+1);
        }

        System.out.println("");
    }

    private List<WeightElement> weightElements;

    public void initWeight(String[] keys, Integer[] weights){
        if(keys == null || weights == null || keys.length != weights.length){
            return;
        }

        weightElements = new ArrayList<WeightElement>();

        for(int i=0; i< keys.length; i++){
            weightElements.add(new WeightElement(keys[i], weights[i]));
        }

        rangeWeightElemnts();

        printRvs();
    }

    private void rangeWeightElemnts(){
        if(weightElements.size() == 0){
            return;
        }

        WeightElement ele0 = weightElements.get(0);
        ele0.setThresholdLow(0);
        ele0.setThresholdHigh(ele0.getWeight());

        for(int i = 1; i < weightElements.size(); i++){
            WeightElement curElement = weightElements.get(i);
            WeightElement preElement = weightElements.get(i - 1);

            curElement.setThresholdLow(preElement.getThresholdHigh());
            curElement.setThresholdHigh(curElement.getThresholdLow() + curElement.getWeight());
        }
    }

    public WeightElement getElementByRandomValue(Integer rv){
        //因为元素权重范围有序递增，所以这里可以改为二分查找
        for(WeightElement e:weightElements){
            if(rv >= e.getThresholdLow() && rv < e.getThresholdHigh()){
                return e;
            }
        }

        return null;
    }

    public Integer getMaxRandomValue(){
        if(weightElements == null || weightElements.size() == 0){
            return null;
        }

        return weightElements.get(weightElements.size() - 1).getThresholdHigh();
    }

    public void printRvs(){
        for(WeightElement e:weightElements){
            System.out.println(e.toString());
        }
    }

    static class WeightElement{
        /**
         * 元素标记
         */
        private String key;
        /**
         * 元素权重
         */
        private Integer weight;
        /**
         * 权重对应随机数范围低线
         */
        private Integer thresholdLow;
        /**
         * 权重对应随机数范围高线
         */
        private Integer thresholdHigh;

        public WeightElement(){
        }

        public WeightElement(Integer weight){
            this.key = weight.toString();
            this.weight = weight;
        }

        public WeightElement(String key, Integer weight){
            this.key = key;
            this.weight = weight;
        }

        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public Integer getWeight() {
            return weight;
        }
        public void setWeight(Integer weight) {
            this.weight = weight;
        }
        public Integer getThresholdLow() {
            return thresholdLow;
        }
        public void setThresholdLow(Integer thresholdLow) {
            this.thresholdLow = thresholdLow;
        }
        public Integer getThresholdHigh() {
            return thresholdHigh;
        }
        public void setThresholdHigh(Integer thresholdHigh) {
            this.thresholdHigh = thresholdHigh;
        }

        public String toString(){
            return "key:"+this.key + " weight:" + this.weight + " low:"+this.thresholdLow+" heigh:"+this.thresholdHigh;
        }
    }
}
