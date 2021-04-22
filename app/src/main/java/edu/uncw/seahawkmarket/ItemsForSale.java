package edu.uncw.seahawkmarket;

public class ItemsForSale {
    //TODO: Include timestamp

    private String title;
    private String description;
    private String price;
    private String email;

    public ItemsForSale() {
    }

    public ItemsForSale(String title, String description, String price, String currentUserEmail) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.email = currentUserEmail;

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

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
