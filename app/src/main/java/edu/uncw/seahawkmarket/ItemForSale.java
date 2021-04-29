package edu.uncw.seahawkmarket;

import java.util.Date;

public class ItemForSale {
    //TODO: Include timestamp

    private String title;
    private String description;
    private String price;
    private String user;
    private Date datePosted;

    public ItemForSale() {
    }

    public ItemForSale(String title, String description, String price, String user, Date datePosted) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.user = user;
        this.datePosted = datePosted;
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

    public String getUser() {
        return user;
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
}
