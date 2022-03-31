import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class spec {

    RequestSpecification req1;
    ResponseSpecification res1;

    RequestSpecification req2;
    ResponseSpecification res2;
    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();


    @BeforeTest
    public void setup() {

        requestSpecBuilder.setBaseUri("https://jsonplaceholder.typicode.com").
                addHeader("Content-Type", "application/json");

        req1 = RestAssured.with().spec(requestSpecBuilder.build());
        res1 = RestAssured.with().expect().contentType(ContentType.JSON);

        requestSpecBuilder.setBaseUri("https://reqres.in/api").
                addHeader("Content-Type", "application/json");
        req2 = RestAssured
                .with().spec(requestSpecBuilder.build());
        res2 = RestAssured.with().expect().contentType(ContentType.JSON);

    }

    @Test
    void test_get_call() {

        Response response = given().
                spec(req1).
                when().
                get("/posts").
                then().statusCode(200).
                spec(res1).extract().response();

        JSONArray arr = new JSONArray(response.asString());
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            assert obj.getInt("userId") != 40 || (obj.getInt("id") == 4);
            assert obj.get("title") != null;
            assert obj.get("title") instanceof String;
        }
    }

    @Test
    public void test_put_call() {

        File jsonData = new File("C:\\Users\\suhasm\\Desktop\\core\\postman\\REST2\\src\\test\\resources\\putdata.json");
        given().spec(req2).
                body(jsonData).
                when().
                put("/users").then().spec(res2).statusCode(200).
                body("name", equalTo("Arun")).body("job", equalTo("Manager"));
    }
}