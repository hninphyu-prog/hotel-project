/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author May Na Dar
 */
public class Food_history {
    String food_order_date;
    String Food_Name;
    int Qty;
    int Price;
//FoodService

    public Food_history(String food_order_date, String Food_Name, int Qty, int Price) {
        this.food_order_date = food_order_date;
        this.Food_Name = Food_Name;
        this.Qty = Qty;
        this.Price = Price;
    }

    public String getFood_order_date() {
        return food_order_date;
    }

    public void setFood_order_date(String food_order_date) {
        this.food_order_date = food_order_date;
    }
 

    public String getFood_Name() {
        return Food_Name;
    }

    public void setFood_Name(String Food_Name) {
        this.Food_Name = Food_Name;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }
    
            
    






}
