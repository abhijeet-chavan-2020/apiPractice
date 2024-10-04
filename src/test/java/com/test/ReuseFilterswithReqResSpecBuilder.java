package com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class ReuseFilterswithReqResSpecBuilder {
        //Filter is an interface. RequestLoggingFilter is a class which implements Filter interface.
    // This class has multiple constructors and one of which accepts PrintStream object. Using this object we can log details to a file.
    //we use filter() method from RestAssured class, pass an object of RequestLoggingFilter/ ResponseLoggingFilter along with the PrintStream object to write to the log file.

    @BeforeClass
    public void beforeClass(){
        File file= new File("restassured.log");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
                .setBaseUri("https://postman-echo.com/");

        ResponseSpecBuilder responseSpecBuilder= new ResponseSpecBuilder()
                .expectStatusCode(200);

        if(!file.exists() || file.delete()){
            file.setWritable(true);
            try{
                PrintStream printStream= new PrintStream(file );
                reqSpecBldr.addFilter(new RequestLoggingFilter(printStream)).
                addFilter(new ResponseLoggingFilter(printStream));
            } catch (Exception e){
                System.out.println("File logging is failed");
            }
        }

        RestAssured.requestSpecification =  reqSpecBldr.build().log().all();
        System.out.println("Printing Response Specification");
        RestAssured.responseSpecification=  responseSpecBuilder.build();
    }

    @Test
    public void logginToFileUsingSpecBuilder() {
        given(RestAssured.requestSpecification).
        when().
            get("/get").
        then().
            spec(RestAssured.responseSpecification).
            assertThat().
            statusCode(200);
    }
}
