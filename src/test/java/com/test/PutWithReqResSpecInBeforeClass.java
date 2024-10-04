package com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class PutWithReqResSpecInBeforeClass {

    @BeforeClass
    public void beforeClass() {
        //created post request and response both Default specification builder in the before class
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
    }

    @Test
    public void postMethodWithBodyAsStringBDD() {
        //Use Payload as Body in String format.

        String workspaceId="454d433e-d8e0-4baf-b6c6-3a05a9b219ed";
        given().
        body("{\n" +
                "    \"workspace\": {\n" +
                "        \"name\":\"myFirstWorkspace2\",\n" +
                "        \"type\":\"personal\",\n" +
                "        \"description\":\"this is created by Rest assured\"\n" +
                "    }\n" +
                "}").
        pathParam("workspaceId",workspaceId).
        when().
            put("/workspaces/{workspaceId}"). // the value of workspaceId will be resolved during execution.
        then().
            assertThat().
                body("workspace.name", equalTo("myFirstWorkspace2"),
               "workspace.id", equalTo(workspaceId),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}")
                )
        ;
    }

    @Test
    public void postMethodWithBodyAsStringNonBDD() {
        String workspaceId="454d433e-d8e0-4baf-b6c6-3a05a9b219ed";
        Response response= with().
                body("{\n" +
                        "    \"workspace\": {\n" +
                        "        \"name\":\"myFirstWorkspace2\",\n" +
                        "        \"type\":\"personal\",\n" +
                        "        \"description\":\"this is created by Rest assured\"\n" +
                        "    }\n" +
                        "}").
                pathParam("workspaceId",workspaceId).
                put("/workspaces/{workspaceId}");
                assertThat(response.path("workspace.name"), equalTo("myFirstWorkspace2"));
                assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}"));
                assertThat(response.path("workspace.id"), equalTo(workspaceId));
    }

}

