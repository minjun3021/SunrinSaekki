package com.kmj.sunrinsaekki.data;

public class RestaurantData {
    private String name;
    private int review;
    private String category;

    public RestaurantData(String name, int review, String category) {
        this.name = name;
        this.review = review;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
