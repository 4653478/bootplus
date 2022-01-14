package com.age.util;

import com.age.config.EncryptConfig;

import java.util.HashMap;
import java.util.Map;

import static com.age.util.encry.AesEncryptUtil.encrypt;

/**
 * 返回数据
 *
 * @author age
 * @Email 4653478@qq.com
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {

        super.put(key, value);
        return this;
    }

    public R putEncode(String key, Object value) {

        try{
            String enkey = EncryptConfig.KEY;// "0123456789abcdef";
            String iv =EncryptConfig.IV;// "0123456789abcdef";
            String data=value.toString();
            System.out.println("原文："+data);
            data = encrypt(data, enkey, iv);
            System.out.println("密文："+data);
            value=data;
        }
        catch(Exception e){
        }


        super.put(key, value);
        return this;
    }
}
