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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SkillsFileUtilsTest {

  private ObjectMapper mapper;
  
  private String sampleReview = "{\"helpfulVotes\":9,\"rating\":5.0,\"reviewId\":\"R1HDBRGC8PJNSD\",\"reviewText\":\"Awesome way to learn about AWS cloud computing service.\","
      + "\"reviewTitle\":\"Awesome AWS learning tool.\",\"reviewer\":\"Jarjis Imam\",\"submissionDate\":\"March 18, 2016\",\"totalVotes\":18}";
  private String sampleReviews = "[{\"helpfulVotes\":2,\"rating\":5.0,\"reviewId\":\"R6WPULDHW5OXO\",\"reviewText\":\"Good questions.  I got 4 of 5 correct!\","
      + "\"reviewTitle\":\"Quick College Basketball Trivia Fun\",\"reviewer\":\"Terren Peterson\",\"submissionDate\":\"April 02, 2016\",\"totalVotes\":2},"
      + "{\"helpfulVotes\":0,\"rating\":5.0,\"reviewId\":\"R3MXLTCGTLEDX9\",\"reviewText\":\"Incredible trivia game. Well done. Still trying to go 5 for 5!\","
      + "\"reviewTitle\":\"Great basketball trivia!\",\"reviewer\":null,\"submissionDate\":\"April 03, 2016\",\"totalVotes\":0},"
      + "{\"helpfulVotes\":0,\"rating\":4.0,\"reviewId\":\"R2PB5K4E2SFWLY\",\"reviewText\":\"Neat skill but could be improved.\","
      + "\"reviewTitle\":\"Good skill\",\"reviewer\":\"Erik\",\"submissionDate\":\"April 03, 2016\",\"totalVotes\":0}]";
  private String sampleSkill = "{\"accountLinkingWhitelistedDomains\":null,\"asin\":\"B0184JBP4I\",\"averageRating\":4.0,\"canDisable\":true,\"capabilities\":null,"
      + "\"category\":\"Travel\",\"description\":\"Gives Washington DC Metro light rail train arrival and departure times, as well as maintenance or incident reports. "
      + "It will remember the last station you requested times for and save it, setting it as default if a station is not specified next time.\","
      + "\"enablement\":null,\"exampleInteractions\":[\"Alexa, ask DC Metro for the next train arriving at Columbia Heights\","
      + "\"Alexa, ask DC Metro is there weekend work\",\"Alexa, ask DC Metro for the next train from metro center\"],\"firstReleaseDate\":1.447791220393E9,"
      + "\"homepageLinkText\":null,\"homepageLinkUrl\":null,\"id\":\"amzn1.echo-sdk-ams.app.4fa44b35-dc28-456a-8f8d-7690ed1c284e\",\"imageAltText\":\"DC Metro icon\","
      + "\"imageUrl\":\"https://s3.amazonaws.com/CAPS-SSE/echo_developer/240c97eb28f643f7970cdff9fe976895/APP_ICON?versionId=3rU44tcogFsnP68UgpVZfjzwHy8.pNR.&AWSAccessKeyId=AKIAJOWWTPLZVE3OR7BQ&Expires=1459757743&Signature=rn5SM%2FUeOeFJ0DDH9MER1wJU%2BkA%3D\","
      + "\"inAppPurchasingSupported\":false,\"launchPhrase\":\"dc metro\",\"name\":\"DC Metro\",\"numberOfReviews\":3,"
      + "\"pamsPartnerId\":null,\"permissions\":null,\"privacyPolicyUrl\":null,\"shortDescription\":"
      + "\"Gives Washington DC Metro light rail train arrival and departure times, as well as maintenance or incident reports.\","
      + "\"skillTypes\":null,\"stage\":\"live\",\"termsOfUseUrl\":null,\"vendorId\":\"MGHW9XDBILJYE\",\"vendorName\":\"Jimmy Cartrette\"}";
  
  @Before
  public void setup(){
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }
  
  @Test
  public void testWriteReview() throws JsonParseException, JsonMappingException, IOException{
    String outputDir = "./test/output/123456/B235546/";

    Path path = Paths.get(outputDir);
    try {
      Files.createDirectories(path);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    SkillReview skillReview = mapper.readValue(sampleReview, new TypeReference<SkillReview>(){});
    SkillsFileUtils.writeSkillReview(outputDir, skillReview);
  }
  
  @Test
  public void testWriteSkillReviews() throws JsonParseException, JsonMappingException, IOException{
    String outputDir = "./test/output/" + System.currentTimeMillis() + "/";

    Path path = Paths.get(outputDir);
    try {
      Files.createDirectories(path);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    List<SkillReview> skillReviews = mapper.readValue(sampleReviews, new TypeReference<List<SkillReview>>(){});
    SkillsFileUtils.writeSkillReviews(outputDir, skillReviews);
  }
  
  @Test
  public void testWriteSkill() throws JsonParseException, JsonMappingException, IOException{
    String outputDir = "./test/output/" + System.currentTimeMillis() + "/";

    Path path = Paths.get(outputDir);
    try {
      Files.createDirectories(path);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    List<SkillReview> skillReviews = mapper.readValue(sampleReviews, new TypeReference<List<SkillReview>>(){});
    Skill skill = mapper.readValue(sampleSkill, new TypeReference<Skill>(){});
    SkillsFileUtils.writeSkill(outputDir, skill, skillReviews);
  }
}
