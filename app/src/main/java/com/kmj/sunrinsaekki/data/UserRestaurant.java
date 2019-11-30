package com.kmj.sunrinsaekki.data;

public class UserRestaurant {
    private int review;
    private String category;
    private String star;

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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    private String visit;

    public UserRestaurant() {
    }

    public UserRestaurant( int review, String category, String star, String visit) {

        this.review = review;
        this.category = category;
        this.star = star;
        this.visit = visit;
    }
}
