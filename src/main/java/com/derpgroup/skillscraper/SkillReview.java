package com.derpgroup.skillscraper;

public class SkillReview {

  private int helpfulVotes;
  private double rating;
  private String reviewId;
  private String reviewText;
  private String reviewTitle;
  private String reviewer;
  private String submissionDate;
  private int totalVotes;
  
  public int getHelpfulVotes() {
    return helpfulVotes;
  }
  
  public void setHelpfulVotes(int helpfulVotes) {
    this.helpfulVotes = helpfulVotes;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getReviewId() {
    return reviewId;
  }

  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  public String getReviewText() {
    return reviewText;
  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public String getReviewer() {
    return reviewer;
  }

  public void setReviewer(String reviewer) {
    this.reviewer = reviewer;
  }

  public String getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(String submissionDate) {
    this.submissionDate = submissionDate;
  }

  public int getTotalVotes() {
    return totalVotes;
  }

  public void setTotalVotes(int totalVotes) {
    this.totalVotes = totalVotes;
  }

  @Override
  public String toString() {
    return "SkillReview [helpfulVotes=" + helpfulVotes + ", rating=" + rating
        + ", reviewId=" + reviewId + ", reviewText=" + reviewText
        + ", reviewTitle=" + reviewTitle + ", reviewer=" + reviewer
        + ", submissionDate=" + submissionDate + ", totalVotes=" + totalVotes
        + "]";
  }
}
