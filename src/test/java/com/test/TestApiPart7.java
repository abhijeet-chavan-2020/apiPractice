package com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestApiPart7 {

    @BeforeClass
    public void beforeClass() {
        //created request and response both Default specification builder in the before class
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
        .setBaseUri("https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/")
        .addHeaders(h1)
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
    public void assert_Status_code_UsingDefaultRequestAndResponseSpec() {
                get("/workspaces");
    }

    @Test
    public void assert_Status_body_UsingDefaultRequestAndResponseSpec() {
        Response response = get("/workspaces").
        then().
                extract().
                response();
        assertThat(response.path("workspaces[0].name"), equalTo("Team First Workspace"));
    }

}

