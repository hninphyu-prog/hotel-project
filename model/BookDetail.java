/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author May Na Dar
 */
public class BookDetail {
  int    Booking_detailId;
  int Booking_Id;
  int room_Id;
  int occupant_capacity;
  String checkIn;
  String CheckOut;
  String roomstatus;
  String name;
  
  //Invoice 

    public BookDetail(int Booking_detailId, int Booking_Id, int room_Id, String checkIn, String CheckOut) {
        this.Booking_detailId = Booking_detailId;
        this.Booking_Id = Booking_Id;
        this.room_Id = room_Id;
        this.checkIn = checkIn;
        this.CheckOut = CheckOut;
    }
    //roomHistoryCard

    public BookDetail(int Booking_detailId, int room_Id,String name, String checkIn, String CheckOut, String roomstatus) {
        this.Booking_detailId = Booking_detailId;
        this.room_Id = room_Id;
        this.name = name;
        this.checkIn = checkIn;
        this.CheckOut = CheckOut;
        this.roomstatus = roomstatus;
      
    }
    
    
   
   


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
    
  

    public int getBooking_detailId() {
        return Booking_detailId;
    }

    public void setBooking_detailId(int Booking_detailId) {
        this.Booking_detailId = Booking_detailId;
    }

    public int getBooking_Id() {
        return Booking_Id;
    }

    public void setBooking_Id(int Booking_Id) {
        this.Booking_Id = Booking_Id;
    }

    public int getRoom_Id() {
        return room_Id;
    }

    public void setRoom_Id(int room_Id) {
        this.room_Id = room_Id;
    }

    public int getOccupant_capacity() {
        return occupant_capacity;
    }

    public void setOccupant_capacity(int occupant_capacity) {
        this.occupant_capacity = occupant_capacity;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIc) {
        this.checkIn = checkIc;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String CheckOut) {
        this.CheckOut = CheckOut;
    }

    public String getRoomstatus() {
        return roomstatus;
    }

    public void setRoomstatus(String roomstatus) {
        this.roomstatus = roomstatus;
    }
    
  
  
 }

