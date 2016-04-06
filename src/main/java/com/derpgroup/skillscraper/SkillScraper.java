/**
 * Copyright (C) 2016 David Phillips
 * Copyright (C) 2016 Eric Olson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.derpgroup.skillscraper;

import java.util.List;
import java.util.Map;

/**
 * Utility for aggregating information about skills and skill reviews.
 * @author Eric
 *
 */
public class SkillScraper {  

  public static void main(String[] args) {
    
    if(args == null || args.length < 1){
      System.err.println("No arguments provided, which means required argument 'configFileLocation' not found.");
      System.exit(1);
    }
    
    String configFileLocation = args[0];
    
    SkillScraperClient client = new SkillScraperClient(configFileLocation);
    
    List<Skill> skills = client.getSkillsResponse();
    
    Map<Skill, SkillReviewsResponse> skillReviewsBySkillId = client.getReviewsForSkills(skills);
      
    SkillsFileUtils.writeShitOut(skillReviewsBySkillId);
    
    //Optionally, do some post processing of the data while we have it in memory 
    analyze(skills, skillReviewsBySkillId);
  }

  private static void analyze(List<Skill> skills, Map<Skill, SkillReviewsResponse> skillReviewsBySkillId) {
    analyzeSkills(skills);
    analyzeSkillReviews(skillReviewsBySkillId);
  }

  private static void analyzeSkills(List<Skill> skills) {
    for(Skill skill : skills){
      
      //Ignore dev skills
      if(!skill.isCanDisable()){
        continue; 
      }
      
      //Some sample things you could check
      if(skill.getPermissions() != null){
        System.out.println("Permissions was non-null for skill '" + skill.getName() + "': " + skill.getPermissions());
      }
      
      if(skill.getEnablement() != null){
        System.out.println("Enablement was non-null for skill '" + skill.getName() + "': " + skill.getEnablement().toString());
      }
    }
  }
  
  private static void analyzeSkillReviews(Map<Skill,SkillReviewsResponse> skillReviewsBySkill){
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

}
