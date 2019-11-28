package com.kmj.sunrinsaekki.data;

public class RestaurantData {
    private String name;
    private String review;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RestaurantData(String name, String review, String category) {
        this.name = name;
        this.review = review;
        this.category = category;
    }
}
