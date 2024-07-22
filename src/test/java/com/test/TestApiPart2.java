package com.test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestApiPart2 {

    RequestSpecification reqSpec;

    @BeforeClass
    public void beforeClass() {
        //Use of Request specification in different ways

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";
        reqSpec = given().
                baseUri(url).
                headers(h1).
                request().log().all();

//        reqSpec = with().
//                baseUri(url).
//                headers(h1).
//                request().log().all();
    }

    @Test
    public void baseTestCase1() {
        given().spec(reqSpec).
                when().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void assert_Status_code() {
        Response response = reqSpec.get("/workspaces");
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    @Test
    public void assert_Status_body() {
        Response response = reqSpec.get("/workspaces");
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name"), equalTo("Team First Workspace"));
    }

}

