package com.derpgroup.skillscraper;

import java.util.List;

public class Skill {

  private List<String> accountLinkingWhitelistedDomains;
  private String asin;
  private double averageRating;
  private boolean canDisable;
  private Skill capabilities; //Figure out what object type this is
  private String category;
  private String description;
  private SkillEnablement enablement; //Figure out what object type this is
  private List<String> exampleInteractions;
  private double firstReleaseDate;
  private String homepageLinkText;
  private String homepageLinkUrl;
  private String id;
  private String imageAltText;
  private String imageUrl;
  private boolean inAppPurchasingSupported;
  private String launchPhrase;
  private String name;
  private int numberOfReviews;
  private String pamsPartnerId;
  private List<String> permissions;
  private String privacyPolicyUrl;
  private String shortDescription;
  private List<String> skillTypes;
  private String stage;
  private String termsOfUseUrl;
  private String vendorId;
  private String vendorName;
  
  public List<String> getAccountLinkingWhitelistedDomains() {
    return accountLinkingWhitelistedDomains;
  }
  
  public void setAccountLinkingWhitelistedDomains(
      List<String> accountLinkingWhitelistedDomains) {
    this.accountLinkingWhitelistedDomains = accountLinkingWhitelistedDomains;
  }

  public String getAsin() {
    return asin;
  }

  public void setAsin(String asin) {
    this.asin = asin;
  }

  public double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }

  public boolean isCanDisable() {
    return canDisable;
  }

  public void setCanDisable(boolean canDisable) {
    this.canDisable = canDisable;
  }

  public Skill getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Skill capabilities) {
    this.capabilities = capabilities;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SkillEnablement getEnablement() {
    return enablement;
  }

  public void setEnablement(SkillEnablement enablement) {
    this.enablement = enablement;
  }

  public List<String> getExampleInteractions() {
    return exampleInteractions;
  }

  public void setExampleInteractions(List<String> exampleInteractions) {
    this.exampleInteractions = exampleInteractions;
  }

  public double getFirstReleaseDate() {
    return firstReleaseDate;
  }

  public void setFirstReleaseDate(double firstReleaseDate) {
    this.firstReleaseDate = firstReleaseDate;
  }

  public String getHomepageLinkText() {
    return homepageLinkText;
  }

  public void setHomepageLinkText(String homepageLinkText) {
    this.homepageLinkText = homepageLinkText;
  }

  public String getHomepageLinkUrl() {
    return homepageLinkUrl;
  }

  public void setHomepageLinkUrl(String homepageLinkUrl) {
    this.homepageLinkUrl = homepageLinkUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getImageAltText() {
    return imageAltText;
  }

  public void setImageAltText(String imageAltText) {
    this.imageAltText = imageAltText;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public boolean isInAppPurchasingSupported() {
    return inAppPurchasingSupported;
  }

  public void setInAppPurchasingSupported(boolean inAppPurchasingSupported) {
    this.inAppPurchasingSupported = inAppPurchasingSupported;
  }

  public String getLaunchPhrase() {
    return launchPhrase;
  }

  public void setLaunchPhrase(String launchPhrase) {
    this.launchPhrase = launchPhrase;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumberOfReviews() {
    return numberOfReviews;
  }

  public void setNumberOfReviews(int numberOfReviews) {
    this.numberOfReviews = numberOfReviews;
  }

  public String getPamsPartnerId() {
    return pamsPartnerId;
  }

  public void setPamsPartnerId(String pamsPartnerId) {
    this.pamsPartnerId = pamsPartnerId;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public String getPrivacyPolicyUrl() {
    return privacyPolicyUrl;
  }

  public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
    this.privacyPolicyUrl = privacyPolicyUrl;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public List<String> getSkillTypes() {
    return skillTypes;
  }

  public void setSkillTypes(List<String> skillTypes) {
    this.skillTypes = skillTypes;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getTermsOfUseUrl() {
    return termsOfUseUrl;
  }

  public void setTermsOfUseUrl(String termsOfUseUrl) {
    this.termsOfUseUrl = termsOfUseUrl;
  }

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }
}
