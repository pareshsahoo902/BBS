package com.release.bbsbillapp;

import java.io.Serializable;

public class ItemsModel implements Serializable {

    String itemName ,weight , quantity;
    double price;


    public ItemsModel() {
    }

    public ItemsModel(String itemName, String weight, String quantity, double price) {
        this.itemName = itemName;
        this.weight = weight;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
