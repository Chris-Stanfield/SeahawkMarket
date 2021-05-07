package edu.uncw.seahawkmarket;

import java.util.Date;

public class ItemForSale {
    //TODO: Include timestamp

    private String title;
    private String description;
    private String price;
    private String email;
    private Date datePosted;
    private String imageFile;

    public ItemForSale() {
    }

    public ItemForSale(String title, String description, String price, String email, Date datePosted, String imageFile) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.email = email;
        this.datePosted = datePosted;
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public String getEmail() {
        return email;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }



}
