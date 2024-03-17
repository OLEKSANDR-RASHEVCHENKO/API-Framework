import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class ApiFrameWork {
    public static void main(String[] args) {
        //Add palace --> Update Place with New Address --> Get Place to validate if New address is present in response

        //given -all input detail
        //when - submit the API -resource,http method
        //then - validate the response
        //When i became an JsonFile from Developer --> content of the file to String-->content of file can convert into Byte-->Byte data to String
        //-->(.body new String((Files.readAllBytes(path.get("c://users//....")))))

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.addPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("Server","Apache/2.4.52 (Ubuntu)").extract().response().asString();

        System.out.println(response);
        JsonPath js = new JsonPath(response);// for parsing Json
        String placeId=js.getString("place_id");
        System.out.println(placeId);

        //Update Place
        String newAddress = "Summer Walk,Africa";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}").
                when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));

        //Get place
        String getPlaceResponse=given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id",placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().statusCode(200).extract().response().asString();
        JsonPath js2=ReUsableMethods.rowToJson(getPlaceResponse);
        String actualAddress=js2.getString("address");
        System.out.println(actualAddress);
        Assert.assertEquals(actualAddress,newAddress);
        //Cucumber Junit,Testng
    }



    }

