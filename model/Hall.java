/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author May Na Dar
 */
public class Hall {
    int hall_booked_detailID;
    String halltype;
    String HallNo;
    int price;
    String imagename;
    String capacity;
    String StartTime;
    String EndTime;
    String BookingDate;

    public int getHall_booked_detailID() {
        return hall_booked_detailID;
    }

    public void setHall_booked_detailID(int hall_booked_detailID) {
        this.hall_booked_detailID = hall_booked_detailID;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String BookingStatus) {
        this.BookingStatus = BookingStatus;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    String BookingStatus;
    String paymentstatus;
    String name;
    
    
    //HallCard

    public Hall(String halltype, String HallNo,  String capacity,int price, String imagename) {
        this.halltype = halltype;
        this.HallNo = HallNo;
        this.price = price;
        this.imagename = imagename;
        this.capacity = capacity;
    }
   //tableList

    public Hall(String HallNo,String halltype, String BookingDate,String StartTime, String EndTime, int price) {
         this.HallNo = HallNo;
        this.halltype = halltype;
        this.BookingDate = BookingDate;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.price = price;
       
    }
      public Hall(int hall_booked_detailID, String halltype, String HallNo, int price, String capacity, String StartTime, String EndTime, String BookingDate, String name, String BookingStatus) {
        this.hall_booked_detailID = hall_booked_detailID;
        this.halltype = halltype;
        this.HallNo = HallNo;
        this.price = price;
        this.capacity = capacity;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.BookingDate = BookingDate;
        this.name = name;
        this.BookingStatus = BookingStatus;
       
    }

    public Hall(String halltype, int price, String capacity) {
        this.halltype = halltype;
        this.price = price;
        this.capacity = capacity;
    }

    public Hall() {
    }

    
    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String BookingDate) {
        this.BookingDate = BookingDate;
    }
    

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    
   

    public String getHalltype() {
        return halltype;
    }

    public void setHalltype(String halltype) {
        this.halltype = halltype;
    }

    public String getHallNo() {
        return HallNo;
    }

    public void setHallNo(String HallNo) {
        this.HallNo = HallNo;
    }

    

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

   
    
}
