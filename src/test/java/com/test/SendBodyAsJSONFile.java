package com.test;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SendBodyAsJSONFile {

    RequestSpecification reqSpec;
    String workspaceId="";
    String workspaceName="";
    @BeforeClass
    public void beforeClass() {
        //created delete request and response both Default specification builder in the before class
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-bf0f71d79548fe4d749682d8bca6988c0f");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
        .setBaseUri("https://api.postman.com")
        .addHeaders(h1)
                .setContentType(ContentType.JSON)
        .log(LogDetail.ALL);
        RestAssured.requestSpecification =  reqSpecBldr.build().log().all();

        System.out.println("Printing Response Specification");

        ResponseSpecBuilder responseSpecBuilder= new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.ALL);
        RestAssured.responseSpecification=  responseSpecBuilder.build();
        reqSpec=  reqSpecBldr.build();
    }

    @Test
    public void createWorkspaceUsingJsonFile() {
        File fl= new File("src/main/resources/CreateWorkspacePayload.json");
        if(fl.exists()){
            try {
                FileInputStream fis= new FileInputStream(fl);
                JSONObject jsonObject= new JSONObject(fis);
                System.out.println("jsonObject.getJSONArray(\"workspace\").getJSONObject(0).get(\"name\") = " + jsonObject.getJSONArray("workspace").getJSONObject(0).get("name").toString());
                workspaceName=  jsonObject.getJSONArray("workspace").getJSONObject(0).get("name").toString();
            }catch (Exception e){

            }
        }

        workspaceId= given().
                body(fl).
                when().
                post("/workspaces").
                then().
               extract().response().getBody().jsonPath().getString("workspace.id");
        System.out.println("workspaceId = " + workspaceId);
    }

    @Test(dependsOnMethods = {"createWorkspaceUsingJsonFile"})
    public void deleteMethodWithBodyAsStringBDD() {
        //Use Payload as Body in String format.
        given().
        pathParam("workspaceId",workspaceId).
        when().
            delete("/workspaces/{workspaceId}"). // the value of workspaceId will be resolved during execution.
        then().
                log().all().
            assertThat().
                body("workspace.id", equalTo(workspaceId)
                );
    }

}

