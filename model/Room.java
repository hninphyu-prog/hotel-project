/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author May Na Dar
 */
public class Room {
    private String roomno;
    private String roomType;
    private String floornno;
    private int roomprice;
    private String imageName; // just keep image file name or path
    private String checkInDate;
    private String checkOutDate;

    public Room(String roomno, String roomType, String floornno, int roomprice, String imageName) {
        this.roomno = roomno;
        this.roomType = roomType;
        this.floornno = floornno;
        this.roomprice = roomprice;
        this.imageName = imageName;
    }
    //for cart table

    public Room(String roomno, String roomType, int roomprice, String checkInDate, String checkOutDate) {
        this.roomno = roomno;
        this.roomType = roomType;
        this.roomprice = roomprice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
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

    public int getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(int roomprice) {
        this.roomprice = roomprice;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Room room = (Room) obj;
    return roomno.equals(room.roomno) &&
           roomType.equals(room.roomType) &&
           roomprice==roomprice &&
           checkInDate.equals(room.checkInDate) &&
           checkOutDate.equals(room.checkOutDate);
}

@Override
public int hashCode() {
    return Objects.hash(roomno, roomType, roomprice, checkInDate, checkOutDate);
}


    

   

    
    
    
    
}
