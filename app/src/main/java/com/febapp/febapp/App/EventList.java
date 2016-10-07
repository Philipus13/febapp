package com.febapp.febapp.App;


import java.io.Serializable;

/**
 * Created by HANGGI on 25/02/2016.
 */
public class EventList implements Serializable {
    private String
            name,
            description,
            organizer,
            address,
            thumbnail,
            id,
            start_on,
            category_name;

    private int

            savedId,
            category,
            price,
            stok,
            attendee;

    private boolean isDone, attended;

    private Double latitude, longitude;


    public EventList() {
    }

    public EventList(String address, int attendee, String description, String id, boolean isDone, String name, String organizer, int price, String start_on, String thumbnail) {
        this.address = address;
        this.attendee = attendee;
        this.description = description;
        this.id = id;
        this.isDone = isDone;
        this.name = name;
        this.organizer = organizer;
        this.price = price;
        this.start_on = start_on;
        this.thumbnail = thumbnail;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSavedId() {
        return savedId;
    }

    public void setSavedId(int savedId) {
        this.savedId = savedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStok() {
        return price;
    }

    public void setStok(int price) {
        this.price = price;
    }

    public String getStartDate() {
        return start_on;
    }

    public void setStartDate(String start_on) {
        this.start_on = start_on;
    }

    public int getAttendee() {
        return attendee;
    }

    public void setAttendee(int attendee) {
        this.attendee = attendee;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
