package edu.uncw.seahawkmarket;

public class ItemsForSale {
    private String title;
    private String description;
    private Float price;

    public ItemsForSale() {
    }

    public ItemsForSale(String title, String description, Float price) {
        this.title = title;
        this.description = description;
        this.price = price;
//        this.created = firebase.firestore.FieldValue.serverTimestamp()
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
