/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Rooms {
    private String roomno;
    private int roomType_id;
    private String roomType;
    private String floornno;
    private String status;
    public Rooms(String roomno, int roomType_id, String roomType, String floornno, String status) {
        this.roomno = roomno;
        this.roomType_id = roomType_id;
        this.roomType = roomType;
        this.floornno = floornno;
        this.status = status;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public int getRoomType_id() {
        return roomType_id;
    }

    public void setRoomType_id(int roomType_id) {
        this.roomType_id = roomType_id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getFloornno() {
        return floornno;
    }

    public void setFloornno(String floornno) {
        this.floornno = floornno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
