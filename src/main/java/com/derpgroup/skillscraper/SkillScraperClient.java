package com.derpgroup.skillscraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

/**
 * This client is built to interact with the alexa web view at alexa.amazon.com.
 * It allows you to retrieve the entire list of skills,
 * and aggregate reviews for those skills.
 * As it is using APIs that are not meant for public consumption,
 * it's likely it will break over time as Amazon's data model changes.
 * @author Eric
 *
 */
public class SkillScraperClient {

  private static final String SKILLS_ENDPOINT = "/skills/entitlements";
  private static final String SKILL_REVIEWS_ENDPOINT = "/skill/review/get-reviews/%s/%d";
  
  private static String url;
  private static String cookieString;
  private static String atMain;
  private static String ubidMain;
  private static int sleepTimeMillis;
  private static int maxSkillsToCheck = -1; //Any number less than zero will check all skills

  private static ObjectMapper mapper;
  
  public SkillScraperClient(String configFileLocation){
    if(StringUtils.isEmpty(configFileLocation)){
      String message = "No config file found.";
      System.err.println(message);
      throw new RuntimeException(message);
    }
    
    try {
      init(configFileLocation);
    } catch (ConfigurationException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }

    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }
  
  private void init(String configFileLocation) throws ConfigurationException {
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
        .configure(params.properties()
            .setFileName(configFileLocation));
    
    Configuration config = builder.getConfiguration();
    url = config.getString("url");
    cookieString = config.getString("cookieString");
    atMain = config.getString("atMain");
    ubidMain = config.getString("ubidMain");
    sleepTimeMillis = config.getInt("sleepTimeMillis");
    if(config.containsKey("maxSkillsToCheck")){
      maxSkillsToCheck = config.getInt("maxSkillsToCheck");
    }
  }

  /**
   * Retrieve a list of all skills
   * @return
   */
  public List<Skill> getSkillsResponse(){

    System.out.println("Getting list of skills.");
    GetRequest skillsRequest = Unirest.get(url + SKILLS_ENDPOINT);
    skillsRequest.header("Cookie", String.format(cookieString, atMain,ubidMain));
    
    HttpResponse<String> response;
    try {
      response = skillsRequest.asString();
    } catch (UnirestException e) {
      e.printStackTrace();
      return null;
    }
    
    List<Skill> skills = deserialize(response.getBody(), new TypeReference<SkillsWrapper>(){}).getApps();

    System.out.println("Found " + skills.size() + " skills.");
    return skills;
  }
  
  /**
   * Retrieve all reviews for a list of skills
   * @param skills
   * @return
   */
  public Map<Skill, SkillReviewsResponse> getReviewsForSkills(List<Skill> skills) {
    Map<Skill, SkillReviewsResponse> skillReviewsBySkillId = new HashMap<Skill,SkillReviewsResponse>();
    
    if(skills == null || skills.size() < 1){
      return null;
    }
    
    int i = 0;
    for(Skill skill : skills){
      //Ignore development skills
      if(skill.isCanDisable() && skill.getNumberOfReviews() > 0){
        SkillReviewsResponse skillReviewsResponse = getReviewsForSkill(skill.getAsin());
        skillReviewsBySkillId.put(skill, skillReviewsResponse);
        i++;
      }
      if(i == maxSkillsToCheck){
        break;
      }
    }
    return skillReviewsBySkillId;
  }

  /**
   * Make a call to the reviews endpoint, check if there are additional skills to retrieve, and if so repeat
   * @param asin The short unique id string for this skill
   * @return
   */
  public SkillReviewsResponse getReviewsForSkill(String asin) {

    int pageIndex = 0;
    int numReviewsReturned = 0;
    SkillReviewsResponse skillReviewsOutput = new SkillReviewsResponse();
    do{
      System.out.println("Getting reviews for '" + asin + "' from page " + (pageIndex + 1));
      GetRequest skillReviewsRequest = Unirest.get(String.format(url + SKILL_REVIEWS_ENDPOINT, asin, (pageIndex * 10)));
      skillReviewsRequest.header("Cookie", String.format(cookieString, atMain,ubidMain));
      
      HttpResponse<String> response;
      try {
        response = skillReviewsRequest.asString();
      } catch (UnirestException e) {
        e.printStackTrace();
        return null;
      }
      
      SkillReviewsWrapper wrapper = deserialize(response.getBody(), new TypeReference<SkillReviewsWrapper>(){});
      if(wrapper == null || wrapper.getSkillReviews() == null){
        System.out.println("Could not deserialize response.");
        System.out.println(response);
        continue;
      }
      SkillReviewsResponse skillReviewsResponse = wrapper.getSkillReviews();
      List<SkillReview> topReviews = skillReviewsResponse.getTopReviews();
      
      if(pageIndex == 0){
        skillReviewsOutput.setTopReviews(topReviews);
        skillReviewsOutput.setAverageRating(skillReviewsResponse.getAverageRating());
        skillReviewsOutput.setNumberOfReviews(skillReviewsResponse.getNumberOfReviews());
      }else{
        skillReviewsOutput.getTopReviews().addAll(topReviews);
      }
      
      numReviewsReturned = topReviews.size();
      pageIndex++;
      
      try {
        Thread.sleep(sleepTimeMillis);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }while(numReviewsReturned >= 10);
    
    System.out.println("Found " + skillReviewsOutput.getNumberOfReviews() + " reviews for '" + asin + "'.");
    
    return skillReviewsOutput;
  }
  
  public <T> T deserialize(String responseString, TypeReference<T> typeReference){
    try {
      return mapper.readValue(responseString, typeReference);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
