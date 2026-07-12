/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author Dell
 */
public class FoodOrderDisplay {
    private int foodOrderId;
    private int roomNo;
    private LocalDate orderDate;
    private String status; // Corresponds to order_status
    private int bookingDetailId; // To link back if needed for detailed view or other actions

    public FoodOrderDisplay(int foodOrderId, int roomNo, LocalDate orderDate, String status, int bookingDetailId) {
        this.foodOrderId = foodOrderId;
        this.roomNo = roomNo;
        this.orderDate = orderDate;
        this.status = status;
        this.bookingDetailId = bookingDetailId;
    }

    // Getters
    public int getFoodOrderId() {
        return foodOrderId;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public int getBookingDetailId() {
        return bookingDetailId;
    }

    // Setters (if you need to update properties after creation, e.g., status)
    public void setStatus(String status) {
        this.status = status;
    }
}
