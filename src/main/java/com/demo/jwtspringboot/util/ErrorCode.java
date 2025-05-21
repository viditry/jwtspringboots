package com.demo.jwtspringboot.util;

public interface ErrorCode {

    //编码
    int code();

    //提示信息
    String msg();

    //提示信息-传参
    ErrorCode msg(Object... args);
}
