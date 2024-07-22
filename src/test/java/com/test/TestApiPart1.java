package com.test;

import io.restassured.config.LogConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasSize;

public class TestApiPart1 {

    @Test
    public void test1() {
        //Response response=
        given().
                baseUri("https://api.postman.com").
                header("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01").
                //request().
                        config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
                config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-key"))).
                //   log().all().
                        when().
                get("/workspaces").
                then().
                //   log().all().
                //log().body().
                //log().everything().
                //extract().response().
                // log().ifError().
                //       log().ifValidationFails().
                        assertThat().
//                    body(
//                    "workspaces", hasSize(2),
//                    "workspaces.name", contains("Team Workspace", "My Workspace"),
//                    "workspaces.name", containsInAnyOrder("My Workspace","Team Workspace"),
//                    "workspaces.name", everyItem(endsWithIgnoringCase("Workspace")),
//                    "workspaces.name", hasItem("My Workspace"),
//                    "workspaces.name", hasItems("My Workspace","Team Workspace") ,
//                    "workspaces[0].name", is(equalTo("Team Workspace")),
//                    "workspaces[0]", hasEntry("name", "Team Workspace"),
//                    "workspaces[0]", hasKey("visibility"),
//                    "workspaces[0]", hasValue("team"),
//                    "workspaces[0]", is(not(empty())),
//                    "workspaces[0].name", allOf(startsWith("Team"), containsString("Workspace")),
//                    "workspaces[0].name", anyOf(startsWith("Team"), containsString("My Workspace")),
//                    "workspaces", iterableWithSize(2),
//                    "workspaces[0]", aMapWithSize(5),
//                    "workspaces[1]", aMapWithSize(5)
//
//
//                    ).
        statusCode(201)
        ;
//    System.out.println("All Value is :" +response.body().jsonPath().get("workspaces.name"));
//    System.out.println("Single Value is :" +response.body().jsonPath().get("workspaces[0].name"));
//    System.out.println("Size is :" +response.body().jsonPath().getList("workspaces").size());
//    System.out.println("prettify :" +response.body().jsonPath().prettify());
//    System.out.println("prettyPrint :" +response.body().jsonPath().prettyPrint());
//    System.out.println("Using path :" +response.body().path("workspaces[0]"));
//    System.out.println("JsonPath.from(response.asString()) = " + JsonPath.from(response.asString()).getString("workspaces"));
//    System.out.println("By Using GSON Builder:" + new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(response.body().asString())));

    }


    @Test
    public void test2() {
        // in this example, we can see that we can create multiple header objects and pass that to Headers object as List<Headers>
        //also we can create a HashMap and pass the Hashmap object to Headers
//        Header h1 = new Header("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
//        Header h2 = new Header("headerName", "value2");
//        Header h3 = new Header("x-mock-match-request-headers", "headerName");

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value2");
        h1.put("x-mock-match-request-headers", "headerName");
        // Headers headers = new Headers((List<Header>) h1);

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";
        given().
                baseUri(url).
                headers(h1).
                request().
                log().all().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                body(
                        "workspaces", hasSize(2)

                ).header("resHeader", "resValue2")
        ;


    }

    @Test
    public void test3() {
//in this example, the  header("multiValueHeader","value1","value2") will  produce two identical keys with values as value1 and value 2

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value2");
        h1.put("x-mock-match-request-headers", "headerName");

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";
        given().
                baseUri(url).
                headers(h1).
                header("multiValueHeader", "value1", "value2").
                request().
                log().all().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                body(
                        "workspaces", hasSize(2)
                )
        ;


    }

    @Test
    public void assert_response_header() {
//in this example, the  header("multiValueHeader","value1","value2") will  produce two identical keys with values as value1 and value 2

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value2");
        h1.put("x-mock-match-request-headers", "headerName");

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";
        given().
                baseUri(url).
                headers(h1).
                header("multiValueHeader", "value1", "value2").
                request().
                log().all().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                body(
                        "workspaces", hasSize(2)
                )
//                .header("resHeader","resValue2")
//                .header("x-ratelimit-limit","300")
                .headers("resHeader", "resValue2", "x-ratelimit-limit", "300")
        ;
    }

    @Test
    public void extract_response_header() {
//in this example, the  header("multiValueHeader","value1","value2") will  produce two identical keys with values as value1 and value 2

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";
        Headers extractedHeaders = given().
                baseUri(url).
                headers(h1).
                // header("multiValueHeader","value1","value1").
                        request().
                log().all().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                statusCode(200).
                extract().headers();
        System.out.println("extractedHeaders.get(\"resHeader\").getName() = " + extractedHeaders.get("resHeader").getName());
        System.out.println("extractedHeaders.get(\"resHeader\").getValue() = " + extractedHeaders.get("resHeader").getValue());
        System.out.println("extractedHeaders.getValue(\"resHeader\") = " + extractedHeaders.getValue("resHeader"));

        for (Header h : extractedHeaders) {
            System.out.println("h.getName() = " + h.getName() + " , and value is  " + h.getValue());
        }

        List<String> multiValueHeaderList = extractedHeaders.getValues("multiValueHeader");
        for (String mvh : multiValueHeaderList) {
            System.out.println("mvh = " + mvh);
        }
    }

    @Test
    public void differentWayToUseRequestSpecification() {

        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-af8efcc4531b580d4fc7dcc1d00b186a01");
        h1.put("headerName", "value1");
        h1.put("x-mock-match-request-headers", "headerName");

        String url = "https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io/";

        RequestSpecification reqSpec = given().
                baseUri(url).
                headers(h1).
                request().log().all();
        given().spec(reqSpec).
                when().
                get("/workspaces").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

}

