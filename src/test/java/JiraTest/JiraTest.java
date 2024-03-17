package JiraTest;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;


public class JiraTest {
    public static void main(String[] args) {
        RestAssured.baseURI="http://localhost:8080";

        //Login Scenario
        SessionFilter sessionFilter = new SessionFilter();
        String response=given().relaxedHTTPSValidation().header("Content-Type","application/json").
                body("{ \"username\": \"rashevchenkoo\", \"password\": \"Gazmanov1234\" }")
                        .log().all().filter(sessionFilter).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

String expectedMessage = "Hi How are you?";
//Add comment
        String addCommentResponse=given().pathParam("id","10203").log().all().header("Content-Type","application/json").body("{\n" +
                "    \"body\": \""+expectedMessage+"\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}").filter(sessionFilter).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath = new JsonPath(addCommentResponse);
        String commentId=jsonPath.getString("id");

//Add attachment
        given().header("X-Atlassian-Token","no-check").
                filter(sessionFilter).pathParam("id","10203")
                .header("Content-Type","multipart/form-data")
                .multiPart("file",new File("jira.txt")).when().post("/rest/api/2/issue/{id}/attachments").then().log().all()
                .assertThat().statusCode(200);

        // Get issue
        String issueDetails=given().filter(sessionFilter).pathParam("id","10203")
                .queryParam("fields","comment").log().all().
                when().get("/rest/api/2/issue/{id}").
                then().log().all().extract().response().asString();
        System.out.println(issueDetails);

        JsonPath js= new JsonPath(issueDetails);
        int commentsCount=js.getInt("fields.comment.comments.size()");
        for (int i = 0;i<commentsCount;i++){
            String commentIdIssue = js.get("fields.comment.comments.size["+i+"].id").toString();
            if(commentIdIssue.equalsIgnoreCase(commentId)){
                String massege=js.get("fields.comment.comments.size["+i+"].body").toString();
                System.out.println(massege);
                Assert.assertEquals(massege,expectedMessage);
            }
        }

    }
}
