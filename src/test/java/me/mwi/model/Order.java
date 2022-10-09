package me.mwi.model;

import java.util.List;

public class Order {

    public String firstName;
    public String lastName;
    public String city;
    public Integer orderNumber;
    public boolean orderIsPaid;
    public OrderDetail orderDetail;
    public static class OrderDetail {
        public String orderDate;
        public short products;
        public String paymentType;
        public List<String> productsDetail;
    }

}
