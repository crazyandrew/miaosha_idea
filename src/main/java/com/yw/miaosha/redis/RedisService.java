package com.yw.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个对象
     */
    public<T> T get(KeyPrefix prefix ,String key , Class<T> clazz){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            String str=jedis.get(realKey);
            T t=stringToBean(str,clazz);
            return t;
        }finally {
            retunToPool(jedis);
        }
    }

    private void retunToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        if(str==null||str.length()<=0||clazz==null){
            return null;
        }
        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    /**
     * 设置对象
     */
    public<T> boolean set(KeyPrefix prefix ,String key , T value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String str=beanToString(value);
            if(str==null||str.length()<=0){
                return false;
            }
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            int seconds=prefix.expireSeconds();
            if(seconds<=0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            retunToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class<?> clazz=value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String)value;
        }else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * 判断key是否存在
     */
    public<T> boolean exists(KeyPrefix prefix ,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            retunToPool(jedis);
        }
    }

    /**
     * 增加值
     */
    public<T> Long incr(KeyPrefix prefix ,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            retunToPool(jedis);
        }
    }

    /**
     * 减少值
     */
    public<T> Long decr(KeyPrefix prefix ,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            retunToPool(jedis);
        }
    }

    public boolean delete(MiaoshaUserKey prefix, String key) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            long result= jedis.del(realKey);
            return result>0;
        }finally {
            retunToPool(jedis);
        }
    }
}
