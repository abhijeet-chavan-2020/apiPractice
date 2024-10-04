package com.test;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeleteWithReqResSpecInBeforeClass {

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
    public void createWorkspace() {
        Faker fk= new Faker();
        workspaceName= fk.name().fullName().toUpperCase().replaceAll(" ", "") + fk.number().digits(5);
        System.out.println("workspaceName = " + workspaceName);
        workspaceId= given().
                body("{\n" +
                        "    \"workspace\": {\n" +
                        "        \"name\":\""+workspaceName+"\",\n" +
                        "        \"type\":\"personal\",\n" +
                        "        \"description\":\"Rest assured created this\"\n" +
                        "    }\n" +
                        "}").
                when().
                post("/workspaces").
                then().
                assertThat().
                body("workspace.name", equalTo(workspaceName)
                ).extract().response().getBody().jsonPath().getString("workspace.id");
        System.out.println("workspaceId = " + workspaceId);
    }

    @Test(dependsOnMethods = {"createWorkspace"})
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

    public static void main(String[] args) {
      //  HttpStatus.SC_NOT_FOUND;

    }

}

