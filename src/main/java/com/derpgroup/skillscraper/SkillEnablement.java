package com.derpgroup.skillscraper;

public class SkillEnablement {

  private String applicationId;
  private String pamsPartnerId;
  private String stage;
  
  public String getApplicationId() {
    return applicationId;
  }
  
  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }
  
  public String getPamsPartnerId() {
    return pamsPartnerId;
  }
  
  public void setPamsPartnerId(String pamsPartnerId) {
    this.pamsPartnerId = pamsPartnerId;
  }
  
  public String getStage() {
    return stage;
  }
  
  public void setStage(String stage) {
    this.stage = stage;
  }

  @Override
  public String toString() {
    return "SkillEnablement [applicationId=" + applicationId
        + ", pamsPartnerId=" + pamsPartnerId + ", stage=" + stage + "]";
  }
}
