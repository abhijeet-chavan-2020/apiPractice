package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.rest.pojo.simple.SimplePojo;
import com.rest.pojo.simple.Workspace;
import com.rest.pojo.simple.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class SimplePojoTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    String workspaceId = "";
    String workspaceName = "";

    @BeforeClass
    public void beforeClass() {
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-bf0f71d79548fe4d749682d8bca6988c0f");
        h1.put("x-mock-match-request-headers", "true");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
                .setBaseUri("https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io")
                .addHeaders(h1)
                .setContentType(ContentType.JSON)
                .setConfig(config.encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("charset","UTF-8")))
                .log(LogDetail.ALL);
        RestAssured.requestSpecification = reqSpecBldr.build().log().all();

        System.out.println("Printing Response Specification");

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.ALL);
        resSpec= responseSpecBuilder.build();
    }

    @Test
    public void postRequestWithPojo() {
        SimplePojo simplePojo= new SimplePojo();
       // SimplePojo simplePojo= new SimplePojo("name","Abhijeet");
        simplePojo.setKey1("name");
        simplePojo.setKey2("Abhijeet");
                given().
                    body(simplePojo).
                when().
                    post("/postSimplePojo").
                then().
                        spec(resSpec).
                    assertThat().
                    statusCode(200);
    }

    @Test
    public void postRequestWithPojoDeSerialization() throws JsonProcessingException {
        //To deserialize the response we use response)_.as(Classname.class) method. And collect the response in the class object of the same class provided.
        // Then we convert the request object and response object into plain string using ObjectMapper's writeValueAsString(ObjectMapper object) method.
        // Then by using ObjectMapper.readTree(String) method which return JsonNode class object, we compare this JsonNode class objects from request and response using hamcrest library.
        // Always create a default constructor in POJO classes because Jackson will look for Default constructor in POJO classes during deserialization.

        SimplePojo simplePojo= new SimplePojo();
        // SimplePojo simplePojo= new SimplePojo("name","Abhijeet");
        simplePojo.setKey1("value1");
        simplePojo.setKey2("value2");

        SimplePojo deserializedPojo=  given().
                body(simplePojo).
                when().
                post("/postSimplePojo").
                then().
                spec(resSpec).
                extract().
                response().as(SimplePojo.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String deserializedPojoString= objectMapper.writeValueAsString(deserializedPojo);
        String simplePojoString= objectMapper.writeValueAsString(simplePojo);
        assertThat(objectMapper.readTree(deserializedPojoString), equalTo(objectMapper.readTree(simplePojoString)));
    }

    @Test(dataProvider = "createWorkspaceData")
    public void createWorkspaceUsingPojo(String[] data) {
        Faker fk = new Faker();
        workspaceName = fk.name().fullName().toUpperCase().replaceAll(" ", "") + fk.number().digits(5);
        System.out.println("workspaceName = " + data[0]);

        Workspace workspace = new Workspace(data[0], data[1], data[2]);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializedWorkspaceRoot =
                given().
                        body(workspaceRoot).
                        when().
                        post("/workspaces").
                        then().
                        spec(resSpec).
                        extract().
                        response().
                        as(WorkspaceRoot.class);

        assertThat(deserializedWorkspaceRoot.getWorkspace().getName(), equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializedWorkspaceRoot.getWorkspace().getId(), matchesPattern("[a-z0-9-]{36}$"));
    }

    @DataProvider
    public Object[][] createWorkspaceData(){
        return new Object[] []{
            {new Faker().name().fullName().toUpperCase(), "My Workspace", "Rest assured created this using nested json object as request payload- data 1" },
            {new Faker().name().fullName().toUpperCase(), "My Workspace", "Rest assured created this using nested json object as request payload- data 1" },
            {new Faker().name().fullName().toUpperCase(), "My Workspace", "Rest assured created this using nested json object as request payload - data 3" }
        };
    }
}

