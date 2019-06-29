package com.yw.miaosha.result;

public class CodeMsg {
    int code;
    String msg;


    //通用异常
    public static CodeMsg SUCCESS=new CodeMsg(0,"successs");
    public static CodeMsg ERROR=new CodeMsg(404,"页面消失");
    //不同模块不同代码

    private CodeMsg(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    //删除setter防止数据更改
}
