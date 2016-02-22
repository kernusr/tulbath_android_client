package com.kernusr.tulbath.model;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class BathContent {

    private String name, itemImage;
    private int id;
    private String address;
    private String price;
    private ArrayList Phones;

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

    public ArrayList getPhones() {
        return Phones;
    }

    public void setPhones(JSONArray Phones) throws JSONException {
        this.Phones = new ArrayList();
        for(int i=0; i<Phones.length(); i++){
            this.Phones.add(Phones.getString(i));
        }
    }


}
