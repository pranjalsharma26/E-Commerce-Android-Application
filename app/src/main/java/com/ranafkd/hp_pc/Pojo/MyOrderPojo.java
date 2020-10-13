package com.ranafkd.hp_pc.Pojo;

public class MyOrderPojo {

    private String  name;
    private String price;
    private int image;
    private String count;

    public MyOrderPojo(String name, String count, String price, int image) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
