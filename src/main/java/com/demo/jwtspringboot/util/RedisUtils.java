package com.demo.jwtspringboot.util;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 *
 * @author develop01
 * @email
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    /**  锁默认过期时长，单位：毫秒 */
    public final static long DEFAULT_EXPIRE_LOCK = 1800000;

    private static final String LUA_UNLOCK_SCRIPT = "if redis.call(\"get\", KEYS[1]) == ARGV[1] " +
            "then " +
            "return redis.call(\"del\", KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";

    public void expire(String key,long expire) {
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public boolean haveKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public List getList(String key) {
        String value = valueOperations.get(key);
        return value == null ? null : fromJson(value, List.class);
    }

    public Set getSet(String key) {
        String value = valueOperations.get(key);
        return value == null ? null : fromJson(value, Set.class);
    }

    public Set getOrigSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Map getMap(String key) {
        String value = valueOperations.get(key);
        return value == null ? null : fromJson(value, Map.class);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    /**
     * @param lockKey 是否已加锁
     * @param clientId
     * @return
     */
    public boolean isLock(String lockKey) {
        boolean locked = redisTemplate.hasKey(lockKey);
        return locked;
    }

    /**
     * @param lockKey   加锁键
     * @param clientId  加锁客户端唯一标识(采用UUID)
     * @return
     */
    public boolean tryLock(String lockKey, String clientId) {
        boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, DEFAULT_EXPIRE_LOCK, TimeUnit.MILLISECONDS);
        return locked;
    }


    /**
     * @param lockKey   加锁键
     * @param clientId  加锁客户端唯一标识(采用UUID)
     * @return
     */
    public boolean tryLock(String lockKey, String clientId,long expire) {
        boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, expire, TimeUnit.MILLISECONDS);
        return locked;
    }

    /**
     * <p>解锁
     * <p>使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而 redis 锁自动过期失效的时候误删其他线程的锁
     */
    public boolean unlock(String lockKey, String clientId) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_UNLOCK_SCRIPT, Long.class);
        Object result = redisTemplate.execute(redisScript, Arrays.asList(lockKey), clientId);
        return result != null && Long.parseLong(result.toString()) > 0;
    }

    /**
     * 设置key超时时间
     */
    public void expireKey(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 将数据缓存到的Set集合
     *
     * @param setKey
     * @param values
     */
    public void add2Set(String setKey, Object... values) {
        redisTemplate.boundSetOps(setKey).add(values);
    }
    /**
     * 将数据缓存到的Set集合，设置超时时间
     * @param setKey
     */
    public void add2SetWithExpire(String setKey,long timeout, TimeUnit unit,Object... values) {
        redisTemplate.boundSetOps(setKey).add(values);
        expireKey(setKey,timeout,unit);
    }
    /**
     * 将数据从Set集合移除
     *
     * @param setKey
     * @param values
     */
    public void remove2Set(String setKey, Object... values) {
        redisTemplate.boundSetOps(setKey).remove(values);
    }
    /**
     * 获取Set集合数量
     *
     * @param setKey
     */
    public long getSetNum(String setKey) {
        return redisTemplate.boundSetOps(setKey).size();
    }
    /**
     * 获取Set集合成员
     * @param setKey
     */
    public Set<Object> getSetMember(String setKey) {
        return redisTemplate.boundSetOps(setKey).members();
    }
    /**
     * 判断value是否为Set集合元素
     */
    public boolean isSetMember(String setKey, Object value) {
        BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(setKey);
        if (boundSetOperations != null) {
            return boundSetOperations.isMember(value);
        }
        return false;
    }

    /**
     * key前缀模糊删除
     */
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        redisTemplate.delete(keys);
    }
    /**
     * key后缀模糊删除
     */
    public void deleteBySuffix(String suffix) {
        Set<String> keys = redisTemplate.keys("*" + suffix);
        redisTemplate.delete(keys);
    }
    /**
     * key前缀获取key
     */
    public Set<String> getKeysByPrefix(String prefix) {
        return  redisTemplate.keys(prefix + "*");
    }


    /**
     * 获取key剩余过期时间
     */
    public long ttlKey(String key){
        return redisTemplate.getExpire(key);
    }
    /**
     * 将集合数据缓存到hash
     */
    public void add2Hashs(String key,Map<String,Object> map){
        redisTemplate.boundHashOps(key).putAll(map);
    }
    /**
     * 将数据缓存到hash,value为null情况，替换为空字符串
     */
    public void add2Hash(String key,String hashKey,Object value){
        if(value==null){
            value = "";
        }
        redisTemplate.boundHashOps(key).put(hashKey,value);
    }

    /**
     * 获取hash的成员值
     */
    public boolean hasHashKey(String key,String hashKey){
        return redisTemplate.boundHashOps(key).hasKey(hashKey);
    }

    /**
     * 获取hash的成员值
     */
    public Object getHashMember(String key,String hashKey){
        return redisTemplate.boundHashOps(key).get(hashKey);
    }
    /**
     * 获取缓存 hash 所有键值对数据
     *
     * @param k key
     * @return Map<String, String>
     */
    public Map<String, Object> getHashEntry(String k) {
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(k);
    }

    /**
     * 根据key和 hashKey删除值
     * @param key  key
     * @param hashKey hashKey
     */
    public void delHashByHashKey(String key,String hashKey){
        redisTemplate.opsForHash().delete(key,hashKey);
    }
    /**
     * 根据key和 多个 hashKey删除值
     * @param key  key
     * @param hashKeys hashKeys
     */
    public void delHashByHashKeys(String key,List<String> hashKeys){
        String[] array = hashKeys.toArray(new String[hashKeys.size()]);
        redisTemplate.opsForHash().delete(key, array);
    }

    /**
     * 获取缓存list所有数据，元素为字符串格式
     *
     * @param key
     * @return
     */
    public List<String> listRange(String key) {
        return stringRedisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 删除
     *
     * @param keys
     */
    public void delete(String... keys) {
        redisTemplate.delete(Arrays.stream(keys).collect(Collectors.toList()));
    }


    public Long rightPushAll(String key, Collection<?> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }


    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

}
