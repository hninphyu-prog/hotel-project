/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MITUSER-1
 */

public class OccupiedRoomData {
    private int booking_detail_id;
    private int booking_id;

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public OccupiedRoomData(int booking_detail_id, int booking_id, int room_id, String guest_name, String check_in, String check_out) {
        this.booking_detail_id = booking_detail_id;
        this.booking_id = booking_id;
        this.room_id = room_id;
        this.guest_name = guest_name;
        this.check_in = check_in;
        this.check_out = check_out;
    }
    private int room_id;
    private String guest_name;
    private String check_in;
    private String check_out;

    public OccupiedRoomData(int booking_detail_id, int room_id, String guest_name, String check_in, String check_out) {
        this.booking_detail_id = booking_detail_id;
        this.room_id = room_id;
        this.guest_name = guest_name;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public int getBooking_detail_id() {
        return booking_detail_id;
    }

    public void setBooking_detail_id(int booking_detail_id) {
        this.booking_detail_id = booking_detail_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }
    
    
}