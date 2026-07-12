package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.LocalTime;

public class HallBookingList {
    private final SimpleIntegerProperty bookingDetailId;
    private final SimpleStringProperty guestName;
    private final SimpleStringProperty phoneNo;
    private final SimpleStringProperty hallId;
    private final SimpleObjectProperty<LocalDate> bookDate;
    private final SimpleObjectProperty<LocalDate> wantedDate;
    private final SimpleObjectProperty<LocalTime> startTime;
    private final SimpleObjectProperty<LocalTime> endTime;
    private final SimpleStringProperty bookingStatus;
    private final SimpleIntegerProperty price;

    public HallBookingList(
            int bookingDetailId,
            String guestName,
            String phoneNo,
            String hallId,
            LocalDate bookDate,
            LocalDate wantedDate,
            LocalTime startTime,
            LocalTime endTime,
            String bookingStatus,
            int price  // ✅ Fixed: removed semicolon, added comma before this line
    ) {
        this.bookingDetailId = new SimpleIntegerProperty(bookingDetailId);
        this.guestName = new SimpleStringProperty(guestName);
        this.phoneNo = new SimpleStringProperty(phoneNo);
        this.hallId = new SimpleStringProperty(hallId);
        this.bookDate = new SimpleObjectProperty<>(bookDate);
        this.wantedDate = new SimpleObjectProperty<>(wantedDate);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
        this.bookingStatus = new SimpleStringProperty(bookingStatus);
        this.price = new SimpleIntegerProperty(price);  // ✅ Fixed: now initialized
    }

    public int getBookingDetailId() {
        return bookingDetailId.get();
    }

    public String getGuestName() {
        return guestName.get();
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    public String getHallId() {
        return hallId.get();
    }

    public LocalDate getBookDate() {
        return bookDate.get();
    }

    public LocalDate getWantedDate() {
        return wantedDate.get();
    }

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public LocalTime getEndTime() {
        return endTime.get();
    }

    public String getBookingStatus() {
        return bookingStatus.get();
    }

    public int getPrice() {  // ✅ Added getter
        return price.get();
    }

    // Optional: property accessors
    public SimpleIntegerProperty priceProperty() {
        return price;
    }
}
