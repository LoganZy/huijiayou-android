package com.huijiayou.huijiayou.net;

import java.io.Serializable;

/**
 * Created by user on 2016/6/21.
 * 错误信息返回结果
 */
public class MessageEntity implements Serializable {


    /**
     * code : 1202
     * message : 用户名不存在或密码错误
     * data : {"call_num":2}
     */

    private int code;
    private String message;
    /**
     * call_num : 2
     */

    private DataResult data;
    public MessageEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataResult getData() {
        return data;
    }

    public void setData(DataResult data) {
        this.data = data;
    }

    public static class DataResult implements Serializable{
        private int call_num;

        public int getCall_num() {
            return call_num;
        }

        public void setCall_num(int call_num) {
            this.call_num = call_num;
        }
    }
}
