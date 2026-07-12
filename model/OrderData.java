/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class OrderData {
    //orderlist
    private int order_id;
    private int room_id;
    private String order_date;
    private String delivery_date;
    private String status;
    private int no;
    private String service_name;
    private String cloth_name;
    private int quantity;
    private int service_price;
    private int total;
    
    //order detail list

    public OrderData(int order_id, int room_id, String order_date, String delivery_date, String status) {
        this.order_id = order_id;
        this.room_id = room_id;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.status = status;
    }

    public OrderData(int no, String service_name, String cloth_name, int quantity, int service_price, int total) {
        this.no = no;
        this.service_name = service_name;
        this.cloth_name = cloth_name;
        this.quantity = quantity;
        this.service_price = service_price;
        this.total = total;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getCloth_name() {
        return cloth_name;
    }

    public void setCloth_name(String cloth_name) {
        this.cloth_name = cloth_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getService_price() {
        return service_price;
    }

    public void setService_price(int service_price) {
        this.service_price = service_price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    
    
    
}
