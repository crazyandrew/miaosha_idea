package com.yw.miaosha.redis;

public class GoodsKey extends BasePrefix {
    public static KeyPrefix getGoodsList=new GoodsKey(60,"goodsList");
    public static KeyPrefix getGoodsDetail=new GoodsKey(60,"goodsDetail");


    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
