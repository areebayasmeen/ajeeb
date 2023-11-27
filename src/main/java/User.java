package com.example.demo;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String address;
    private String contactInfo;
    private List<Medicine> cart;
    private List<Order> orderHistory;

    public User(String name, String address, String contactInfo) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.cart = new ArrayList<>(); // Initialize the cart list
        this.orderHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setCart(List<Medicine> cart) {
        this.cart = cart;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addToCart(Medicine medicine) {
        cart.add(medicine);
    }

    public void removeFromCart(Medicine medicine) {
        cart.remove(medicine);
    }

    public void clearCart() {
        cart.clear();
    }

    public List<Medicine> getCart() {
        return cart;
    }


    public void placeOrder() {
        if (!cart.isEmpty()) {
            Order newOrder = new Order(this, cart);
            orderHistory.add(newOrder);
            System.out.println("Order placed! Order ID: " + newOrder.getOrderId());
            cart.clear();  // <-- Move this line after displaying the order confirmation
        } else {
//            System.out.println("Cart is empty. Add items to the cart before placing an order.");
        }
    }

}
