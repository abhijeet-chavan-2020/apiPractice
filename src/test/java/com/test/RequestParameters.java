package com.test;

import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class RequestParameters {

    @Test
    public void singleQueryParameter() {
        given().
                baseUri("https://postman-echo.com/").
                queryParam("foo1", "bar1").
                param("foo2", "bar2").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multipleQueryParameter() {
        HashMap<String, String> queryParam= new HashMap<String, String>();
        queryParam.put("foo1","bar1");
        queryParam.put("foo2","bar2");
        queryParam.put("foo3","bar3");
        given().
                baseUri("https://postman-echo.com/").
                queryParams(queryParam).
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multiValueQueryParameter() {
        //We can add multiple query parameters in two ways.
        // First by creating a hashmap of
        HashMap<String, String> queryParam= new HashMap<String, String>();
        queryParam.put("foo1","bar1, name1, type1");
        queryParam.put("foo5","bar5; name5; type5");
        queryParam.put("foo2","bar2");
        queryParam.put("foo3","bar3");
        given().
                baseUri("https://postman-echo.com/").
               // queryParams(queryParam).
                queryParams("foo1","bar1","foo2","bar2").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void pathParameter() {
        //to add path parameter, we use pathParam() andd supply key and value. And then in the get() method, we can use the key of the pathParam that we have added within {} braces.
        // It will internally convert the variable and get the value which is 2 in this case.
        //Effective URL will be - https://reqres.in/api/users/2
        given().
                baseUri("https://reqres.in").
                pathParam("userId","2").
                log().all().
        when().
                get("/api/users/{userId}").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }
}

