package com.xq.live.service;

/**
 * Kafka基本操作
 * Created by lipeng on 2018/11/29.
 */
public interface KafkaService {
    /**
     * 发送数据到指定的topic中
     *
     * 注意:如果用此方法,如果是自动创建topic,则默认的分区数目为1
     * @param topicname topic名称
     * @param data      数据
     * @return 发送的状态
     */
    Boolean sendDataToTopic(String topicname, String data);

    /**
     * 发送数据到指定的topic中(通过key来发送,来查找具体的某个topic分区)
     * 注意:如果用此方法,如果是自动创建topic,则默认的分区数目为1
     * @param topicname topic名称
     * @param key       指定的key
     * @param data      数据
     * @return 发送的状态
     */
    Boolean sendDataToTopic(String topicname, String key,String data);

    /**
     * 发送数据到指定的topic中(通过key来发送,发送到具体某个分区)
     * 注意:如果用这个方法,topic最好手动创建,因为kafkaTemplate创建的分区默认指定为1
     * @param topicname topic名称
     * @param partition topic具体某个分区
     * @param key       指定的key
     * @param data      数据
     * @return 发送的状态
     */
    Boolean sendDataToTopic(String topicname, Integer partition,String key,String data);

    /**
     * 发送数据到指定的topic中(通过key来发送,来查找具体的某个topic分区)
     * 注意:如果用此方法,如果是自动创建topic,则默认的分区数目为1
     * 注意:这个地方用到了事物
     * @param topicname topic名称
     * @param key       指定的key
     * @param data      数据
     * @return 发送的状态
     */
    Boolean sendDataToTopicWithTran(String topicname, String key,String data);

    /**
     * 校验topic是否已经存在于kafka中
     *
     * @param topicname topic的名称
     * @return 是否存在的状态
     */
    Boolean isExistTopic(String topicname);

    /**
     * 创建指定的topic
     *
     * @param topicname topic的名称
     * @param partitions 分区数目  -----根据业务量来决定,最好别乱指定
     * @return 创建topic是否成功的状态
     */
    Boolean createTopic(String topicname,Integer partitions);
}
