package com.derpgroup.skillscraper;

import java.util.List;

public class SkillReviewsResponse {

  private double averageRating;
  private int numberOfReviews;
  private List<SkillReview> topReviews;
  
  public double getAverageRating() {
    return averageRating;
  }
  
  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }
  
  public int getNumberOfReviews() {
    return numberOfReviews;
  }
  
  public void setNumberOfReviews(int numberOfReviews) {
    this.numberOfReviews = numberOfReviews;
  }
  
  public List<SkillReview> getTopReviews() {
    return topReviews;
  }
  
  public void setTopReviews(List<SkillReview> topReviews) {
    this.topReviews = topReviews;
  }

  @Override
  public String toString() {
    return "SkillReviewsResponse [averageRating=" + averageRating
        + ", numberOfReviews=" + numberOfReviews + ", topReviews=" + topReviews
        + "]";
  }
}
