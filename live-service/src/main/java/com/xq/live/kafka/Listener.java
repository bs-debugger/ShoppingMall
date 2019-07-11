package com.xq.live.kafka;

import com.alibaba.fastjson.JSON;
import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.OrderInfo;
import com.xq.live.service.*;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.out.OrderCouponOut;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用规则:如果topic是单个分区,则使用singleContainerFactory来进行单线程消费
 *        如果topic是多个分区,则使用batchContainerFactory来进行多线程消费
 *
 * 业务扩展:目前只需要使用两个消费者组就够了,单个线程消费者组,多个线程消费者组
 * Created by admin on 2018/10/26.
 */
@Component
public class Listener {

    @Autowired
    private PayRefundService payRefundService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private ActLotteryService actLotteryService;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private SalePointService salePointService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private OrderInfoService orderInfoService;

    private Logger logger = Logger.getLogger(Listener.class);

    //@KafkaListener(id="testDemo",topics = {"newTest"},containerFactory = "singleContainerFactory")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) {

        System.out.println("testkey:"+record.key());
        System.out.println("testvalue:"+record.value().toString());
        /*//如果偏移量为偶数则确认消费，否则拒绝消费
        if (record.offset() % 2 == 0) {
            logger.info(record.offset()+"--ack");
            ack.acknowledge();
        } else {
            logger.info(record.offset()+"--nack");
            kafkaTemplate.send("newTest",record.key(), record.value());
        }*/
        ack.acknowledge();
    }

    //@KafkaListener(id="lipengDemo",topics = {"lipengTopic"},containerFactory = "batchContainerFactory")
    public void lipengDemo(List<ConsumerRecord<?, ?>> record , Acknowledgment ack) {
        for (ConsumerRecord<?, ?> consumerRecord : record) {
            System.out.println("testkey:"+consumerRecord.key());
            System.out.println("testvalue:" + consumerRecord.value());
            /*//如果偏移量为偶数则确认消费，否则拒绝消费
            if (consumerRecord.offset() % 2 == 0) {
                logger.info(consumerRecord.offset()+"--ack");
                ack.acknowledge();
            } else {
                logger.info(consumerRecord.offset()+"--nack");
                kafkaTemplate.send("newTest",consumerRecord.key(), consumerRecord.value());
            }*/
            ack.acknowledge();
        }
    }

    /**
     * 微信退款
     * @param record
     */
    @KafkaListener(topics = {"payRefundTopic"},containerFactory = "singleContainerFactory")
    public void payRefundTopic(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        Long orderId=Long.valueOf(record.value().toString());
        payRefundService.refund(orderId);
        ack.acknowledge();
    }

    /**
     * 修改该团产生的相关奖励金
     * @param record
     */
    @KafkaListener(topics = {"distriButionTopic"},containerFactory = "singleContainerFactory")
    public void distriBution(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        ActGoodsSku actGoodsSku=JSON.parseObject(record.value().toString(), ActGoodsSku.class);//将附带参数读取出来
        actGoodsSkuService.succesDistribution(actGoodsSku);
        ack.acknowledge();
    }

    /**
     * 核销完之后增加票数和抽奖机会
     * @param record
     */
    @KafkaListener(topics = {"messageAndActivitys"},containerFactory = "singleContainerFactory")
    public void messageAndActivity(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        OrderCouponInVo inVo=JSON.parseObject(record.value().toString(), OrderCouponInVo.class);//将附带参数读取出来

        //判断票券是否存在或票券是否被使用
        OrderCouponOut cp = orderCouponService.selectById(inVo.getId());
        if(cp==null){
            return;
        }
        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return;
        }
        //核销完成后给商家投票
        //voteService.addByOrderIdFroAct(orderInfo);

        //增加抽红包机会
        ActLotteryInVo actLotteryInVo=new ActLotteryInVo();
        actLotteryInVo.setUserId(cp.getUserId());
        actLotteryInVo.setActId(new Long(46));
        actLotteryInVo.setTotalNumber(1);
        actLotteryService.hxAdd(actLotteryInVo, inVo.getId());

        //3.7，3.8首单核销给用户发红包
        Integer i=orderCouponService.girlsDay(cp);
        ack.acknowledge();
    }

    /**
     * 批量刷单
     * @param record
     */
    //@KafkaListener(id = "batchOrderGroup",topics = {"batch.order.topic"},containerFactory = "batchContainerFactory")
    public void batchOrderTopic(List<ConsumerRecord<?, ?>> record, Acknowledgment ack) {
        for (ConsumerRecord<?, ?> consumerRecord : record) {
            OrderCouponInVo attachInVo = JSON.parseObject(consumerRecord.value().toString(), OrderCouponInVo.class);//将附带参数读取出来
            orderInfoService.batchHxCouponToKafka(attachInVo);
            ack.acknowledge();
        }
    }

}
