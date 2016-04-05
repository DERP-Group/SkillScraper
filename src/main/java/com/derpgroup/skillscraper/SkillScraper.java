package com.derpgroup.skillscraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class SkillScraper {

  private static final String SKILLS_ENDPOINT = "/skills/entitlements";
  private static final String SKILL_REVIEWS_ENDPOINT = "/skill/review/get-reviews/%s/%d";
  private static String url = "https://pitangui.amazon.com/api";
  private static String cookieString = "at-main=%s; ubid-main=%s";
  private static String atMain = "Atza|IQEBLzAtAhUAkXF1RBsfzVGg5QeNKwHqkK22N-4CFHGLnE3SOXaZtqlY70XBWgV9Zk_KnxLldSLDYES3FtKIhIto1BjhlpEFOQ0toVyTJYV7DhTo6pZg0glID7WhWmdXgzJvH1GRrG3bMj8l686qm1r4_2gnwV1yN8GwpoBXZIjmr6jjftb0ugCZ8Cav5wmhuAX6ZrXZipDhvBCg24Z4c5Bg5XB9jm_k229csg3L1vpD83UiaD_Q5gDGSLMUPuKmSEu9YI476xiRPy_yaz3mlp8iEM-QV3o3XcbPjwCVshLhDSCXeEjeR-hhJF4fV-50cfukJgv4FxlclREHYNX4NZ42FzE";
  private static String ubidMain = "179-4973632-4294533";
  private static int sleepTimeMillis = 10000;
  private static int maxSkillsToCheck = -1;

  private static ObjectMapper mapper;
  

  public static void main(String[] args) {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    
    String response  = getSkillsResponse();
    
    List<Skill> skills = deserialize(response, new TypeReference<SkillsWrapper>(){}).getApps();
    System.out.println("Found " + skills.size() + " skills");
    
    Map<Skill, SkillReviewsResponse> skillReviewsBySkillId = getReviewsForSkills(skills);
    
    System.out.println("Found reviews for " + skillReviewsBySkillId.size() + " skills.");
      
    SkillsFileUtils.writeShitOut(skillReviewsBySkillId);
    
    checkForShitOnSkills(skills);
    checkForShitOnReviews(skillReviewsBySkillId);
      
    //TODO: checkForShitOnReviews (reviewer id; reviewer name matches skill vendor name, Donald C. Lindenmuth)
    //TODO: checkForChangesInStoredShit (deleted reviews, statistically significant changes in review volume)
  }

  

  public static String getSkillsResponse(){

    GetRequest skillsRequest = Unirest.get(url + SKILLS_ENDPOINT);
    skillsRequest.header("Cookie", String.format(cookieString, atMain,ubidMain));
    
    HttpResponse<String> response;
    try {
      response = skillsRequest.asString();
    } catch (UnirestException e) {
      e.printStackTrace();
      return null;
    }
    
    return response.getBody();
  }
  
  private static Map<Skill, SkillReviewsResponse> getReviewsForSkills(List<Skill> skills) {
    Map<Skill, SkillReviewsResponse> skillReviewsBySkillId = new HashMap<Skill,SkillReviewsResponse>();
    
    if(skills == null || skills.size() < 1){
      return null;
    }
    
    int i = 0;
    for(Skill skill : skills){
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

  public static SkillReviewsResponse getReviewsForSkill(String asin) {

    int pageIndex = 0;
    int numReviewsReturned = 0;
    SkillReviewsResponse skillReviewsOutput = new SkillReviewsResponse();
    do{
      System.out.println("Getting reviews for '" + asin + "' from page " + pageIndex);
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
    System.out.println(skillReviewsOutput);
    
    return skillReviewsOutput;
  }

  private static void checkForShitOnSkills(List<Skill> skills) {
    for(Skill skill : skills){
      if(!skill.isCanDisable()){
        continue; //Ignore dev skills
      }
      if(skill.getPermissions() != null){
        System.out.println("Permissions was non-null for skill '" + skill.getName() + "': " + skill.getPermissions());
      }
      
      if(skill.getPamsPartnerId() != null){
        System.out.println("PamsPartnerId was non-null for skill '" + skill.getName() + "': " + skill.getPamsPartnerId());
      }
      
      if(skill.getEnablement() != null){
        System.out.println("Enablement was non-null for skill '" + skill.getName() + "': " + skill.getEnablement().toString());
      }
      
      //TODO: What's up with capabilities?
      //TODO: Skill types?
      //TODO: Enablement vs top level fields?
    }
  }
  
  private static void checkForShitOnReviews(Map<Skill,SkillReviewsResponse> skillReviewsBySkill){
    if(skillReviewsBySkill == null){
      return;
    }
    
    for(Skill skill : skillReviewsBySkill.keySet()){
      if(skill == null){
        System.out.println("Skill was null.");
        continue;
      }
      SkillReviewsResponse skillReviews = skillReviewsBySkill.get(skill);
      
      if(skillReviews == null || skillReviews.getTopReviews() == null){
        continue;
      }
      
      for(SkillReview skillReview : skillReviews.getTopReviews()){
        if(skillReview == null){
          System.out.println("Skill review was null.");
          continue;
        }
        if(skillReview.getReviewer() != null && skillReview.getReviewer().equalsIgnoreCase(skill.getVendorName())){
          System.out.println("Possible self-review found for skill '" + skill.getName() + "'!");
        }
      }
    }
  }
  
  public static <T> T deserialize(String responseString, TypeReference<T> typeReference){
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
