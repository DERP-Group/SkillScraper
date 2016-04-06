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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SkillsFileUtils {


  private static ObjectMapper mapper;

  private static String jsonFileOutputRootPath = "./output/";
  
  static{
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }
  
  protected static void writeShitOut(Map<Skill, SkillReviewsResponse> skillReviewsBySkill) {
    long currentTime = System.currentTimeMillis();
    
    String outputPath = String.format(jsonFileOutputRootPath + "%d/", currentTime);
    Path path = Paths.get(outputPath);
    try {
      Files.createDirectories(path);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    for(Skill skill : skillReviewsBySkill.keySet()){
      List<SkillReview> skillReviews = skillReviewsBySkill.get(skill).getTopReviews();
      writeSkill(outputPath, skill, skillReviews);
    }
  }

  public static void writeSkill(String outputPath, Skill skill, List<SkillReview> skillReviews) {
    String skillOutputPath = String.format(outputPath + "%s/", skill.getAsin());
    Path skillPath = Paths.get(skillOutputPath);
    try {
      Files.createDirectories(skillPath);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    try {
      mapper.writeValue(new File(skillOutputPath + skill.getName() + ".json"), skill);
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(skillReviews != null && skillReviews.size() > 0){

      writeSkillReviews(skillOutputPath, skillReviews);
    }
  }

  protected static void writeSkillReviews(String skillOutputPath, List<SkillReview> skillReviews) {
    String skillReviewsOutputPath = skillOutputPath + "reviews/";
    Path skillReviewsPath = Paths.get(skillReviewsOutputPath);
    try {
      Files.createDirectories(skillReviewsPath);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    for(SkillReview skillReview : skillReviews){

      writeSkillReview(skillReviewsOutputPath, skillReview);
    }
  }

  protected static void writeSkillReview(String skillReviewsOutputPath, SkillReview skillReview) {
    try {
      mapper.writeValue(new File(skillReviewsOutputPath + skillReview.getReviewId() + ".json"), skillReview);
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
