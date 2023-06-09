package com.huawei.utils;

import cn.hutool.core.util.BooleanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author: suwenge
 * @since: 2020/10/28 16:06
 */
public class RedisUtils {
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * The constant RUN_MESSAGE.
     */
    private static RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils expire(String key,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(天)
     * @return
     */
    public static boolean expireDay(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.DAYS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils expire(String key,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hasKey(String key) failure." + e.getMessage());
            return false;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils set(String key,Object value) failure." + e.getMessage());
            return false;
        }

    }

    /**
     * 设置分布式锁
     *
     * @param key  键
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean getLock(String key, long time) {
        try {
            if (redisTemplate.hasKey(key)) {
                return false;
            } else {
                if (time > 0) {
                    redisTemplate.opsForValue().set(key, key, time, TimeUnit.SECONDS);
                } else {
                    set(key, key);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils set(String key,Object value,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param key 键
     */
    public static void releaseLock(String key) {
        try {
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils set(String key,Object value,long time) failure." + e.getMessage());
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils set(String key,Object value,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(天) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean setDay(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils set(String key,Object value,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta
     * @return
     */
    public static long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hmset(String key, Map<String,Object> map) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hmset(String key, Map<String,Object> map, long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hset(String key,String item,Object value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 插入排序集合
     *
     * @param key   键
     * @param value  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean setZset(String key,Object value,String obj) {
        try {
            redisTemplate.opsForZSet().add(key, value, Double.parseDouble(obj));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hset(String key,String item,Object value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 获取有序集合
     * @param key
     * @param start
     * @param end
     * @param isDesc
     * @param <T>
     * @return
     */
    public static <T> Set<T> range(String key,int start,int end,boolean isDesc) {
        Set<T> range =  new HashSet<>();
        try {
            if(BooleanUtil.isTrue(isDesc)){
                range = redisTemplate.opsForZSet().range(key, start, end);
            }else{
                range = redisTemplate.opsForZSet().reverseRange(key, start, end);
            }
           return range;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hset(String key,String item,Object value) failure." + e.getMessage());
            return null;
        }
    }

    /**
     * 获取在Zset中的值
     * @param key
     * @param obj
     * @return
     */
    public static Double score(String key,Object obj) {
        Double score = null;
        try {
            score = redisTemplate.opsForZSet().score(key,obj);
            return score;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hset(String key,String item,Object value) failure." + e.getMessage());
            return null;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils hset(String key,String item,Object value,long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public static double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public static double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils sGet(String key) failure." + e.getMessage());
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils sHasKey(String key,Object value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils sSet(String key, Object...values) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils sSetAndTime(String key,long time,Object...values) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils sGetSetSize(String key) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils setRemove(String key, Object ...values) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public static List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lGet(String key, long start, long end) failure." + e.getMessage());
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lGetListSize(String key) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lGetIndex(String key,long index) failure." + e.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lSet(String key, Object value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lSet(String key, Object value, long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lSet(String key, List<Object> value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lSet(String key, List<Object> value, long time) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lUpdateIndex(String key, long index,Object value) failure." + e.getMessage());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RedisUtils lRemove(String key,long count,Object value) failure." + e.getMessage());
            return 0;
        }
    }

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);


        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

//    /**
//     * redis分布式锁调用方案
//     */
//    String uuid = UUID.randomUUID().toString();
//    Jedis jedis = null;
//        try {
//        //获得客户端实例
//        jedis = redisManager.getJedis();
//        if (redisUtil.tryGetDistributedLock(jedis, Constants.CRM_ROLE_LOCK, uuid, 600)) {
//            //角色数据同步
//            roleService.dataSyncRole();
//        }
//    } catch (Exception e) {
//        log.error("Big data crm role Position information decomposition processing exception");
//    } finally {
//        if (!Objects.isNull(jedis)) {
//            redisUtil.releaseDistributedLock(jedis, Constants.CRM_ROLE_LOCK, uuid);
//        }
//    }

//redisson加锁方式
//    RLock lock = redissonClient.getLock("Export:create:" + Context.get().getCorpId());
//try {
//        if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
//            //业务处理
//        } else {
//            Assert.isTrue(false, "排队中,请稍后重试!");
//        }
//    } catch (InterruptedException e) {
//        Assert.isTrue(false, "请勿重复操作!");
//    } finally {
//        if (lock.isLocked()) {
//            lock.unlock();
//        }
//    }


}



