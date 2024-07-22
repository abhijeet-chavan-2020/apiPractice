package com.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestApiPart3 {

    RequestSpecification reqSpec;

    @BeforeClass
    public void beforeClass() {
        //Use of SpecBuilder in the before class
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
        .setBaseUri("https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/")
        .addHeaders(h1)
        .log(LogDetail.ALL);
        reqSpec=  reqSpecBldr.build();
    }

    @Test
    public void baseTestCaseUsingSpecBuilder() {
        given().spec(reqSpec).
                when().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void assert_Status_code_UsingSpecBuilder() {
        Response response = given().spec(reqSpec).get("/workspaces");
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    @Test
    public void assert_Status_body_UsingSpecBuilder() {
        Response response = given().spec(reqSpec).get("/workspaces");
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name"), equalTo("Team First Workspace"));
    }

}

