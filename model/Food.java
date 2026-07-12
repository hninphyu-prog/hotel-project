/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dell
 */
public class Food {
    private int foodId;
    private String foodName;
    private int foodPrice; // Change to int
    private String foodImage;
    private int foodTypeId;
    private int mealCourseId;
    private String status; // Add status field
String order_date_time;
    String Food_Name;
    int Qty;
    int Price;
//FoodService

    public Food(String order_date_time, String Food_Name, int Qty, int Price) {
        this.order_date_time = order_date_time;
        this.Food_Name = Food_Name;
        this.Qty = Qty;
        this.Price = Price;
    }
    
    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
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
    
            
    







    // Constructor with 7 arguments (matching AdminFoodController's loadAllFoodItems)
    public Food(int foodId, String foodName, int foodPrice, String foodImage, int foodTypeId, int mealCourseId, String status) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodTypeId = foodTypeId;
        this.mealCourseId = mealCourseId;
        this.status = status;
    }

    // Existing constructor updated to use int foodPrice
    public Food(int foodId, String foodName, int foodPrice, String foodImage) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        // Default or placeholder values if IDs are not passed
        this.foodTypeId = -1;
        this.mealCourseId = -1;
        this.status = "unknown"; // Default status
    }

    // Existing constructor updated to use int foodPrice
    public Food(int foodId, String foodName, int foodPrice, String foodImage, int foodTypeId, int mealCourseId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodTypeId = foodTypeId;
        this.mealCourseId = mealCourseId;
        this.status = "unknown"; // Default status
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() { // Return type changed to int
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) { // Parameter type changed to int
        this.foodPrice = foodPrice;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public int getMealCourseId() {
        return mealCourseId;
    }

    public void setMealCourseId(int mealCourseId) {
        this.mealCourseId = mealCourseId;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}