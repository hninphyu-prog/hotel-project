package model;

public class ServiceDetail {
    private String clothName;
    private String serviceType;
    private int quantity;
    private double price;
    private String orderDate;
    private String orderStatus;

    public ServiceDetail(String clothName, String serviceType, int quantity, double price, String orderDate, String orderStatus) {
        this.clothName = clothName;
        this.serviceType = serviceType;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public String getClothName() { return clothName; }
    public String getServiceType() { return serviceType; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
}
