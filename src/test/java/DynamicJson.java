import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    @Test(dataProvider = "BooksData")
    public void addBook(String isbn,String aisle){
        RestAssured.baseURI = "http://216.10.245.166";
        String response=given().log().all().header("Content-Type","application/json")
                .body(payload.AddBook(isbn,aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js=ReUsableMethods.rowToJson(response);
        String id=js.get("ID");
        System.out.println(id);


        // deleteBook
    }
    @DataProvider(name="BooksData")
    public Object[][] getData(){
        // array = collection of elements
        // multidimensional array = collection of array
        return new Object[][]{{"udsfu","432"},{"gbvccx","8567"},{"izzvj","87453"}};
    }
}
