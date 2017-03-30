package com.huijiayou.huijiayou.bean;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class OrderDetail {
    private String id;
    private String total_time;
    private String recharge_amount;
    private String recharge_time;
    private String status;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(String recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public String getRecharge_time() {
        return recharge_time;
    }

    public void setRecharge_time(String recharge_time) {
        this.recharge_time = recharge_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
