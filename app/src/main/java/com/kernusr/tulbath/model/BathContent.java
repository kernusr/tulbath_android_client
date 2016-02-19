package com.kernusr.tulbath.model;


import java.util.List;


public class BathContent {

    private String name, itemImage;
    private int id;
    private String address;
    private String price;
    private String description;
    private List Phones;

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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getPhones() {
        return Phones;
    }

    public void setPhones(List Phones) {
        this.Phones = Phones;
    }


}
