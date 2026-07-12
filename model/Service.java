/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Service {
    private int no;
    private int service_id;
    private String service_name;
    private int cloth_id;
    private String cloth_name;
    private int service_price;
    private String cloth_img;
    private int qty;
    private int total;
    private String gender;
    private String status;
    
    
    //to use in admin service view
    public Service(int service_id, String service_name, String cloth_name, String gender,int service_price,  String status) {    
        this.service_id = service_id;
        this.service_name = service_name;
        this.cloth_name = cloth_name;
        this.service_price = service_price;
        this.gender = gender;
        this.status = status;
    }

    // to use in cart
    public Service(int service_id, String service_name, int cloth_id, String cloth_name, int service_price, int qty, int total) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.cloth_id = cloth_id;
        this.cloth_name = cloth_name;
        this.service_price = service_price;
        this.qty = qty;
        this.total = total;
    }
    //to use in slip

    public Service(int no,String service_name, String cloth_name,  int qty,int service_price, int total) {
        this.no=no;
        this.service_name = service_name;
        this.cloth_name = cloth_name;
        this.service_price = service_price;
        this.qty = qty;
        this.total = total;
    }
    
    
    
    //use in cloth_card
    public Service(int service_id, String service_name, int cloth_id, String cloth_name, int service_price, String cloth_img) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.cloth_id = cloth_id;
        this.cloth_name = cloth_name;
        this.service_price = service_price;
        this.cloth_img= cloth_img;
    }   
    

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public int getCloth_id() {
        return cloth_id;
    }

    public void setCloth_id(int cloth_id) {
        this.cloth_id = cloth_id;
    }

    public String getCloth_name() {
        return cloth_name;
    }

    public void setCloth_name(String cloth_name) {
        this.cloth_name = cloth_name;
    }

    public int getService_price() {
        return service_price;
    }

    public void setService_price(int service_price) {
        this.service_price = service_price;
    }

    public String getCloth_img() {
        return cloth_img;
    }

    public void setCloth_img(String cloth_img) {
        this.cloth_img = cloth_img;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
    
    
    
    
}
