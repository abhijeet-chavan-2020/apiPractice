package com.test;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JSONSchemaValidation {
    //JSON Schema is a declarative language for annotating and validating JSON documents' structure, constraints, and data types. It provides a way to standardize and define expectations for JSON data.
    //https://json-schema.org/docs
    // it defines that data types for the json fields/ keys.
    //use jsonschema.net to generate jsonschema automatically for the given fields./
    // there is a method callled matchesJsonSchemaInClasspath("<filename.json in which json schema is provided >"). Need to keep this file in the classpath.
    // this method is from json-schema-validator dependency from rest-assured

    @Test
    public void singleQueryParameter() {
        given().
                baseUri("https://postman-echo.com/").
                log().all().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("echoGet.json"));
    }
}
