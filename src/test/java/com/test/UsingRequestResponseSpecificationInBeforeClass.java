package com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
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
import static org.hamcrest.Matchers.is;

public class UsingRequestResponseSpecificationInBeforeClass {

    ResponseSpecification responseSpec;
    @BeforeClass
    public void beforeClass() {
        //created request and response specifications in the bfore class
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
        .setBaseUri("https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/")
        .addHeaders(h1)
        .log(LogDetail.ALL);
        RestAssured.requestSpecification =  reqSpecBldr.build();

        responseSpec= RestAssured.expect().statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void assert_Status_code_UsingDefaultRequestAndResponseSpec() {
                get("/workspaces").
                then().
                spec(responseSpec).
                log().all();
    }

    @Test
    public void assert_Status_body_UsingDefaultRequestAndResponseSpec() {
        Response response = get("/workspaces").
        then().spec(responseSpec).
                log().all().
                extract().
                response();
        assertThat(response.path("workspaces[0].name"), equalTo("Team First Workspace"));
    }

}

