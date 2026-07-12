/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty; // Import SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate; // Import LocalDate

/**
 *
 * @author Dell
 */
public class BookingList {
    private final SimpleIntegerProperty bookingDetailId;
    private final SimpleStringProperty guestName;
    private final SimpleStringProperty phoneNo;
    private final SimpleStringProperty roomNo;
    private final SimpleObjectProperty<LocalDate> bookDate;
    private final SimpleObjectProperty<LocalDate> checkInDate;
    private final SimpleObjectProperty<LocalDate> checkOutDate;
    private final SimpleStringProperty roomStatus;

    // Corrected constructor parameters
    public BookingList(SimpleIntegerProperty bookingDetailId, SimpleStringProperty guestName, SimpleStringProperty phoneNo, SimpleStringProperty roomNo,
                       SimpleObjectProperty<LocalDate> bookDate, SimpleObjectProperty<LocalDate> checkInDate, SimpleObjectProperty<LocalDate> checkOutDate,
                       SimpleStringProperty roomStatus) {
        this.bookingDetailId = bookingDetailId;
        this.guestName = guestName;
        this.phoneNo = phoneNo;
        this.roomNo = roomNo;
        this.bookDate = bookDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomStatus = roomStatus;
    }

    public SimpleIntegerProperty bookingDetailIdProperty() {
        return bookingDetailId;
    }
    
    public int getBookingDetailId() {
        return bookingDetailId.get();
    }

    public SimpleStringProperty guestNameProperty() {
        return guestName;
    }

    public String getGuestName() {
        return guestName.get();
    }

    public SimpleStringProperty phoneNoProperty() {
        return phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    public SimpleStringProperty roomNoProperty() {
        return roomNo;
    }

    public String getRoomNo() {
        return roomNo.get();
    }

    // Corrected getter return types
    public SimpleObjectProperty<LocalDate> bookDateProperty() {
        return bookDate;
    }

    public LocalDate getBookDate() {
        return bookDate.get();
    }

    // Corrected getter return types
    public SimpleObjectProperty<LocalDate> checkInDateProperty() {
        return checkInDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate.get();
    }

    // Corrected getter return types
    public SimpleObjectProperty<LocalDate> checkOutDateProperty() {
        return checkOutDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate.get();
    }

    public SimpleStringProperty roomStatusProperty() {
        return roomStatus;
    }

    public String getRoomStatus() {
        return roomStatus.get();
    }
}