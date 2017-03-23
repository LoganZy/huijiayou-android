package com.huijiayou.huijiayou.bean;

/**
 * Created by Administrator on 2017/3/8 0008.
 */
public class Record {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    private String belong;
    private String product_name;
    private String id;
    private String card_number;//卡号
    private String total_time;//充值时间
    private String discount_after_amount;//实际支付
    private String discount_before_amount;//充值金额
    private String status ;//订单状态
    private String order_number;//订单编号
    private String ctime;//下单时间
    public String getDiscount_before_amount() {
        return discount_before_amount;
    }

    public void setDiscount_before_amount(String discount_before_amount) {
        this.discount_before_amount = discount_before_amount;
    }


    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }





    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getDiscount_after_amount() {
        return discount_after_amount;
    }

    public void setDiscount_after_amount(String discount_after_amount) {
        this.discount_after_amount = discount_after_amount;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;


}
