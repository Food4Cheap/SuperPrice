package com.example.food4cheap;

public class LocationDetails {
    private int locationId;
    private String streetAddress;
    private String city;
    private String storeName;

    public int getLocationId() {
        return locationId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
