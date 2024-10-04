package com.test;

import io.restassured.config.EncoderConfig;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class MultipartFormData {

    // Data in the form of key and value pairs is passed in the body section for Post and Put request.
    // It is needed when client wants to send large amount of data to server. It is not sent in one go rather it is sent in parts. That's why its called multi part form data.
    //Rest Assured provide method called multipart that allows to specify a file, byte-array, input stream or text to upload.
    //For some advanced test cases, we can use MultiPartSpecBuilder. Example - creating objects of different user defined classes and then creating a spec of it and pass to multipart method.
    //control-name= key
    @Test
    public void multiPartFormDataWithSimpleKeyValue() {
        //this will generate following multipart form data:
        /*
        Multiparts:		------------
				Content-Disposition: form-data; name = foo1; filename = file
				Content-Type: text/plain

				bar1
				------------
				Content-Disposition: form-data; name = foo2; filename = file
				Content-Type: text/plain

				bar2
         */
        given().
                baseUri("https://postman-echo.com/").
                multiPart("foo1", "bar1").
                multiPart("foo2", "bar2").
                log().all().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multiPartFormDataWithFileUpload() {
        String attributes = "{\"name\":\"temp.txt\", \"parent\": {\"id\":\"123456\"}}";
        given().
                baseUri("https://postman-echo.com/").
                multiPart("file", "temp.txt").
                multiPart("attributes", attributes, "application/json").
                log().all().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void fileParamWithURLEncoding() {
        //here, in form url encoding, the URL is percent encoded. Means there will be some characters starting with % will be used to represent some special characters.
        String attributes = "{\"name\":\"temp.txt\", \"parent\": {\"id\":\"123456\"}}";
        given().
                baseUri("https://postman-echo.com/").
                config(config.encoderConfig(
                        EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
                )).
                formParam("key1","value1").
                formParam("key2","value2").
                log().all().
                when().
                get("/get").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

}

