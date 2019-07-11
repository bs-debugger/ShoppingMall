package com.xq.live.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.common
 * @description: redis缓存工具类封装
 * @author: zhangpeng32
 * @date: 2018/3/17 16:04
 * @version: 1.0
 */
@Service
public class RedisCache{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取字符串类型的缓存数据
     * @author feitao <yyimba@qq.com> 2019/5/6 11:50
     * @param key
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存<br>
     * 注：该方法暂不支持Character数据类型
     * @param key   key
     * @param clazz 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        return (T)redisTemplate.boundValueOps(key).get();
    }



    /**
     * 模糊删除满足条件的redis数据
     * @param key
     * @return
     */
    public  void  delAll(String key){
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }

    /**
     * 模糊查询满足条件的redis数据
     * @param key
     * @return
     */
    public  Set  selAll(String key){
        Set keys = redisTemplate.keys(key);
        return keys;
    }

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param timeout 失效时间(秒)
     */
    public void set(String key,Object value,final long timeout, final TimeUnit unit){
        if(value.getClass().equals(String.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Integer.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Double.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Float.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Short.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Long.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Boolean.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
        if(timeout > 0){
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * @Description: 是否缓存
     * @Author: zhangpeng32
     * @Date: 2018/3/17 17:32
     * @Version: 1.0.0
     */
    public boolean hasKey(String key) {
        boolean result = redisTemplate.hasKey(key);
        return result;
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @Description: 指定缓存的失效时间
     * @param: key
     * @Return:
     * @Author: zhangpeng32
     * @Date: 2018/3/20 19:50
     * @Version: 1.0.0
     */
    public void expire(String key, final long timeout, final TimeUnit unit) {
        if(timeout > 0){
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * @Description: 查询缓存的过期时间
     * @param: key
     * @param: unit
     * @Return:
     * @Author: lipeng
     * @Date: 2018/8/21 19:50
     * @Version: 1.0.0
     */
    public Long getExpire(String key, final TimeUnit unit) {
        Long expire = redisTemplate.getExpire(key, unit);
        return expire;
    }

    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     *
     * 注意:这个地方由于用的是StringRedisTemplate,则序列化的方式是String
     * ,则是Map<Object, String>.如果是自带的jdk的序列化方式,这个地方是Map<Object, Object>
     * 注意:如果想用hash结构，可以把StringRedisTemplate,换成redisTemplate默认的JDK序列化方式,这个这个只是我自己猜想的
     * ,还没有实践(推荐使用StringRedisTemplate)
     */
    public Map<Object, String> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取某个hash中的所有键
     * @param key
     * @return
     */
    public Set hmkeyget(String key){
        Set keys = redisTemplate.opsForHash().keys(key);
        return keys;
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param timeout 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, final long timeout, final TimeUnit unit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (timeout > 0) {
                expire(key, timeout,unit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param timeout 时间(秒)
     * 注意:如果已存在的hash表有时间,这里将会替换原有的时间      * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, final long timeout, final TimeUnit unit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (timeout > 0) {
                expire(key, timeout,unit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 模糊查询某个hash数据中满足条件的redis数据
     * @param key
     * @return
     */
    public Cursor<Map.Entry<String, String>> hselAll(String key, ScanOptions options){
        Cursor<Map.Entry<String, String>> keys = redisTemplate.opsForHash().scan(key, options);
        return keys;
    }

    /**
     * 模糊查询满足条件的数据库键列表
     * @param key 查询条件
     * @return
     */
    public List<String> scan(String key){
        List<String> list = new ArrayList<>();
        Jedis jedis =getJedis();
        if(jedis!=null){
            ScanParams scanParams = new ScanParams().count(5000);//一次查询五千数据，待调整
            scanParams.match(key);
            String cursor = ScanParams.SCAN_POINTER_START;
            while (true) {
                ScanResult< String> scanResult= jedis.scan(cursor,scanParams);
                List<String> elements = scanResult.getResult();
                if (elements != null && elements.size() > 0) {
                    list.addAll(elements);
                }
                cursor = scanResult.getStringCursor();
                if (ScanParams.SCAN_POINTER_START.equals(cursor)) {//游标参数为0时一轮查询完成
                    break;
                }
            }
        }
        return  list;
    }

    /**
     *在指定数据库键中查询h满足条件的redis数据
     * @param key 数据库键
     * @param match 匹配条件
     * @return
     */
    public  ScanResult<Map.Entry<String, String>>  hscan(String key,String match){
        Jedis jedis =getJedis();
        if(jedis!=null){
            ScanParams scanParams = new ScanParams().count(5000);
            scanParams.match(match);
            String cursor = ScanParams.SCAN_POINTER_START;
            ScanResult< String> scanResult =jedis.scan(cursor,scanParams);
            ScanResult<Map.Entry<String, String>> re= jedis.hscan(key,cursor,scanParams);
            return re;
        }
        return null;
    }

    public Jedis getJedis(){
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = factory.getConnection();
        if (rc instanceof JedisConnection) {
            JedisConnection jedisConnection = (JedisConnection) rc;
            Jedis jedis = jedisConnection.getNativeConnection();
            return jedis;
        }else{
            return null;
        }

    }

}
