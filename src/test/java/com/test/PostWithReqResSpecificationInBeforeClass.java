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

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class PostWithReqResSpecificationInBeforeClass {

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
        given().
        body("{\n" +
                "    \"workspace\": {\n" +
                "        \"name\":\"myFirstWorkspace1122\",\n" +
                "        \"type\":\"personal\",\n" +
                "        \"description\":\"Rest assured created this\"\n" +
                "    }\n" +
                "}").
        when().
            post("/workspaces").
        then().
            assertThat().
                body("workspace.name", equalTo("myFirstWorkspace1122"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}")
                )
        ;
    }

    @Test
    public void postMethodWithBodyAsStringNonBDD() {
        Response response= with().
                body("{\n" +
                        "    \"workspace\": {\n" +
                        "        \"name\":\"myFirstWorkspace2\",\n" +
                        "        \"type\":\"personal\",\n" +
                        "        \"description\":\"Rest assured created this\"\n" +
                        "    }\n" +
                        "}").
                post("/workspaces");
                assertThat(response.path("workspace.name"), equalTo("myFirstWorkspace2"));
                assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}"));
    }

}

