package com.kernusr.tulbath.model;


import org.json.JSONArray;

import java.util.List;


public class BathContent {

    private String name, itemImage;
    private int id;
    private String address;
    private String price;
    private JSONArray Phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public JSONArray getPhones() {
        return Phones;
    }

    public void setPhones(JSONArray Phones) {
        this.Phones = Phones;
    }


}
