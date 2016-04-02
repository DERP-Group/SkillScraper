package com.derpgroup.skillscraper;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class SkillScraper {

  private static final String SKILLS_ENDPOINT = "/skills/entitlements";
  private static String url = "https://pitangui.amazon.com/api";
  private static String cookieString = "at-main=%s; ubid-main=%s";
  private static String atMain = "Atza|IQEBLzAtAhUAkXF1RBsfzVGg5QeNKwHqkK22N-4CFHGLnE3SOXaZtqlY70XBWgV9Zk_KnxLldSLDYES3FtKIhIto1BjhlpEFOQ0toVyTJYV7DhTo6pZg0glID7WhWmdXgzJvH1GRrG3bMj8l686qm1r4_2gnwV1yN8GwpoBXZIjmr6jjftb0ugCZ8Cav5wmhuAX6ZrXZipDhvBCg24Z4c5Bg5XB9jm_k229csg3L1vpD83UiaD_Q5gDGSLMUPuKmSEu9YI476xiRPy_yaz3mlp8iEM-QV3o3XcbPjwCVshLhDSCXeEjeR-hhJF4fV-50cfukJgv4FxlclREHYNX4NZ42FzE";
  private static String ubidMain = "179-4973632-4294533";

  private static ObjectMapper mapper;
  

  public static void main(String[] args) {
    mapper = new ObjectMapper();
    
    String response  = getSkillsResponse();
    System.out.println(response);
    
    SkillsWrapper skillsWrapper = deserialize(response, new TypeReference<SkillsWrapper>(){});
    System.out.println("Found " + skillsWrapper.getApps().size() + " skills");
    
    checkForShit(skillsWrapper.getApps());
  }
  
  private static void checkForShit(List<Skill> skills) {
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
    }
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
