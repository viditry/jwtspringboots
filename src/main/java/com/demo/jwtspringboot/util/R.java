package com.demo.jwtspringboot.util;



import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * @author cscc
 * @email
 * @date 2016年10月27日 下午9:59:27
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
//		return error(500, "未知异常，请联系管理员");
        return error(500, "ERROR");
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

    public static R error(ErrorCode errorCode) {
        R r = new R();
        r.put("code", errorCode.code());
        r.put("msg", errorCode.msg());
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

    public static R ok(Object data) {
        R r = new R();
        r.put("data", data);
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

    public Boolean isOk(){
        return (Integer)this.get("code") == 0;
    }

    public Boolean isError(){
        return (Integer)this.get("code") == 500;
    }
}
