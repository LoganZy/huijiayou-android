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

    private String card_number;
    private String total_time;
    private String discount_after_amount;

    private String status ;

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

    private String product_name;
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;


}
