/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FoodCartItem {
    private final IntegerProperty no;
    private final StringProperty itemName;
    private final IntegerProperty price;
    private final IntegerProperty quantity;
    private final IntegerProperty amount; 

    private Food originalFood; 

    public FoodCartItem(int no, String itemName, int price, int quantity, Food originalFood) {
        this.no = new SimpleIntegerProperty(no);
        this.itemName = new SimpleStringProperty(itemName);
        this.price = new SimpleIntegerProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.amount = new SimpleIntegerProperty(price * quantity);
        this.originalFood = originalFood;
    }

    public IntegerProperty noProperty() { return no; }
    public StringProperty itemNameProperty() { return itemName; }
    public IntegerProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }
    public IntegerProperty amountProperty() { return amount; }


    public int getNo() { return no.get(); }
    public String getItemName() { return itemName.get(); }
    public int getPrice() { return price.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getAmount() { return amount.get(); }
    public Food getOriginalFood() { return originalFood; }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.amount.set(this.price.get() * quantity);
    }
   
    public void setNo(int no) { this.no.set(no); }
}